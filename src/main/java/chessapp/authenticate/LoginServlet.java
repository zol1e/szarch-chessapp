package chessapp.authenticate;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chessapp.beans.LoginBean;
import chessapp.model.UserDAO;

public class LoginServlet extends HttpServlet {
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
			LoginBean user = new LoginBean();
			user.setUserName(request.getParameter("uname"));
			user.setPassword(request.getParameter("passw"));

			user = UserDAO.login(user);

			if (user != null) {

				HttpSession session = request.getSession(true);
				session.setAttribute("currentSessionUser", user.getUserName());
				session.setMaxInactiveInterval(30*60);
				Cookie userName = new Cookie("user", user.getUserName());
				//setting session to expiry in 30 mins
				userName.setMaxAge(30*60);
				response.addCookie(userName);
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
