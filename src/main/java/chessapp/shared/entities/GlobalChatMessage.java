package chessapp.shared.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.nosql.annotations.Field;

public class GlobalChatMessage implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Field(name = "_message_id")
	private String messageId;

	@Basic
	private String srcUserName;

	public String getMessageId() {
		return messageId;
	}
	public String getSrcUserName() {
		return srcUserName;
	}
	public String getTextMsg() {
		return textContent;
	}
	public Date getDate() {
		return date;
	}
	
	@Basic
	private String textContent;
	
	@Basic
	@Temporal(TemporalType.DATE)
	private Date date;
	
	public GlobalChatMessage() {}
	public GlobalChatMessage(String src, String msg) {
		srcUserName = src;
		textContent = msg;
		date = new Date();
	}
	
	
	
}
