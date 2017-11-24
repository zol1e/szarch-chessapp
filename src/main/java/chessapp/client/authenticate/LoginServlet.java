package chessapp.client.authenticate;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chessapp.server.service.LoginService;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public LoginService loginService = new LoginService();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			handleRequest(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			handleRequest(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		try {
			int result = loginService.login(request.getParameter("uname"), request.getParameter("passw"), request.getSession().getId());

			if (result == 0) {
				HttpSession session = request.getSession();
				session.setAttribute("currentSessionUser", request.getParameter("uname"));
				session.setMaxInactiveInterval(30*60);
				response.setStatus(200);
				response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/main/index")); // logged-in page
			} else {
				response.sendError(400);
			}
			
		} catch (Throwable theException) {
			System.out.println(theException);
		}
	}
}
