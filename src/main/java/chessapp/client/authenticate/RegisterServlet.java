package chessapp.client.authenticate;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chessapp.server.service.LoginService;

public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private LoginService loginService = new LoginService();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
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
			int succes = loginService.register(request.getParameter("uname"), request.getParameter("passw"));

			if (succes == 0) {
				response.setStatus(200);
				response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/auth"));
			} else {
				response.sendError(400, "username occupied");
			}
		} catch (Throwable theException) {
			System.out.println(theException);
		}
	}
}
