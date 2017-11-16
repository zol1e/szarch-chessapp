package chessapp.service;

import chessapp.model.LoginBean;
import chessapp.model.UserBean;
import chessapp.shared.entities.User;
import chessapp.shared.entities.UserLogin;

public class LoginService {

	private LoginBean loginBean = new LoginBean();
	private UserBean userBean = new UserBean();
	
	public int login(String userName, String password, String sessionId) {
		if (userName == null || password == null || sessionId == null)
			return -1;
		
		UserLogin newer = new UserLogin(userName, sessionId, null);
		
		UserLogin old = loginBean.findByName(userName);		
		if (old != null) {
			loginBean.modify(newer);
			return 0;
		}
		
		User usr = userBean.findByNameNPassword(userName, password);
		if (usr != null){
			loginBean.create(newer);
			return 0;
		}
		return -1;
	}
	
	public void logout(String userName, String sessionId) {
		loginBean.delete(loginBean.findByName(userName));
	}	
	
	public int isLoggedIn(String userName, String sessionId) {
		UserLogin old = loginBean.findByName(userName);
		
		if (old == null || !old.getSessionId().equals(sessionId))
				return -1;
						
		return 0;
	}
	
	public int register(String userName, String password) {
		if (null != userBean.findByName(userName))
			return -1;
		User user = new User();
		user.setFirstName("");
		user.setLastName("");
		user.setNickName(userName);
		user.setPassword(password);
		
		userBean.create(user);
		return 0;
	}
	
}
