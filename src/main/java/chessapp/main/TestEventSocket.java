package chessapp.main;

import java.io.IOException;
import java.net.HttpCookie;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import chessapp.game.ChesspressoUtility;

public class TestEventSocket extends WebSocketAdapter {
	
	@Override
	public void onWebSocketConnect(Session sess) {
		super.onWebSocketConnect(sess);
		
		// Így kell kiszedni a sessionId-t
		String sessionId = null;
		for(HttpCookie cookie: sess.getUpgradeRequest().getCookies()) {
			if(cookie.getName().equals("JSESSIONID")) {
				// Hozzá van fűzve .-al elválasztva valami, ezért a pont előtti rész kell csak nekünk
				sessionId = cookie.getValue().split("\\.")[0];
			}
		}
		
		System.out.println("Socket Connected: " + sess);
	}

	@Override
	public void onWebSocketText(String message) {
		super.onWebSocketText(message);
		System.out.println("Received TEXT message: " + message);
		
		ChesspressoUtility chessUtility = new ChesspressoUtility();
		String serverMove = chessUtility.generateRandomMove(message);
		
		System.out.println(serverMove);
		
		try {
			getRemote().sendString(serverMove);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onWebSocketBinary(byte[] payload, int offset, int len) {
		super.onWebSocketBinary(payload, offset, len);
		System.out.println("Received binary message");
	}
	
	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);
		System.out.println("Socket Closed: [" + statusCode + "] " + reason);
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		super.onWebSocketError(cause);
		cause.printStackTrace(System.err);
	}

}
