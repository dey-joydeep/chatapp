package com.joy.gca.server;

import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

import com.joy.gca.process.GetRequestProcessor;
import com.joy.gca.process.PostRequestProcessor;
import com.joy.gca.resources.CommonResources;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server implements HttpHandler {

	public static void main(String[] args) throws Exception {
		int port = 9000;
		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
		server.createContext(CommonResources.APPLICATION_CONTEXT, new Server());
		server.start();
		System.out.println("Server started succefully at port " + port);
		String sURI = "http://localhost:" + port + CommonResources.APPLICATION_CONTEXT;
		System.out.println("Opening [" + sURI + "] in browser");
		Desktop.getDesktop().browse(new URI(sURI));
	}

	@Override
	public void handle(HttpExchange t) throws IOException {
		Object response = "";
		String hostAddr = t.getRemoteAddress().getAddress().getHostAddress();
		PostRequestProcessor postProcessor = new PostRequestProcessor();
		GetRequestProcessor getProcessor = new GetRequestProcessor();

		try {
			switch (t.getRequestMethod()) {
			case "GET":
				response = getProcessor.handleGet(t);
				break;
			case "POST":
				response = postProcessor.handlePost(t);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = Boolean.FALSE.toString();
		}

		OutputStream os = t.getResponseBody();
		t.getResponseHeaders().set("Cache-Control", "no-cache");
		if (response != null) {
			if (response instanceof String) {
				t.sendResponseHeaders(200, response.toString().getBytes().length);
				os.write(response.toString().getBytes());
			} else if (response instanceof byte[]) {
				t.sendResponseHeaders(200, ((byte[]) response).length);
				os.write((byte[]) response);
			}
		} else {
			t.sendResponseHeaders(200, 0);
		}
		os.close();
	}
}
