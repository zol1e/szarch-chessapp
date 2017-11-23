package chessapp.server.game;

public class GameStatus {

	private String fen;
	
	private Long whiteTime;
	
	private Long blackTime;
	
	private String onMove;
	
	public GameStatus(String fen, Long whiteTime, Long blackTime, String onMove) {
		super();
		this.fen = fen;
		this.whiteTime = whiteTime;
		this.blackTime = blackTime;
		this.onMove = onMove;
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

	public String getOnMove() {
		return onMove;
	}

	public void setOnMove(String onMove) {
		this.onMove = onMove;
	}
}
