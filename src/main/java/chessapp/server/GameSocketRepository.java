package chessapp.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.eclipse.jetty.websocket.api.Session;

import chessapp.server.game.ColoredSubscriber;

public class GameSocketRepository {
	public static ConcurrentMap<String, List<ColoredSubscriber>> connections;
	
	public static void initConnections() {
		connections = new ConcurrentHashMap<String, List<ColoredSubscriber>>();
	}
	
	public static void putConnection(String gameId, List<ColoredSubscriber> players) {
		connections.put(gameId, players);
	}
	
	public static List<ColoredSubscriber> getSubscribers(String gameId) {
		return connections.get(gameId);
	}
	
	public static void addPlayer(String gameId, String userName, Session sess, boolean isBlack) {
		if (gameId == null || gameId.isEmpty() || userName == null  || userName.isEmpty() || sess == null)
			return;
		List<ColoredSubscriber> recent = connections.get(gameId);
		ColoredSubscriber newSub = new ColoredSubscriber(userName, sess, isBlack);
		if (recent == null || recent.isEmpty())
			putConnection(gameId, new ArrayList<ColoredSubscriber>(Arrays.asList(newSub)));
		else if (!isContain(newSub, recent))
			recent.add(newSub);
	}
	
	private static boolean isContain(ColoredSubscriber suspect, List<ColoredSubscriber> lst) {
		if (suspect == null || suspect.playerName == null || suspect.playerName.isEmpty() || suspect.socket == null)
			return false;
		for (ColoredSubscriber s : lst) {
			if (suspect.playerName.equals(s.playerName))
				return true;
		}
		return false;
	}
	
	public static void removeConnection(String gameId, String userName) {
		if (gameId == null || gameId.isEmpty()  || userName == null || userName.isEmpty())
			return;
		
		List<ColoredSubscriber> players = connections.get(gameId);
		if (players == null || players.isEmpty())
			return;
		for (ColoredSubscriber c : players) {
			if (userName.equals(c.playerName)) {
				players.remove(c);
				break;
			}
		}
		if (players.isEmpty())
			connections.remove(gameId);
	}
	
	
	public static void releaseConnections() {
		for(Entry<String, List<ColoredSubscriber>> connection : connections.entrySet()) {
			for (ColoredSubscriber c : connection.getValue()) {
				if (c.socket.isOpen()) {
					c.socket.close();
				}
			}
		}
	}
}
