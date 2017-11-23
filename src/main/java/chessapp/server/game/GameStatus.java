package chessapp.server.game;

public class GameStatus {
	
	private String gameId;
	
	private String fen;
	
	private String moves;

	private Long whiteTime;
	
	private Long blackTime;

	private String whiteUserName;
	
	private String blackUserName;

	public GameStatus(String gameId, String fen, String moves, Long whiteTime, Long blackTime, String whiteUserName, String blackUserName) {
		this.gameId = gameId;
		this.fen = fen;
		this.moves = moves;
		this.whiteTime = whiteTime;
		this.blackTime = blackTime;
		this.whiteUserName = whiteUserName;
		this.blackUserName = blackUserName;
	}
	
	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getFen() {
		return fen;
	}

	public void setFen(String fen) {
		this.fen = fen;
	}

	public Long getWhiteTime() {
		return whiteTime;
	}

	public void setWhiteTime(Long whiteTime) {
		this.whiteTime = whiteTime;
	}

	public Long getBlackTime() {
		return blackTime;
	}

	public void setBlackTime(Long blackTime) {
		this.blackTime = blackTime;
	}
	
	public String getMoves() {
		return moves;
	}

	public void setMoves(String moves) {
		this.moves = moves;
	}
	
	
	public String getWhiteUserName() {
		return whiteUserName;
	}

	public void setWhiteUserName(String whiteUserName) {
		this.whiteUserName = whiteUserName;
	}

	public String getBlackUserName() {
		return blackUserName;
	}

	public void setBlackUserName(String blackUserName) {
		this.blackUserName = blackUserName;
	}
}
