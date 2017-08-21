package org.joy.ca.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joy.ca.process.CommonProcessor;
import org.joy.ca.process.GetRequestProcessor;
import org.joy.ca.resources.CommonResources;

/**
 * This servlet is particularly used to serve requests from WebWorkers of chat
 * window. The WebWorkers are used to check the online users and new message
 * arrival. However, this processes should be replaced by the WebSocket
 * implementation to avoid continuous accessing to server.
 *
 * @author Joydeep Dey
 *
 */
@WebServlet({ "/worker/users", "/worker/load" })
public class WebWrokerServlet extends HttpServlet {

	private static final long serialVersionUID = 5023949434633527784L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		String response = CommonResources.RESPONSE_ERROR;
		Map<String, String> paramValue = CommonProcessor.getRequestQuery(req.getQueryString());
		String token = paramValue.get("token");

		if (CommonProcessor.checkLoggedInUserByToken(token, session)) {
			String path = req.getRequestURI();
			path = path.substring(path.lastIndexOf('/') + 1, path.length());
			GetRequestProcessor processor = new GetRequestProcessor();

			switch (path) {
			case "users":
				// Check and return logged in users
				response = processor.getLoggedInUsers(token);
				break;
			case "load":
				// Fetch and return next message, correspond to given message ID
				int lastMsgId = Integer.parseInt(paramValue.get("msgId"));
				response = processor.getMessage(lastMsgId);
				break;
			default:
				break;
			}
		}

		// If the response is not empty or -1, the content is always a JSON
		if (!response.isEmpty() && !CommonResources.RESPONSE_ERROR.equals(response))
			resp.setContentType("application/json");

		PrintWriter writer = resp.getWriter();
		writer.write(response);
	}
}
