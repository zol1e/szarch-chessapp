package chessapp.client.main;

import java.io.StringReader;
import java.net.HttpCookie;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import chessapp.server.GlobalSocketRepository;
import chessapp.server.model.ChessGameBean;
import chessapp.server.model.GlobalChatMessageBean;
import chessapp.server.model.LoginBean;
import chessapp.server.model.PrivateChatMessageBean;
import chessapp.shared.entities.GlobalChatMessage;
import chessapp.shared.entities.PrivateChatMessage;
import chessapp.shared.entities.UserLogin;

public class WebSocketHandler extends WebSocketAdapter {
	
	public static final String WEBSOCKET_COOKIE_SESSIONID_KEY = "JSESSIONID";
	
	/** Konstansok a chessapp.js-el szinkronban tartva */
	
	// Játékhoz kapcsolódó vezérlő üzenet konstansok
	public static final String WS_TYPE_GAME_CONNECT = "connect_game";
	public static final String WS_TYPE_GAME_DISCONNECT = "disconnect_game";
	public static final String WS_TYPE_GAME_MOVE = "move";

	// Globális chat vezérlő üzenet konstansok
	public static final String WS_TYPE_GLOBAL_CONNECT = "connect_global";
	public static final String WS_TYPE_GLOBAL_DISCONNECT = "disconnect_global";
	public static final String WS_TYPE_GLOBAL_MESSAGE = "global_message";

	// Privát chat vezérlő üzenet konstansok
	public static final String WS_TYPE_PRIVATE_CONNECT = "connect_private";
	public static final String WS_TYPE_PRIVATE_DISCONNECT = "disconnect_private";
	public static final String WS_TYPE_PRIVATE_MESSAGE = "message_private";
	
	// Üzenet paraméterek
	public static final String WS_PROPERTY_TYPE = "type";
	public static final String WS_PROPERTY_CONTENT = "content";
	
	public static enum MessageType {
		GLOBAL,
		PRIVATE,
		GAME
	}
	
	/** ------------------------------------------------------------- */
	
	// Http session-ben használt id mező, ami itt is fontos websocket azonosításához
	private String httpSessionId = null;
	
	@Override
	public void onWebSocketConnect(Session sess) {
		super.onWebSocketConnect(sess);
		
		// Így kell kiszedni a sessionId-t
		for(HttpCookie cookie: sess.getUpgradeRequest().getCookies()) {
			if(cookie.getName().equals(WebSocketHandler.WEBSOCKET_COOKIE_SESSIONID_KEY)) {
				// Hozzá van fűzve .-al elválasztva valami, ezért a pont előtti rész kell csak nekünk
				httpSessionId = cookie.getValue().split("\\.")[0];
				break;
			}
		}
		
		if(httpSessionId != null) {
			LoginBean loginBean = new LoginBean();
			UserLogin userLogin = loginBean.findBySessionId(httpSessionId);
			if(userLogin == null) {
				sess.close();
				return;
			}
			//GlobalSocketRepository.putConnection(httpSessionId, sess);
		}
		
		System.out.println("Socket Connected: " + sess);
	}

