package chessapp.authenticate;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chessapp.service.LoginService;

public class AuthFilter implements Filter {

	LoginService loginService;
	
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

		Cookie[] cookies = req.getCookies();
		Cookie user = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("user")) {
					user = cookie;
				}
			}
		}
		HttpSession session = req.getSession(false);

		if (session == null || user == null || !isValid(session.getId(), user.getValue())) {
			this.context.log("Unauthorized access request");
			res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/login"));
		} else {
			// pass the request along the filter chain
			chain.doFilter(request, response);
		}

	}

	private boolean isValid(String session, String userName) {
		return 0 == loginService.isLoggedIn(userName, session);
	}

	public void destroy() {
		// close any resources here
	}

}
