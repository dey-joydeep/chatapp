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
import org.joy.ca.process.PostRequestProcessor;
import org.joy.ca.resources.CommonResources;

/**
 * Common servlet to process POST requests from Chat window.<br>
 * POST request contains- 1) Sending message 2) Clear Chat Log 3) Logout
 *
 * @author Joydeep Dey
 *
 */
@WebServlet(urlPatterns = "/send", asyncSupported = true)
public class EventControlServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		Map<String, String> formData = CommonProcessor.getRequestBody(req.getInputStream());
		String token = formData.get("token");
		String response = CommonResources.RESPONSE_ERROR;

		if (CommonProcessor.checkLoggedInUserByToken(token, session.getAttribute("token"))) {
			PostRequestProcessor processor = new PostRequestProcessor();
			int eventId = Integer.parseInt(formData.get("eventId"));

			switch (eventId) {
			case 1:
				// Write new chat message to JSON file (to be changed in DB)
				int loginId = Integer.parseInt(formData.get("loginId"));
				String message = formData.get("message");
				response = processor.writeMessage(loginId, message, req.getRemoteHost());
				break;
			case 2:
				// Execute Logout
				session.invalidate();
				response = processor.logout(token);
				break;
			case 3:
				// Execute chat clear (Need to implement more precisely)
				response = processor.clearChats();
				break;
			default:
				break;
			}

		}

		PrintWriter writer = resp.getWriter();
		writer.write(response);
	}
}
