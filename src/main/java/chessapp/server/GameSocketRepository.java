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
	
	/*public static void putConnectionBlack(String gameId, String blackPlayer, Session bsess) {
		if (gameId == null || blackPlayer == null || bsess == null || gameId.isEmpty() || blackPlayer.isEmpty())
			return;
		ColoredSubscriber player = new ColoredSubscriber();
		player.setBlackPlayer(new Subscriber(blackPlayer, bsess));
		connections.put(gameId, player);
	}
	
	public static void putConnectionWhite(String gameId, String whitePlayer, Session wsess) {
		if (gameId == null || whitePlayer == null || wsess == null || gameId.isEmpty() || whitePlayer.isEmpty())
			return;
		ColoredSubscriber player = new ColoredSubscriber();
		player.setWhitePlayer(new Subscriber(whitePlayer, wsess));
		connections.put(gameId, player);
	}*/
	
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
			if (suspect.playerName.equals(s.playerName) /*&& suspect.socket.equals(s.socket) && suspect.isBlack == s.isBlack*/)
				return true;
		}
		return false;
	}
	
	/*public static void putConnection(String gameId, String whitePlayer, Session wsess, String blackPlayer, Session bsess) {
		if (gameId == null || whitePlayer == null || wsess == null || gameId.isEmpty() || whitePlayer.isEmpty()|| blackPlayer == null || blackPlayer.isEmpty())
			return;
		connections.put(gameId, new ColoredSubscriber(blackPlayer, bsess, whitePlayer, wsess));
	}*/
	
	/*public static void addSecondPlayer(String gameId, String name, Session sess) {
		if (gameId == null || name == null || sess == null || gameId.isEmpty() || name.isEmpty())
			return;
		ColoredSubscriber mpg = connections.get(gameId);
		if (mpg == null)
			return;
		mpg.addSecondPlayer(name, sess);
	}*/
	
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
		/*if (userName.equals(mapping.getBlackPlayer() == null ? "" : mapping.getBlackPlayer().playerName)) {
			mapping.setBlackPlayer(null);
		} else if (userName.equals(mapping.getWhitePlayer() == null ? "" : mapping.getWhitePlayer().playerName)) {
			mapping.setWhitePlayer(null);
		}
		if (mapping.isBothNull())
			connections.remove(gameId, mapping);*/
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
