package chessapp.server.game;

import org.eclipse.jetty.websocket.api.Session;

public class PlayerSocketTuple {
	public String playerName;
	public Session socket;
	public PlayerSocketTuple (String name, Session sess) {
		playerName = name;
		socket = sess;
	}
}
