package com.joy.ca.controller.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.joy.ca.process.PostRequestProcessor;
import com.joy.ca.resources.Commons;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PostRequestProcessor processor = new PostRequestProcessor();
		String response = processor.login(req, resp);
		if (response == null) {
			req.getRequestDispatcher(Commons.Redirect.Page.LOGIN_PAGE).forward(req, resp);
		} else {
			if (req.getHeader(Commons.ReqHeaders.X_REQUEST) != null) {
				PrintWriter writer = resp.getWriter();
				writer.write(response.toString());
			} else {
				req.getRequestDispatcher(Commons.Redirect.Servlet.INIT).forward(req, resp);
			}
		}
	}
}