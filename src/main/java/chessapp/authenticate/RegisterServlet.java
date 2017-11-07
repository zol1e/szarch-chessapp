package chessapp.authenticate;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chessapp.model.UserDAO;

public class RegisterServlet extends HttpServlet {
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
	private void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		try {

			int succes = UserDAO.register(request.getParameter("uname"), request.getParameter("passw"));

			if (succes == 0) {

				response.setStatus(200);
				response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/login"));
				
			} else {
				response.sendError(400, "username occupied");
			}
				//response.sendRedirect("login.jsp"); // error page
		}

		catch (Throwable theException) {
			System.out.println(theException);
		}
	}
}
