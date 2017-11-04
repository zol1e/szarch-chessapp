package chessapp.authenticate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chessapp.beans.LoginBean;
import chessapp.model.UserDAO;

public class LoginServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		try {

			LoginBean user = new LoginBean();
			user.setUserName(request.getParameter("uname"));
			user.setPassword(request.getParameter("passw"));

			user = UserDAO.login(user);

			if (user != null) {

				HttpSession session = request.getSession(true);
				session.setAttribute("currentSessionUser", user);
				response.sendRedirect("/index"); // logged-in page
			} else {
				response.sendRedirect("/login");
			}
				//response.sendRedirect("login.jsp"); // error page
		}

		catch (Throwable theException) {
			System.out.println(theException);
		}
	}
}
