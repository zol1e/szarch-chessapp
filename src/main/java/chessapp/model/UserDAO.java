package chessapp.model;

import chessapp.beans.LoginBean;

public class UserDAO {

	public static int login(LoginBean user, String sessionId) {
		// TODO Auto-generated method stub
		// put that mofo in the valid session table
		return "Alexample".equals(user.getUserName()) &&
				"123456".equals(user.getPassword()) ?
						0 : -1;
	}
	
	public static void logout(String userName, String sessionId) {
		// TODO Auto-generated method stub
		// remove that mofo from the valid sessions table
		
	}	
	
	public static int isLoggedIn(String userName, String sessionId) {
		// return 0 if that userNAme and sessionid is in there
		// return not0 if no record of userName&session in valid session table
		
		return 0;
	}
	
	public static int register(LoginBean user) {
		// TODO Auto-generated method stub
		// return 0 if succesfully registered, else smthng else
		return 0;
	}

}
