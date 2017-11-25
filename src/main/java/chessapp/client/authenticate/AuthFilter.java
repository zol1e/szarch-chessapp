package chessapp.client.authenticate;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chessapp.server.service.LoginService;

public class AuthFilter implements Filter {

	LoginService loginService = new LoginService();
	
	private ServletContext context;

	public void init(FilterConfig fConfig) throws ServletException {
		this.context = fConfig.getServletContext();
		this.context.log("AuthenticationFilter initialized");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String uri = req.getRequestURI();
		this.context.log("Requested Resource::" + uri);

		HttpSession session = req.getSession(false);

		if (session == null || !isValid(session.getId(), (String)session.getAttribute("currentSessionUser"))) {
			this.context.log("Unauthorized access request");
			
            /*res.setHeader("Location", req.getContextPath() + "/auth");
            res.sendError(403);*/
			res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/auth"));
		} else {
			// pass the request along the filter chain
			chain.doFilter(request, response);
		}

	}

	private boolean isValid(String session, String userName) {
		System.out.println("is valid " + userName + " with sessionId: " + session);
		return 0 == loginService.isLoggedIn(userName, session);
	}

	public void destroy() {
		// close any resources here
	}

}
