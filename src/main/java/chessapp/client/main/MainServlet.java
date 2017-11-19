package chessapp.client.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chessapp.server.model.LoginBean;
import chessapp.shared.entities.UserLogin;

public class MainServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
    public MainServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LoginBean loginBean = new LoginBean();
		
		UserLogin userLogin = loginBean.findBySessionId(request.getSession().getId());
		
		request.setAttribute("userName", userLogin.getUserName());
		request.getRequestDispatcher("/jsp/Main.jsp").forward(request, response);
	}

}