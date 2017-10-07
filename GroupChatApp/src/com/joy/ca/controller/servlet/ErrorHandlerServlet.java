package com.joy.ca.controller.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.joy.ca.resources.Commons;
import com.joy.ca.utils.logger.LogHandler;

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
	private static final LogHandler _logger = new LogHandler(ErrorHandlerServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		_logger.info("Redirecting to error page");
		req.getRequestDispatcher(Commons.Redirect.Page.ERROR_PAGE).forward(req, resp);
	}
}
