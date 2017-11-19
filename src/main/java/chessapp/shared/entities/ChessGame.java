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
	private String whitePlayerName;

	@Basic
	private String blackPlayerName;

	@Basic
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date startDate;
	
	@Basic
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date endDate;

	// private GameResult result;

	public ChessGame() {
		
	}
	
	public ChessGame(String black, String white) {
		whitePlayerName = white;
		blackPlayerName = black;
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

	public String getWhitePlayer() {
		return whitePlayerName;
	}

	public void setWhitePlayer(String whitePlayer) {
		this.whitePlayerName = whitePlayer;
	}

	public String getBlackPlayer() {
		return blackPlayerName;
	}

	public void setBlackPlayer(String blackPlayer) {
		this.blackPlayerName = blackPlayer;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date date) {
		this.startDate = date;
	}
}
