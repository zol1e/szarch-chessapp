package chessapp.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.eclipse.jetty.websocket.api.Session;

import chessapp.server.game.Subscriber;

public class PrivateSocketRepository {
	public static ConcurrentMap<String, List<Subscriber>> connections;
	
	public static void initConnections() {
		connections = new ConcurrentHashMap<String, List<Subscriber>>();
	}
	
	public static void putConnection(String gameId, List<Subscriber> players) {
		connections.put(gameId, players);
	}
	
	public static void addPlayer(String gameId, String name, Session sess) {
		if (gameId == null || name == null || sess == null || gameId.isEmpty() || name.isEmpty())
			return;
		List<Subscriber> players = connections.get(gameId);
		Subscriber newSub = new Subscriber(name, sess);
		if (players == null || players.isEmpty())
			putConnection(gameId, new ArrayList<Subscriber>(Arrays.asList(newSub)));
		else if (!isContain(newSub, players))
			players.add(new Subscriber(name, sess));
	}
	
	private static boolean isContain(Subscriber suspect, List<Subscriber> lst) {
		if (suspect == null || suspect.playerName == null || suspect.playerName.isEmpty() || suspect.socket == null)
			return false;
		for (Subscriber s : lst) {
			if (suspect.playerName.equals(s.playerName))
				return true;
		}
		return false;
	}
	
	public static void removeConnection(String gameId, String userName) {
		if (gameId == null || gameId.isEmpty()  || userName == null || userName.isEmpty())
			return;
		
		List<Subscriber> players = connections.get(gameId);
		if (players == null || players.isEmpty())
			return;
		for (Subscriber p : players) {
			if (p.playerName.equals(userName)) {
				players.remove(p);
				break;
			}
		}
		if (connections.get(gameId) == null || connections.get(gameId).isEmpty())
			connections.remove(gameId);
	}
	
	public static void releaseConnections() {
		for(Entry<String, List<Subscriber>> connection : connections.entrySet()) {
			connection.getValue().forEach(x -> {
		        if (x.socket.isOpen())
		        	x.socket.close();
			});
		}
	}
}
