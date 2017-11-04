package chessapp.model;

import chessapp.beans.LoginBean;

public class UserDAO {

	public static LoginBean login(LoginBean user) {
		// TODO Auto-generated method stub
		return "Alexample".equals(user.getUserName()) &&
				"123456".equals(user.getPassword()) ?
						user : null;
	}
	
	public static void logout(String userName) {
		// TODO Auto-generated method stub
		// remove sessionid n shit
		
	}	
	
	public static int register(LoginBean user) {
		// TODO Auto-generated method stub
		// return 0 if succesfully registered, else smthng else
		return 0;
	}

}
