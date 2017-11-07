package chessapp.shared.entities;

import java.util.Date;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator
public final class User extends AbstractEntity {

	private String userId;

	private String nickName;
	
	private String password;

	private String firstName;
	
	private String lastName;
	
	private Date joinDate;
	
	@BsonCreator
	public User(@BsonProperty("personId") String userId, 
				@BsonProperty("nickName") String nickName,
				@BsonProperty("firstName") String firstName,
				@BsonProperty("lastName") String lastName,
				@BsonProperty("joinDate") Date joinDate) {
		
		super();
		this.nickName = nickName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.joinDate = joinDate;
	}
	
	@BsonId
	public String getUserId() {
		return userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
