package chessapp.authenticate;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chessapp.service.LoginService;

public class LogoutServlet extends HttpServlet {
	
	public LoginService loginService;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	Cookie[] cookies = request.getCookies();
    	
    	if(cookies != null){
	    	for(Cookie cookie : cookies){
	    		if(cookie.getName().equals("user")){
	    			loginService.logout(cookie.getValue(), request.getSession().getId());
	    		}
	    	}
    	}
    	//invalidate the session if exists
    	HttpSession session = request.getSession(false);
    	if(session != null){
    		session.invalidate();
    	}
    	response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/login"));
    }
}
