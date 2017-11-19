package chessapp.server.game;

import org.eclipse.jetty.websocket.api.Session;

public class PlayerWebsocketMapping {
	PlayerSocketTuple blackPlayer;
	PlayerSocketTuple whitePlayer;
	public PlayerSocketTuple getBlackPlayer() {
		return blackPlayer;
	}
	public void setBlackPlayer(PlayerSocketTuple blackPlayer) {
		this.blackPlayer = blackPlayer;
	}
	public PlayerSocketTuple getWhitePlayer() {
		return whitePlayer;
	}
	public void setWhitePlayer(PlayerSocketTuple whitePlayer) {
		this.whitePlayer = whitePlayer;
	}
	
	public PlayerWebsocketMapping(String blackName, Session blackSess, String whiteName, Session whiteSess) {
		blackPlayer = new PlayerSocketTuple(blackName, blackSess);
		whitePlayer = new PlayerSocketTuple(whiteName, whiteSess);
	}
	
	public PlayerWebsocketMapping() {	}
	
	public PlayerWebsocketMapping(PlayerSocketTuple black, PlayerSocketTuple white) {
		blackPlayer = black;
		whitePlayer = white;
	}
	
	public PlayerSocketTuple getByName(String name) {
		if (name.isEmpty())
			return null;
		if (blackPlayer != null && name.equals(blackPlayer.playerName))
			return blackPlayer;
		else if (whitePlayer != null && name.equals(whitePlayer.playerName))
			return whitePlayer;
		return null;
	}
	
	public void addSecondPlayer(String name, Session sess) {
		PlayerSocketTuple player = new PlayerSocketTuple(name, sess);
		if (blackPlayer == null) {
			blackPlayer = player;
		} else  if (whitePlayer == null){
			whitePlayer = player;
		}
	}
	
	public boolean isBothNull() {
		return blackPlayer == null && whitePlayer == null;
	}
	
	public void closeConnections() {
		if (blackPlayer != null)
			blackPlayer.socket.close();
		if (whitePlayer != null)
			whitePlayer.socket.close();
	}
}
