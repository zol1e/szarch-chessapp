package chessapp.main;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chessapp.model.UserBean;
import chessapp.shared.entities.User;

@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {

	private UserBean userBean;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<User> users = userBean.findAll();
		request.setAttribute("users", users);
		request.getRequestDispatcher("/usersTable.jsp").forward(request, response);
	}

}
