package chessapp.client.main;

import java.io.StringReader;
import java.net.HttpCookie;
import java.util.Date;
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
import chessapp.server.game.ChessGameUtility;
import chessapp.server.game.ChesspressoUtility;
import chessapp.server.game.ColoredSubscriber;
import chessapp.server.game.GameStatus;
import chessapp.server.game.GameWsMessageUtility;
import chessapp.server.game.Subscriber;
import chessapp.server.model.ChessGameBean;
import chessapp.server.model.GlobalChatMessageBean;
import chessapp.server.model.LoginBean;
import chessapp.server.model.PrivateChatMessageBean;
import chessapp.shared.entities.ChessGame;
import chessapp.shared.entities.GlobalChatMessage;
import chessapp.shared.entities.PrivateChatMessage;
import chessapp.shared.entities.UserLogin;
import chesspresso.Chess;

public class WebSocketHandler extends WebSocketAdapter {

	public static final String WEBSOCKET_COOKIE_SESSIONID_KEY = "JSESSIONID";

	/** Konstansok a chessapp.js-el szinkronban tartva */

	public static final String WS_USERNAME = "username";
	
	// Játékhoz kapcsolódó vezérlő üzenet konstansok
	public static final String WS_TYPE_GAME_CONNECT = "connect_game";
	public static final String WS_TYPE_GAME_DISCONNECT = "disconnect_game";
	public static final String WS_TYPE_GAME_STATUS = "game_status";
	public static final String WS_TYPE_GAME_MOVE = "move";
	
	// Status message property constants
	public static final String WS_GAME_STATUS_WHITE_TIME = "whitetime";
	public static final String WS_GAME_STATUS_BLACK_TIME = "blacktime";
	public static final String WS_GAME_STATUS_FEN = "fen";
	public static final String WS_GAME_STATUS_PGN = "pgn";
	public static final String WS_GAME_STATUS_WHITE_USER = "whiteuser";
	public static final String WS_GAME_STATUS_BLACK_USER = "blackuser";
	
