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

import org.joy.ca.beans.UserInfo;
import org.joy.ca.process.CommonProcessor;
import org.joy.ca.process.GetRequestProcessor;
import org.joy.ca.resources.CommonResources;

/**
 * Controls activities related to chat page. It loads the main chat window with
 * other resources in background. The URL patterns are also divided accordingly.
 *
 * @author Joydeep Dey
 *
 */
@WebServlet(urlPatterns = { "/chat", "/chat/emoji", "/chat/recover", "/chat/allusers" })
public class ChatServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Load all the resources, called by background processes of chat window
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);

		String response = CommonResources.RESPONSE_ERROR;
		Map<String, String> paramValue = CommonProcessor.getRequestQuery(req.getQueryString());
		String token = paramValue.get("token");

		if (CommonProcessor.checkLoggedInUserByToken(token, session.getAttribute("token"))) {
			String path = req.getRequestURI();
			path = path.substring(path.lastIndexOf('/') + 1, path.length());
			GetRequestProcessor processor = new GetRequestProcessor();
			int loginId = Integer.parseInt(paramValue.get("loginId"));

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
				response = processor.getAllFriends(loginId);
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

	/**
	 * Load the chat page and set info of logged in user in request attribute for
	 * further processing in JSP.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		Map<String, String> formData = CommonProcessor.getRequestBody(req.getInputStream());
		String token = formData.get("token");
		int loginId = Integer.parseInt(formData.get("loginId"));
		boolean isValidUser = CommonProcessor.checkLoggedInUserByToken(token, session.getAttribute("token"));

		if (isValidUser) {
			UserInfo userInfo = CommonProcessor.getUserDetailsByLoginId(loginId);
			userInfo.setToken(token);
			req.setAttribute("user", userInfo);
			req.getRequestDispatcher(CommonResources.CLIENT_PAGE).forward(req, resp);
		} else {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
