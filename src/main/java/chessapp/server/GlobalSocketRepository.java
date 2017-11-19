package chessapp.server;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.eclipse.jetty.websocket.api.Session;

public class GlobalSocketRepository {

	public static ConcurrentMap<String, Session> connections;
	
	public static void initConnections() {
		connections = new ConcurrentHashMap<String, Session>();
	}
	
	public static void putConnection(String sessionId, Session wsSession) {
		connections.put(sessionId, wsSession);
	}
	
	public static void removeConnection(String sessionId, Session wsSession) {
		connections.remove(sessionId, wsSession);
	}
	
	public static void releaseConnections() {
		for(Entry<String, Session> connection : connections.entrySet()) {
			connection.getValue().close();
		}
	}
}