	// Move message property constants
	public static final String WS_MOVE_COLOR = "color";
	public static final String WS_MOVE_FLAGS = "flags";
	public static final String WS_MOVE_FROM = "from";
	public static final String WS_MOVE_TO = "to";
	public static final String WS_MOVE_PROMOTION = "promotion";
	
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
		GLOBAL, PRIVATE, GAME
	}

	/** ------------------------------------------------------------- */

	// Http session-ben használt id mező, ami itt is fontos websocket azonosításához
	private String httpSessionId = null;

	private LoginBean loginBean = new LoginBean();
	private PrivateChatMessageBean privateChatMsgBean = new PrivateChatMessageBean();
	private GlobalChatMessageBean globalChatMsgBean = new GlobalChatMessageBean();

	@Override
	public void onWebSocketConnect(Session sess) {
		super.onWebSocketConnect(sess);

		// Így kell kiszedni a sessionId-t
		for (HttpCookie cookie : sess.getUpgradeRequest().getCookies()) {
			if (cookie.getName().equals(WebSocketHandler.WEBSOCKET_COOKIE_SESSIONID_KEY)) {
				// Hozzá van fűzve .-al elválasztva valami, ezért a pont előtti rész kell csak
				// nekünk
				httpSessionId = cookie.getValue().split("\\.")[0];
				break;
			}
		}

		if (httpSessionId != null) {
			UserLogin userLogin = loginBean.findBySessionId(httpSessionId);
			if (userLogin == null) {
				sess.close();
				return;
			}
		} else {
			// No session id, cannot handle the connection, it should be closed.
			sess.close();
			System.out.println("WebSocket closed, becuse no session id sent");
			return;
		}
		
		System.out.println("Socket Connected: " + sess);
	}

	@Override
	public void onWebSocketText(String messageString) {
		super.onWebSocketText(messageString);
		System.out.println("Received TEXT message: " + messageString);

		JsonReader reader = Json.createReader(new StringReader(messageString));
		JsonObject message = reader.readObject();
		reader.close();

		String type = message.getString(WS_PROPERTY_TYPE);
		if (type == null || type.isEmpty()) {
			return;
		}
		UserLogin userLogin = loginBean.findBySessionId(httpSessionId);
		if (userLogin == null)
			return;
		// --- Globális üzenetek kezelése
		if (message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GLOBAL_CONNECT)) {
			System.out.println("WS-Type: " + WS_TYPE_GLOBAL_CONNECT);

			GlobalSocketRepository.putConnection(httpSessionId, getSession());
		}
		if (message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GLOBAL_DISCONNECT)) {
			System.out.println("WS-Type: " + WS_TYPE_GLOBAL_DISCONNECT);

			GlobalSocketRepository.removeConnection(httpSessionId, getSession());
		}
		if (message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GLOBAL_MESSAGE)) {
			System.out.println("WS-Type: " + WS_TYPE_GLOBAL_MESSAGE);
			String content = message.getString("content");

			globalChatMsgBean.create(new GlobalChatMessage(userLogin.getUserName(), content));

			for (Entry<String, Session> entry : GlobalSocketRepository.connections.entrySet()) {

				Session sess = entry.getValue();
				if (sess.isOpen())
					sendMessage(sess, MessageType.GLOBAL, userLogin.getUserName() + ": " + content, null);
				else
					GlobalSocketRepository.removeConnection(entry.getKey(), sess);
			}
		}
		
		ChessGameBean chessGameBean = new ChessGameBean();
		ChessGame chessGame = chessGameBean.getOngoingBySomePlayer(userLogin.getUserName());
		if (chessGame == null)
			return;

		// --- Privát üzenetek kezelése
		if (message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_PRIVATE_CONNECT)) {
			System.out.println("WS-Type: " + WS_TYPE_PRIVATE_CONNECT);
			PrivateSocketRepository.addPlayer(chessGame.getChessGameId(), userLogin.getUserName(), getSession());
		}
		if (message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_PRIVATE_DISCONNECT)) {
			System.out.println("WS-Type: " + WS_TYPE_PRIVATE_DISCONNECT);
			if (chessGame != null)
				PrivateSocketRepository.removeConnection(chessGame.getChessGameId(), userLogin.getUserName());
		}
		if (message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_PRIVATE_MESSAGE)) {
			System.out.println("WS-Type: " + WS_TYPE_PRIVATE_MESSAGE);
			String content = message.getString(WS_PROPERTY_CONTENT);

			privateChatMsgBean.create(new PrivateChatMessage(userLogin.getUserName(), content, chessGame.getChessGameId()));

			List<Subscriber> subscribers = PrivateSocketRepository.connections.get(chessGame.getChessGameId());
			if (subscribers == null || subscribers.isEmpty())
				return;
			subscribers.forEach(x -> {
				if (x.socket.isOpen())
					sendMessage(x.socket, MessageType.PRIVATE, userLogin.getUserName() + ": " + content, null);
			});
		}

		// --- Játék kezelése
		if (message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GAME_CONNECT)) {
			System.out.println("WS-Type: " + WS_TYPE_GAME_CONNECT);
			boolean amIBlack = userLogin.getUserName().equals(chessGame.getBlackPlayer());
			Session session = getSession();
			
			if (chessGame != null) {
				Date dateNow = new Date();
				int onMove = ChesspressoUtility.onMove(chessGame.getFen());

				// TODO: ez egy paraszt kódmásolás a websocket handlerből, meg kell csinálni
				// egységesre
				Long lastMoveTime = chessGame.getLastMoveTime().getTime();
				Long nowMillis = dateNow.getTime();
				Long difference = nowMillis - lastMoveTime;
				
				if(Chess.WHITE == onMove ) {
					Long whiteTimeLeftNow = chessGame.getWhiteTimeLeft() - difference;
					if(whiteTimeLeftNow <= 0) {
						chessGame.setWhiteTimeLeft(new Long(0));
						chessGame.setEndDate(dateNow);
						chessGame.setResult("white lost on time");
					} else {
						chessGame.setWhiteTimeLeft(whiteTimeLeftNow);
					}
				}
				if(Chess.BLACK == onMove ) {
					Long blackTimeLeftNow = chessGame.getBlackTimeLeft() - difference;
					if(blackTimeLeftNow <= 0) {
						chessGame.setBlackTimeLeft(new Long(0));
						chessGame.setEndDate(dateNow);
						chessGame.setResult("black lost on time");
					} else {
						chessGame.setBlackTimeLeft(blackTimeLeftNow);
					}
				}
			}
			
			GameSocketRepository.addPlayer(chessGame.getChessGameId(), userLogin.getUserName(), session, amIBlack);
			if(session.isOpen()) {
				sendMessage(session, MessageType.GAME, "game status message ", ChessGameBean.getGameStatus(chessGame));
			}
		}
		if (message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GAME_DISCONNECT)) {
			System.out.println("WS-Type: " + WS_TYPE_GAME_DISCONNECT);
			if (chessGame != null)
				GameSocketRepository.removeConnection(chessGame.getChessGameId(), userLogin.getUserName());
		}
		if (message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GAME_MOVE)) {
			System.out.println("WS-Type: " + WS_TYPE_GAME_MOVE);
			
			String from;
			String to;
			String color;
			String flags;
			String promotion;
			try {
				from = message.getString(WS_MOVE_FROM);
				to = message.getString(WS_MOVE_TO);
				color = message.getString(WS_MOVE_COLOR);
				flags = message.getString(WS_MOVE_FLAGS);
				promotion = message.getString(WS_MOVE_PROMOTION);
			} catch (Exception e) {
				return;
			}
			
			String oldPosition = chessGame.getFen();
			int onMove = ChesspressoUtility.onMove(oldPosition);
			
			String newPosition = ChesspressoUtility.makeMove(oldPosition, from, to, flags, color, promotion);
			if(newPosition != null) {
				Date dateNow = new Date();
				chessGame.setFen(newPosition);
				String moves = ChessGameUtility.addMoveToDBMoves(chessGame.getMoves(), from, to, flags, color, promotion);
				chessGame.setMoves(moves);
				
				String resultString = ChesspressoUtility.getGameResult(newPosition);
				
				if(resultString != null) {
					chessGame.setEndDate(dateNow);
					chessGame.setResult(resultString);
				}
				
				Long lastMoveTime = chessGame.getLastMoveTime().getTime();
				Long nowMillis = dateNow.getTime();
				Long difference = nowMillis - lastMoveTime;
				if(Chess.WHITE == onMove ) {
					Long whiteTimeLeftNow = chessGame.getWhiteTimeLeft() - difference;
					if(whiteTimeLeftNow <= 0) {
						chessGame.setWhiteTimeLeft(new Long(0));
						chessGame.setEndDate(dateNow);
						chessGame.setResult("white lost on time");
					} else {
						chessGame.setWhiteTimeLeft(whiteTimeLeftNow);
					}
				}
				if(Chess.BLACK == onMove ) {
					Long blackTimeLeftNow = chessGame.getBlackTimeLeft() - difference;
					if(blackTimeLeftNow <= 0) {
						chessGame.setBlackTimeLeft(new Long(0));
						chessGame.setEndDate(dateNow);
						chessGame.setResult("black lost on time");
					} else {
						chessGame.setBlackTimeLeft(blackTimeLeftNow);
					}
				}
				chessGame.setLastMoveTime(dateNow);
				
				chessGameBean.update(chessGame);
			}
			GameStatus gameStatus = ChessGameBean.getGameStatus(chessGame);
			
			List<ColoredSubscriber> subscribers = GameSocketRepository.connections.get(chessGame.getChessGameId());
			if (subscribers == null || subscribers.isEmpty())
				return;

			subscribers.forEach(x -> {
				if (x.socket.isOpen())
					sendMessage(x.socket, MessageType.GAME, "game status message ", gameStatus);
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
		ChessGameBean chessGameBean = new ChessGameBean();
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
	 * Helyes WebSocket üzenet küldéséhez, csak ezt tudja értelmezni a kliensünk,
	 * kötelező használni!
	 * 
	 * @param session
	 * @param messageType
	 */
	public static void sendMessage(Session session, MessageType messageType, String content, GameStatus gameStatus) {
		if (messageType.equals(MessageType.GLOBAL)) {
			JsonObjectBuilder builder = Json.createObjectBuilder();
			builder.add(WS_PROPERTY_TYPE, WS_TYPE_GLOBAL_MESSAGE);
			builder.add(WS_PROPERTY_CONTENT, content);
			JsonObject message = builder.build();
			session.getRemote().sendString(message.toString(), null);
			return;
		}
		if (messageType.equals(MessageType.PRIVATE)) {
			JsonObjectBuilder builder = Json.createObjectBuilder();
			builder.add(WS_PROPERTY_TYPE, WS_TYPE_PRIVATE_MESSAGE);
			builder.add(WS_PROPERTY_CONTENT, content);
			JsonObject message = builder.build();			
			session.getRemote().sendString(message.toString(), null);
			return;
		}
		if (messageType.equals(MessageType.GAME)) {
			String ongoingChessGameId = gameStatus.getGameId();
			List<ColoredSubscriber> subscribers = GameSocketRepository.connections.get(ongoingChessGameId);

			String userName = null;
			for(ColoredSubscriber subscriber : subscribers) {
				if(subscriber.socket.equals(session)) {
					userName = subscriber.playerName;
					break;
				}
			}
			
			if(userName == null) {
				System.out.println("Username error: cannot send game status");
				return;
			}
			
			JsonObject message = GameWsMessageUtility.buildGameStatusMessage(gameStatus, userName);
			session.getRemote().sendString(message.toString(), null);
			return;
		}
	}
}
