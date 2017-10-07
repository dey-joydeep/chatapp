package com.joy.ca.controller.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.joy.ca.process.CommonProcessor;
import com.joy.ca.process.PostRequestProcessor;
import com.joy.ca.resources.Commons;
import com.joy.ca.utils.logger.LogHandler;

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
	private static final LogHandler _logger = new LogHandler(EventControlServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String response = Commons.General.EMPTY_STRING;
		try {
			Map<String, String> formData = CommonProcessor.getRequestBody(req.getInputStream());
			_logger.info("Form Data: " + formData.toString());
			long loginId = Long.parseLong(formData.get(Commons.ReqParams.LOGIN_ID));

			PostRequestProcessor processor = new PostRequestProcessor();
			int eventId = Integer.parseInt(formData.get(Commons.ReqParams.EVENT_ID));

			switch (eventId) {
			case 1:
				// Write new chat message to JSON file (to be changed in DB)
				String message = formData.get(Commons.ReqParams.MESSAGE);
				response = processor.writeMessage(loginId, message, req.getRemoteHost());
				break;
			case 2:
				// Execute Logout
				processor.logout(req, resp);
				response = Commons.Response.SUCCESS;
				_logger.info("1 user is logged out. Login ID: " + loginId);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			response = Commons.Response.ERROR;
			_logger.error(e);
		} finally {
			PrintWriter writer = resp.getWriter();
			writer.write(response);
		}
	}
}
