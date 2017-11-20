package chessapp.server.game;

import org.eclipse.jetty.websocket.api.Session;

public class ColoredSubscriber extends Subscriber {
	public boolean isBlack;
	
	public ColoredSubscriber(String name, Session sess, boolean isBlack) {
		super(name, sess);
		this.isBlack = isBlack;
	}
	
	/*public Subscriber getBlackPlayer() {
		return blackPlayer;
	}
	public void setBlackPlayer(Subscriber blackPlayer) {
		this.blackPlayer = blackPlayer;
	}
	public Subscriber getWhitePlayer() {
		return whitePlayer;
	}
	public void setWhitePlayer(Subscriber whitePlayer) {
		this.whitePlayer = whitePlayer;
	}*/
	
	/*public ColoredSubscriber(String blackName, Session blackSess, String whiteName, Session whiteSess) {
		blackPlayer = new Subscriber(blackName, blackSess);
		whitePlayer = new Subscriber(whiteName, whiteSess);
	}*/
	
	/*public ColoredSubscriber() {	}
	
	public ColoredSubscriber(Subscriber black, Subscriber white) {
		blackPlayer = black;
		whitePlayer = white;
	}*/
	
	/*public Subscriber getByName(String name) {
		if (name == null || name.isEmpty())
			return null;
		if (blackPlayer != null && name.equals(blackPlayer.playerName))
			return blackPlayer;
		else if (whitePlayer != null && name.equals(whitePlayer.playerName))
			return whitePlayer;
		return null;
	}*/
	
	/*public void addSecondPlayer(String name, Session sess) {
		Subscriber player = new Subscriber(name, sess);
		if (blackPlayer == null) {
			blackPlayer = player;
		} else  if (whitePlayer == null){
			whitePlayer = player;
		}
	}*/
	
	/*public boolean isBothNull() {
		return blackPlayer == null && whitePlayer == null;
	}*/
	
	public void closeConnection() {
		if (this.socket.isOpen())
			this.socket.close();
	}
}