	@Override
	public void onWebSocketText(String messageString) {
		super.onWebSocketText(messageString);
		System.out.println("Received TEXT message: " + messageString);
		
		//ChesspressoUtility chessUtility = new ChesspressoUtility();
		//String serverMove = chessUtility.generateRandomMove(message);
		
		JsonReader reader = Json.createReader(new StringReader(messageString));
		JsonObject message = reader.readObject();
		reader.close();
		
		String type = message.getString(WS_PROPERTY_TYPE);
		if(type == null || type.isEmpty()) {
			return;
		}
		
		// --- Globális üzenetek kezelése
		if(message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GLOBAL_CONNECT)) {
			// TODO: add to listening on global chat
			System.out.println("WS-Type: " + WS_TYPE_GLOBAL_CONNECT);
			
			GlobalSocketRepository.putConnection(httpSessionId, getSession());
		}
		if(message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GLOBAL_DISCONNECT)) {
			// TODO: add to listening on global chat
			System.out.println("WS-Type: " + WS_TYPE_GLOBAL_DISCONNECT);
		}
		if(message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GLOBAL_MESSAGE)) {
			// TODO: send global chat message
			System.out.println("WS-Type: " + WS_TYPE_GLOBAL_MESSAGE);
			String content = message.getString("content");
			
			LoginBean lb = new LoginBean();
			UserLogin ul = lb.findBySessionId(httpSessionId);
			GlobalChatMessageBean gcmb = new GlobalChatMessageBean();
			gcmb.create(new GlobalChatMessage(ul.getUserName(), content));
			
			GlobalSocketRepository.connections
				.forEach((httpid,sess) 
						-> sendMessage(sess, MessageType.GLOBAL, "Received global message:  " + 
								ul.getUserName() + 
								": " + 
								content));
			/*for(Entry<String, Session> entry : GlobalSocketRepository.connections.entrySet()) {
			    Session sess = entry.getValue();
				sendMessage(sess, MessageType.GLOBAL, "Received global message:  " + ul.getUserName() + ": " + content);
			}*/
			
		}
		
		// --- Privát üzenetek kezelése
		if(message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_PRIVATE_CONNECT)) {
			// TODO: add to listening on global chat
			System.out.println("WS-Type: " + WS_TYPE_PRIVATE_CONNECT);
		}
		if(message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_PRIVATE_DISCONNECT)) {
			// TODO: add to listening on global chat
			System.out.println("WS-Type: " + WS_TYPE_PRIVATE_DISCONNECT);
		}
		if(message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_PRIVATE_MESSAGE)) {
			// TODO: send private chat message
			System.out.println("WS-Type: " + WS_TYPE_PRIVATE_MESSAGE);
			String content = message.getString("content");
			
			LoginBean loginBean = new LoginBean();
			UserLogin userLogin = loginBean.findBySessionId(httpSessionId);
			PrivateChatMessageBean pcmb = new PrivateChatMessageBean();
			ChessGameBean cgb = new ChessGameBean();
			
			pcmb.create(new PrivateChatMessage(userLogin.getUserName(), content,
				cgb.getCurrentBySomePlayer(userLogin.getUserName()).getChessGameId()));
			
			sendMessage(getSession(), MessageType.PRIVATE, "Received private message:  " + content);
		}
		
		// --- Játék kezelése
		if(message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GAME_CONNECT)) {
			// TODO: add to listening on global chat
			System.out.println("WS-Type: " + WS_TYPE_GAME_CONNECT);
		}
		if(message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GAME_DISCONNECT)) {
			// TODO: add to listening on global chat
			System.out.println("WS-Type: " + WS_TYPE_GAME_DISCONNECT);
		}
		if(message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GAME_MOVE)) {
			// TODO: send global chat message
			System.out.println("WS-Type: " + WS_TYPE_GAME_MOVE);
			
			String content = message.getString("content");
			sendMessage(getSession(), MessageType.GAME, "Received move message:  " + content);
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
		
		// TODO: le kell iratkozni az összes csatornáról (global, private, game)
		
		GlobalSocketRepository.removeConnection(httpSessionId, getSession());
		
		System.out.println("Socket Closed: [" + statusCode + "] " + reason);
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		super.onWebSocketError(cause);
		
		GlobalSocketRepository.removeConnection(httpSessionId, getSession());
		
		cause.printStackTrace(System.err);
	}
	
	/**
	 * Helyes WebSocket üzenet küldéséhez, csak ezt tudja értelmezni a kliensünk, kötelező használni!
	 * 
	 * @param session
	 * @param messageType
	 */
	public static void sendMessage(Session session, MessageType messageType, String content) {
		if(messageType.equals(MessageType.GLOBAL)) {
			JsonObjectBuilder builder = Json.createObjectBuilder();
			builder.add(WS_PROPERTY_TYPE, WS_TYPE_GLOBAL_MESSAGE);
			builder.add(WS_PROPERTY_CONTENT, content);
			JsonObject message = builder.build();

			// TODO: writecallback-et írni a második paraméterbe
			session.getRemote().sendString(message.toString(), null);
			return;
		}
		if(messageType.equals(MessageType.PRIVATE)) {
			JsonObjectBuilder builder = Json.createObjectBuilder();
			builder.add(WS_PROPERTY_TYPE, WS_TYPE_PRIVATE_MESSAGE);
			builder.add(WS_PROPERTY_CONTENT, content);
			JsonObject message = builder.build();

			// TODO: writecallback-et írni a második paraméterbe
			session.getRemote().sendString(message.toString(), null);
			return;
		}
		if(messageType.equals(MessageType.GAME)) {
			JsonObjectBuilder builder = Json.createObjectBuilder();
			builder.add(WS_PROPERTY_TYPE, WS_TYPE_GAME_MOVE);
			builder.add(WS_PROPERTY_CONTENT, content);
			JsonObject message = builder.build();

			// TODO: writecallback-et írni a második paraméterbe
			session.getRemote().sendString(message.toString(), null);
			return;
		}
	}
}
