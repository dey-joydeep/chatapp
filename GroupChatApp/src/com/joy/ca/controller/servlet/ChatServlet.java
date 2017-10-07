package com.joy.ca.controller.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.joy.ca.beans.UserInfo;
import com.joy.ca.process.CommonProcessor;
import com.joy.ca.process.GetRequestProcessor;
import com.joy.ca.resources.Commons;
import com.joy.ca.utils.logger.LogHandler;

/**
 * Controls activities related to chat page. It loads the main chat window with
 * other resources in background. The URL patterns are also divided accordingly.
 *
 * @author Joydeep Dey
 *
 */
@WebServlet(urlPatterns = { "/chat", "/chat/emoji", "/chat/recover", "/chat/allusers" }, asyncSupported = true)
public class ChatServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final LogHandler _logger = new LogHandler(ChatServlet.class);

	/**
	 * Load all the resources, called by background processes of chat window
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		long loginId;

		Map<String, String> paramValue;
		GetRequestProcessor processor = new GetRequestProcessor();

		String response = Commons.Response.ERROR;

		String path = req.getRequestURI();
		path = path.substring(path.lastIndexOf(Commons.General.URL_PATH_SEPERATOR) + 1, path.length());

		switch (path) {
		case "emoji":
			// Load all Emojis (TODO: Request based loading)
			response = processor.loadEmojis();
			break;
		case "recover":
			// Load previous chat records (TODO: Incremental loading)
			response = processor.getOldChats();
			break;
		case "allusers":
			// Load the friend lists
			paramValue = CommonProcessor.getRequestQuery(req.getQueryString());
			loginId = Long.parseLong(paramValue.get(Commons.ReqParams.LOGIN_ID));
			response = processor.getAllFriends(loginId);
			break;
		default:
			// Redirect from InitServlet. Get loginId from request attribute
			loginId = (long) req.getAttribute(Commons.ReqAttribs.LOGIN_ID);
			UserInfo userInfo = CommonProcessor.getUserDetailsByLoginId(loginId);
			req.setAttribute(Commons.ReqAttribs.USER_INFO, userInfo);
			req.getRequestDispatcher(Commons.Redirect.Page.CLIENT).forward(req, resp);
			_logger.debug("Chat page opend.");
			break;
		}

		// If the response is not empty or -1, the content is always a JSON
		if (!response.isEmpty() && !Commons.Response.ERROR.equals(response)) {
			resp.setContentType(Commons.Response.ContentType.APPLICATION_JSON);
			resp.setCharacterEncoding(Commons.General.UTF_8);
		}
		PrintWriter writer = resp.getWriter();
		writer.write(response);
	}	
}
