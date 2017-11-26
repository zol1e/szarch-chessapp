package chessapp.client.main;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
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
		
		ServletRegistration reg = getServletContext().getServletRegistrations().get("WebSocketServlet");
		Iterator<String> iterator = reg.getMappings().iterator();
		String mapping = iterator.next();
		String host = request.getHeader("host");
		String wsAddress = "\"wss://" + host + mapping + "\"";
		
		request.setAttribute("userName", userLogin.getUserName());
		request.setAttribute("wsAddress", wsAddress);
		request.getRequestDispatcher("/jsp/Main.jsp").forward(request, response);
	}

}