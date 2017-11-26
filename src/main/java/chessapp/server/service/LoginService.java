package chessapp.server.service;

import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;

import chessapp.server.model.LoginBean;
import chessapp.server.model.UserBean;
import chessapp.shared.entities.User;
import chessapp.shared.entities.UserLogin;

public class LoginService {

	private LoginBean loginBean = new LoginBean();
	private UserBean userBean = new UserBean();
	
	public int login(String userName, String password, String sessionId) {
		if (userName == null || password == null || sessionId == null ||
				userName.isEmpty() || password.isEmpty() || sessionId.isEmpty())
			return -1;
		System.out.println("loggin in as " + userName + " with sessionId: " + sessionId);
		String pwHashed = Hashing.sha256()
				  .hashString(password, StandardCharsets.UTF_8)
				  .toString();
		UserLogin newer = new UserLogin(userName, sessionId, null);
		
		UserLogin old = loginBean.findByName(userName);		
		if (old != null) {
			loginBean.modify(newer);
			return 0;
		}
		
		User usr = userBean.findByNameNPassword(userName, pwHashed);
		if (usr != null){
			loginBean.create(newer);
			return 0;
		}
		return -1;
	}
	
	public void logout(String userName, String sessionId) {
		System.out.println("loggin out as " + userName + " with sessionId: " + sessionId);
		UserLogin user;
		if (userName == null || userName.isEmpty())
			user = loginBean.findBySessionId(sessionId);
		else
			user = loginBean.findByName(userName);
		if (user != null)
			loginBean.delete(user);
	}	
	
	public int isLoggedIn(String userName, String sessionId) {
		System.out.println("is logged in as " + userName + " with sessionId: " + sessionId);
		UserLogin old = loginBean.findByName(userName);
		
		if (old == null || !old.getSessionId().equals(sessionId))
				return -1;
						
		return 0;
	}
	
	public int register(String userName, String password) {
		System.out.println("try to register as " + userName);
		if (null != userBean.findByName(userName) || 
				userName == null || userName.isEmpty() || password == null || password.isEmpty())
			return -1;
		String pwHashed = Hashing.sha256()
				  .hashString(password, StandardCharsets.UTF_8)
				  .toString();
		User user = new User();
		user.setNickName(userName);
		user.setPassword(pwHashed);
		
		userBean.create(user);
		return 0;
	}
	
}
