package chessapp.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import chessapp.model.UserBean;

@Stateless
public class LoginService {

	@EJB
	private UserBean userBean;
	
	public int login(String userName, String password, String sessionId) {
		// TODO Auto-generated method stub
		// put that mofo in the valid session table
		return "Alexample".equals(userName) &&
				"123456".equals(password) ?
						0 : -1;
	}
	
	public void logout(String userName, String sessionId) {
		// TODO Auto-generated method stub
		// remove that mofo from the valid sessions table
		
	}	
	
	public int isLoggedIn(String userName, String sessionId) {
		// return 0 if that userNAme and sessionid is in there
		// return not0 if no record of userName&session in valid session table
		
		return 0;
	}
	
	public int register(String userName, String password) {
		// TODO Auto-generated method stub
		// return 0 if succesfully registered, else smthng else
		return 0;
	}
	
}
