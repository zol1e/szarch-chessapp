package chessapp.server;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.eclipse.jetty.websocket.api.Session;

import chessapp.server.game.PlayerSocketTuple;
import chessapp.server.game.PlayerWebsocketMapping;

public class GameSocketRepository {
	public static ConcurrentMap<String, PlayerWebsocketMapping> connections;
	
	public static void initConnections() {
		connections = new ConcurrentHashMap<String, PlayerWebsocketMapping>();
	}
	
	public static void putConnection(String gameId, PlayerWebsocketMapping tuple) {
		connections.put(gameId, tuple);
	}
	
	public static void putConnectionBlack(String gameId, String blackPlayer, Session bsess) {
		PlayerWebsocketMapping player = new PlayerWebsocketMapping();
		player.setBlackPlayer(new PlayerSocketTuple(blackPlayer, bsess));
		connections.put(gameId, player);
	}
	
	public static void putConnectionWhite(String gameId, String whitePlayer, Session wsess) {
		PlayerWebsocketMapping player = new PlayerWebsocketMapping();
		player.setWhitePlayer(new PlayerSocketTuple(whitePlayer, wsess));
		connections.put(gameId, player);
	}
	
	public static void putConnection(String gameId, String whitePlayer, Session wsess, String blackPlayer, Session bsess) {
		connections.put(gameId, new PlayerWebsocketMapping(blackPlayer, bsess, whitePlayer, wsess));
	}
	
	public static void addSecondPlayer(String gameId, String name, Session sess) {
		connections.get(gameId).addSecondPlayer(name, sess);
	}
	
	public static void removeConnection(String gameId, String userName) {
		if (userName == null || userName.isEmpty())
			return;
		
		PlayerWebsocketMapping mapping = connections.get(gameId);
		if (mapping == null)
			return;
		if (userName.equals(mapping.getBlackPlayer() == null ? "" : mapping.getBlackPlayer().playerName)) {
			mapping.setBlackPlayer(null);
		} else if (userName.equals(mapping.getWhitePlayer() == null ? "" : mapping.getWhitePlayer().playerName)) {
			mapping.setWhitePlayer(null);
		}
		if (mapping.isBothNull())
			connections.remove(gameId, mapping);
	}
	
	public static void releaseConnections() {
		for(Entry<String, PlayerWebsocketMapping> connection : connections.entrySet()) {
			connection.getValue().closeConnections();
		}
	}
}
