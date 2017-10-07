package com.joy.ca.controller.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.joy.ca.resources.Commons;
import com.joy.ca.utils.logger.LogHandler;

/**
 * Starting servlet to handle login page redirection (GET→ /init) and login
 * request processing (POST→ /login)
 *
 * @author Joydeep Dey
 *
 */
@WebServlet(urlPatterns = { "/init" })
public class InitServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final LogHandler _logger = new LogHandler(InitServlet.class);

	/**
	 * Called from web.xml>welcome file. Redirect to login page
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		_logger.debug("Page requested from IP: " + req.getRemoteAddr());

		HttpSession session = req.getSession(false);

		if (session != null && session.getAttribute(Commons.ReqAttribs.LOGIN_ID) != null) {
			req.setAttribute(Commons.ReqAttribs.LOGIN_ID, session.getAttribute(Commons.ReqAttribs.LOGIN_ID));
			req.getRequestDispatcher(Commons.Redirect.Servlet.CHAT).forward(req, resp);
			return;
		}

		Cookie[] cookies = req.getCookies();
		if (cookies != null)
			for (int i = 0; i < cookies.length; i++) {
				if (Commons.Cookies.AUTO_LOGIN.equals(cookies[i].getName())) {
					req.setAttribute(Commons.ReqAttribs.LOGIN_TOKEN, cookies[i].getValue());
					req.getRequestDispatcher(Commons.Redirect.Servlet.LOGIN).forward(req, resp);
					return;
				}
			}

		req.getRequestDispatcher(Commons.Redirect.Page.LOGIN_PAGE).forward(req, resp);
	}
}
