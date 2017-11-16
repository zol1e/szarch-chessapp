package chessapp.authenticate;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chessapp.service.LoginService;

public class LoginServlet extends HttpServlet {
	
	public LoginService loginService;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			handleRequest(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			handleRequest(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
				//Cookie userName = new Cookie("user", request.getParameter("uname"));
				//setting session to expiry in 30 mins
				//userName.setMaxAge(30*60);
				//response.addCookie(userName);
				response.setStatus(200);
				response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/game00/index")); // logged-in page
				
			} else {
				response.sendError(400);
			}
				//response.sendRedirect("login.jsp"); // error page
		}

		catch (Throwable theException) {
			System.out.println(theException);
		}
	}
}
