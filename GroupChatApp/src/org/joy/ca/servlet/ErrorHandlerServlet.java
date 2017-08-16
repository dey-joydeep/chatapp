package org.joy.ca.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joy.ca.resources.CommonResources;

/**
 * Handle redirection to error page on occurring of either exception or HTTP
 * error. The error redirection is directly being controlled from web.xml
 *
 * @author Joydeep Dey
 *
 */
@WebServlet("/error")
public class ErrorHandlerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher(CommonResources.ERROR_PAGE).forward(req, resp);
	}
}
