package chessapp.shared.entities;

import java.util.Date;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator
public class ChessGame extends AbstractEntity {
	
	public static enum GameResult {
		WHITE_WON,
		BLACK_WON,
		DRAW
	};
	
	private String chessGameId;

	private User whitePlayer;
	
	private User blackPlayer;
	
	private Date date;
	
	private GameResult result;
	
	@BsonCreator
	public ChessGame(@BsonProperty("chessGameId") String chessGameId,
					 @BsonProperty("whitePlayer") User whitePlayer, 
					 @BsonProperty("blackPlayer") User blackPlayer,
					 @BsonProperty("date") Date date,
					 @BsonProperty("result") GameResult result) {
		
		super();
		this.chessGameId = chessGameId;
		this.whitePlayer = whitePlayer;
		this.blackPlayer = blackPlayer;
		this.date = date;
		this.result = result;
	}
	
	@BsonId
	public String getChessGameId() {
		return chessGameId;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public GameResult getResult() {
		return result;
	}

	public void setResult(GameResult result) {
		this.result = result;
	}
	
}
