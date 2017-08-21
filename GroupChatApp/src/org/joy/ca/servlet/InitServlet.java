package org.joy.ca.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joy.ca.process.PostRequestProcessor;
import org.joy.ca.resources.CommonResources;
import org.joy.ca.resources.GlobalResources;

/**
 * Starting servlet to handle login page redirection (GET→ /init) and login
 * request processing (POST→ /login)
 *
 * @author Joydeep Dey
 *
 */
@WebServlet(urlPatterns = { "/init", "/login" }, loadOnStartup = 1)
public class InitServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig config) throws ServletException {

		// TODO: To be removed. All resources will be loaded from DB or handling
		// method must be improved (Only in case of EMOJIs).
		if (GlobalResources.RESOURCE_PATH_MAP.isEmpty()) {
			File file = new File(config.getServletContext().getRealPath("/data"));
			File[] files = file.listFiles();
			if (files != null)
				for (File f : files)
					GlobalResources.RESOURCE_PATH_MAP.put(f.getName(), f.getAbsolutePath());
		}

		super.init(config);
	}

	/**
	 * Called from web.xml>welcome file. Redirect to login page
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher(CommonResources.LOGIN_PAGE).forward(req, resp);
	}

	/**
	 * Process the login request(AJAX request) and return the result
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PostRequestProcessor processor = new PostRequestProcessor();
		String response = processor.login(req);
		PrintWriter writer = resp.getWriter();
		writer.write(response.toString());

	}
}
