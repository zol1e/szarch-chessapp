package chessapp.client.main;

import java.io.StringReader;
import java.net.HttpCookie;
import java.util.List;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import chessapp.server.GameSocketRepository;
import chessapp.server.GlobalSocketRepository;
import chessapp.server.PrivateSocketRepository;
import chessapp.server.game.ColoredSubscriber;
import chessapp.server.game.Subscriber;
import chessapp.server.model.ChessGameBean;
import chessapp.server.model.GlobalChatMessageBean;
import chessapp.server.model.LoginBean;
import chessapp.server.model.PrivateChatMessageBean;
import chessapp.shared.entities.ChessGame;
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

	private LoginBean loginBean = new LoginBean();
	private PrivateChatMessageBean privateChatMsgBean = new PrivateChatMessageBean();
	private ChessGameBean chessGameBean = new ChessGameBean();
	private GlobalChatMessageBean globalChatMsgBean = new GlobalChatMessageBean();
	
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
			UserLogin userLogin = loginBean.findBySessionId(httpSessionId);
			if(userLogin == null) {
				sess.close();
				return;
			}
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
		UserLogin userLogin = loginBean.findBySessionId(httpSessionId);
		ChessGame ongoing = chessGameBean.getOngoingBySomePlayer(userLogin.getUserName());
		String ongoingId = ongoing == null ? null : ongoing.getChessGameId();
		// --- Globális üzenetek kezelése
		if(message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GLOBAL_CONNECT)) {
			System.out.println("WS-Type: " + WS_TYPE_GLOBAL_CONNECT);
			
			GlobalSocketRepository.putConnection(httpSessionId, getSession());
		}
		if(message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GLOBAL_DISCONNECT)) {
			System.out.println("WS-Type: " + WS_TYPE_GLOBAL_DISCONNECT);
			
			GlobalSocketRepository.removeConnection(httpSessionId, getSession());
		}
		if(message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GLOBAL_MESSAGE)) {
			System.out.println("WS-Type: " + WS_TYPE_GLOBAL_MESSAGE);
			String content = message.getString("content");
			
			globalChatMsgBean.create(new GlobalChatMessage(userLogin.getUserName(), content));
			
			for(Entry<String, Session> entry : GlobalSocketRepository.connections.entrySet()) {
				
			    Session sess = entry.getValue();
			    if (sess.isOpen())
			    	sendMessage(sess, MessageType.GLOBAL, "Received global message:  " + userLogin.getUserName() + ": " + content);
			    else
			    	GlobalSocketRepository.removeConnection(entry.getKey(), sess);
			}
		}
		
		// --- Privát üzenetek kezelése
		if(message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_PRIVATE_CONNECT)) {
			System.out.println("WS-Type: " + WS_TYPE_PRIVATE_CONNECT);
			PrivateSocketRepository.addPlayer(ongoingId, userLogin.getUserName(), getSession());
		}
		if(message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_PRIVATE_DISCONNECT)) {
			System.out.println("WS-Type: " + WS_TYPE_PRIVATE_DISCONNECT);
			if (ongoing != null)
				PrivateSocketRepository.removeConnection(ongoing.getChessGameId(), userLogin.getUserName());
		}
		if(message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_PRIVATE_MESSAGE)) {
			System.out.println("WS-Type: " + WS_TYPE_PRIVATE_MESSAGE);
			String content = message.getString("content");
			
			privateChatMsgBean.create(new PrivateChatMessage(userLogin.getUserName(), content, ongoingId));
			
			List<Subscriber> subscribers = PrivateSocketRepository.connections.get(ongoingId);
			if (subscribers == null || subscribers.isEmpty())
				return;
			subscribers.forEach(x -> {
				if(x.socket.isOpen())
				    sendMessage(x.socket, MessageType.PRIVATE, "Received private message:  " + userLogin.getUserName() + ": " + content);
			});
		}
		
		// --- Játék kezelése
		if(message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GAME_CONNECT)) {
			System.out.println("WS-Type: " + WS_TYPE_GAME_CONNECT);
			boolean amIBlack = userLogin.getUserName().equals(ongoing.getBlackPlayer());
			GameSocketRepository.addPlayer(ongoingId, userLogin.getUserName(), getSession(), amIBlack);
		}
		if(message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GAME_DISCONNECT)) {
			System.out.println("WS-Type: " + WS_TYPE_GAME_DISCONNECT);
			if (ongoing != null)
				GameSocketRepository.removeConnection(ongoing.getChessGameId(), userLogin.getUserName());
		}
		if(message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GAME_MOVE)) {
			System.out.println("WS-Type: " + WS_TYPE_GAME_MOVE);
			String content = message.getString("content");
			
			//TODO persist this
			
			List<ColoredSubscriber> subscribers = GameSocketRepository.connections.get(ongoingId);
			if (subscribers == null || subscribers.isEmpty())
				return;
			subscribers.forEach(x -> {
				if(x.socket.isOpen())
				    sendMessage(x.socket, MessageType.GAME, "Received move message:  " + content);
			});
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
		
		UserLogin userLogin = loginBean.findBySessionId(httpSessionId);
		ChessGame ongoing = chessGameBean.getOngoingBySomePlayer(userLogin.getUserName());
		String ongoingId = ongoing == null ? null : ongoing.getChessGameId();
		
		GlobalSocketRepository.removeConnection(httpSessionId, getSession());
		PrivateSocketRepository.removeConnection(ongoingId, userLogin.getUserName());
		GameSocketRepository.removeConnection(ongoingId, userLogin.getUserName());
		
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
