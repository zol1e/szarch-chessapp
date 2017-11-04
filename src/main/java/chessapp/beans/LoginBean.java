package chessapp.beans;

public class LoginBean {
	private String userName;
	private String password;

	public void setUserName(String name) {
		userName = name;		
	}

	public void setPassword(String pword) {
		password = pword;		
	}
	public String getPassword() {
		return password;		
	}

	public String getUserName() {
		return userName;
	}


}
