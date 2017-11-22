package chessapp.shared.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.nosql.annotations.DataFormatType;
import org.eclipse.persistence.nosql.annotations.Field;
import org.eclipse.persistence.nosql.annotations.NoSql;

@Entity
@NoSql(dataFormat = DataFormatType.MAPPED)
public class GlobalChatMessage implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Field(name = "_message_id")
	private String messageId;

	@Basic
	private String srcUserName;
	
	@Basic
	private String textContent;
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	public GlobalChatMessage() {
		super();
	}
	
	public GlobalChatMessage(String src, String msg) {
		srcUserName = src;
		textContent = msg;
		date = new Date();
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getMessageId() {
		return messageId;
	}

	public String getSrcUserName() {
		return srcUserName;
	}

	public String getTextContent() {
		return textContent;
	}

	public Date getDate() {
		return date;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public void setSrcUserName(String srcUserName) {
		this.srcUserName = srcUserName;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
