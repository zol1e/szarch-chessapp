package chessapp.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chessapp.model.UserBean;
import chessapp.shared.entities.User;

@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {

	private UserBean userBean = new UserBean();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = new User();
		user.setFirstName("asdsas");
		user.setLastName("sfef");
		user.setNickName("fesfs");
		user.setPassword("sssvv");
		
		userBean.create(user);
		
		//List<User> users = userBean.findAll();
		//request.setAttribute("users", users);
		request.getRequestDispatcher("/usersTable.jsp").forward(request, response);
	}

}
