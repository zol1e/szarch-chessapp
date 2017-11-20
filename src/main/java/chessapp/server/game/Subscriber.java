package chessapp.server.game;

import org.eclipse.jetty.websocket.api.Session;

public class Subscriber {
	public String playerName;
	public Session socket;
	public Subscriber (String name, Session sess) {
		playerName = name;
		socket = sess;
	}
}
