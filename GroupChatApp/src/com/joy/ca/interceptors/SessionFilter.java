package com.joy.ca.interceptors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.joy.ca.process.CommonProcessor;

@WebFilter(urlPatterns = "/*", filterName = "SessionFilter")
public class SessionFilter implements Filter {

	private static final String[] EXCLUSION_PATH = { "/chatapp", "/init", "/images/", "/css/", "/js/" };

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		boolean proceed = true;
		HttpServletRequest req = (HttpServletRequest) request;

		if (isAuthenticationRequired(req.getRequestURI())) {
			HttpSession session = req.getSession(false);

			if (!CommonProcessor.isAuthenticated(session)) {
				proceed = false;
				if (!CommonProcessor.isAsyncReq(req)) {
					req.getRequestDispatcher("/").forward(req, response);
				} else {
					((HttpServletResponse) response).setStatus(HttpServletResponse.SC_NOT_FOUND);
				}
			}
		}

		if (proceed)
			chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

	private static final boolean isAuthenticationRequired(String path) {
		for (String exPath : EXCLUSION_PATH) {
			if (path.contains(exPath))
				return false;
		}
		return true;
	}
}
