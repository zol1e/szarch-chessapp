package chessapp.server.game;

public class GameStatus {

	public enum GameResult {
		RESULT_BLACK_WON ("black won"),
		RESULT_WHITE_WON ("white won"),
		RESULT_DRAW ("draw");
		
		private String stringValue;
		
		GameResult(String stringValue) {
			this.stringValue = stringValue;
		}
		
		public final String getStringValue() {
			return stringValue;
		} 
		
		public static final GameResult getGameResultFromString(String stringValue) {
			if(stringValue == null) {
				return null;
			}
			if(stringValue.equals(RESULT_BLACK_WON.getStringValue())) {
				return RESULT_BLACK_WON;
			}
			if(stringValue.equals(RESULT_WHITE_WON.getStringValue())) {
				return RESULT_WHITE_WON;
			}
			if(stringValue.equals(RESULT_DRAW.getStringValue())) {
				return RESULT_DRAW;
			}
			return null;
		}
	}
	
	private String gameId;

	private String fen;

	private String moves;

	private Long whiteTime;

	private Long blackTime;

	private String whiteUserName;

	private String blackUserName;

	private boolean whiteDrawOffer;

	private boolean blackDrawOffer;

	private GameResult gameResult;

	public GameStatus(String gameId, String fen, String moves, Long whiteTime, Long blackTime, String whiteUserName,
			String blackUserName, boolean whiteDrawOffer, boolean blackDrawOffer, GameResult gameResult) {
		this.gameId = gameId;
		this.fen = fen;
		this.moves = moves;
		this.whiteTime = whiteTime;
		this.blackTime = blackTime;
		this.whiteUserName = whiteUserName;
		this.blackUserName = blackUserName;
		this.whiteDrawOffer = whiteDrawOffer;
		this.blackDrawOffer = blackDrawOffer;
		this.gameResult = gameResult;
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
		
	public GameResult getGameResult() {
		return gameResult;
	}

	public void setGameResult(GameResult gameResult) {
		this.gameResult = gameResult;
	}
}
