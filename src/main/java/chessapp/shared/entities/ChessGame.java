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
	private static final long serialVersionUID = 1L;

	/** Constant for "onMove" field */
	public static final String WHITE = "white";
	
	/** Constant for "onMove" field */
	public static final String BLACK = "black";
	
	/** Result constants */
	public static final String DRAW = "draw";
	public static final String CHECKMATE = "checkmate";
	public static final String TIME_EXPIRED = "on time";
	
	@Id
	@GeneratedValue
	@Field(name = "_id")
	private String chessGameId;

	@Basic
	private String whitePlayerName;

	@Basic
	private String blackPlayerName;
	
	@Basic
	private String result;

	/**
	 * Current position in FEN format
	 */
	@Basic
	private String fen;

	/**
	 * Game moves
	 */
	@Basic
	private String moves;

	/** 
	 * 	Store the last time, when a move happened. 
	 * 	It used to count the elapsed time.
	 */
	@Basic
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date lastMoveTime;
	
	/** Current time left of the white player */
	@Basic
	private Long whiteTimeLeft;
	
	/** Current time left of the white player */
	@Basic
	private Long blackTimeLeft;

	@Basic
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date startDate;
	
	@Basic
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date endDate;

	@Basic
	private boolean whiteDrawOffer;
	
	@Basic
	private boolean blackDrawOffer;
	
	public ChessGame() {
		super();
	}
	
	public ChessGame(String black, String white) {
		super();
		whitePlayerName = white;
		blackPlayerName = black;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
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

	public Long getWhiteTimeLeft() {
		return whiteTimeLeft;
	}

	public void setWhiteTimeLeft(Long whiteTimeLeft) {
		this.whiteTimeLeft = whiteTimeLeft;
	}

	public Long getBlackTimeLeft() {
		return blackTimeLeft;
	}

	public void setBlackTimeLeft(Long blackTimeLeft) {
		this.blackTimeLeft = blackTimeLeft;
	}

	public Date getLastMoveTime() {
		return lastMoveTime;
	}

	public void setLastMoveTime(Date lastMoveTime) {
		this.lastMoveTime = lastMoveTime;
	}
		
	public String getFen() {
		return fen;
	}

	public void setFen(String fen) {
		this.fen = fen;
	}
	
	public String getMoves() {
		return moves;
	}

	public void setMoves(String moves) {
		this.moves = moves;
	}
	
	public boolean isWhiteDrawOffer() {
		return whiteDrawOffer;
	}

	public void setWhiteDrawOffer(boolean whiteDrawOffer) {
		this.whiteDrawOffer = whiteDrawOffer;
	}

	public boolean isBlackDrawOffer() {
		return blackDrawOffer;
	}

	public void setBlackDrawOffer(boolean blackDrawOffer) {
		this.blackDrawOffer = blackDrawOffer;
	}
}
