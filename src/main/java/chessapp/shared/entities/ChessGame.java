package chessapp.shared.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;

import org.eclipse.persistence.nosql.annotations.DataFormatType;
import org.eclipse.persistence.nosql.annotations.Field;
import org.eclipse.persistence.nosql.annotations.NoSql;

@Entity
@NoSql(dataFormat = DataFormatType.MAPPED)
public class ChessGame implements Serializable {

	/*
	 * public static enum GameResult { WHITE_WON, BLACK_WON, DRAW };
	 */

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Field(name = "_id")
	private String chessGameId;

	@Basic
	private User whitePlayer;

	@Basic
	private User blackPlayer;

	@Basic
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date startDate;
	
	@Basic
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date endDate;

	// private GameResult result;

	public ChessGame() {
		
	}
	
	public ChessGame(User black, User white) {
		whitePlayer = white;
		blackPlayer = black;
		startDate = new Date();
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getChessGameId() {
		return chessGameId;
	}

	public void setChessGameId(String chessGameId) {
		this.chessGameId = chessGameId;
	}

	public User getWhitePlayer() {
		return whitePlayer;
	}

	public void setWhitePlayer(User whitePlayer) {
		this.whitePlayer = whitePlayer;
	}

	public User getBlackPlayer() {
		return blackPlayer;
	}

	public void setBlackPlayer(User blackPlayer) {
		this.blackPlayer = blackPlayer;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date date) {
		this.startDate = date;
	}
}
