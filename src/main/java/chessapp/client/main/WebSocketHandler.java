package chessapp.client.main;

import static chessapp.server.service.WebSocketMessageService.WS_PROPERTY_CONTENT;
import static chessapp.server.service.WebSocketMessageService.WS_PROPERTY_TYPE;
import static chessapp.server.service.WebSocketMessageService.WS_TYPE_GAME_CONNECT;
import static chessapp.server.service.WebSocketMessageService.WS_TYPE_GAME_DISCONNECT;
import static chessapp.server.service.WebSocketMessageService.WS_TYPE_GAME_MOVE;
import static chessapp.server.service.WebSocketMessageService.WS_TYPE_GLOBAL_CONNECT;
import static chessapp.server.service.WebSocketMessageService.WS_TYPE_GLOBAL_DISCONNECT;
import static chessapp.server.service.WebSocketMessageService.WS_TYPE_GLOBAL_MESSAGE;
import static chessapp.server.service.WebSocketMessageService.WS_TYPE_PRIVATE_CONNECT;
import static chessapp.server.service.WebSocketMessageService.WS_TYPE_PRIVATE_DISCONNECT;
import static chessapp.server.service.WebSocketMessageService.WS_TYPE_PRIVATE_MESSAGE;

import java.io.StringReader;
import java.net.HttpCookie;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import chessapp.server.GameSocketRepository;
import chessapp.server.GlobalSocketRepository;
import chessapp.server.PrivateSocketRepository;
import chessapp.server.model.ChessGameBean;
import chessapp.server.model.LoginBean;
import chessapp.server.service.ChessGameService;
import chessapp.server.service.GlobalMessageService;
import chessapp.server.service.PrivateMessageService;
import chessapp.shared.entities.ChessGame;
import chessapp.shared.entities.UserLogin;

public class WebSocketHandler extends WebSocketAdapter {

	public static final String WEBSOCKET_COOKIE_SESSIONID_KEY = "JSESSIONID";

	// Http session id field, important for authenticating the websocket
	private String httpSessionId = null;

	@Override
	public void onWebSocketConnect(Session sess) {
		super.onWebSocketConnect(sess);

		// Tricky way to get the http session id from cookies
		for (HttpCookie cookie : sess.getUpgradeRequest().getCookies()) {
			if (cookie.getName().equals(WebSocketHandler.WEBSOCKET_COOKIE_SESSIONID_KEY)) {
				// We need only the session id without the part after the point
				httpSessionId = cookie.getValue().split("\\.")[0];
				break;
			}
		}

		if (httpSessionId != null) {
			LoginBean loginBean = new LoginBean();
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

		Date dateNow = new Date();

		JsonReader reader = Json.createReader(new StringReader(messageString));
		JsonObject message = reader.readObject();
		reader.close();

		String type = message.getString(WS_PROPERTY_TYPE);
		if (type == null || type.isEmpty()) {
			return;
		}

		LoginBean loginBean = new LoginBean();
		UserLogin userLogin = loginBean.findBySessionId(httpSessionId);
		if (userLogin == null) {
			return;
		}

		// --- Globális üzenetek kezelése
		if (message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GLOBAL_CONNECT)) {
			System.out.println("WS-Type: " + WS_TYPE_GLOBAL_CONNECT);
			GlobalMessageService.connectGlobalChat(httpSessionId, getSession());
		} else if (message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GLOBAL_DISCONNECT)) {
			System.out.println("WS-Type: " + WS_TYPE_GLOBAL_DISCONNECT);
			GlobalMessageService.disconnectGlobalChat(httpSessionId, getSession());
		} else if (message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GLOBAL_MESSAGE)) {
			System.out.println("WS-Type: " + WS_TYPE_GLOBAL_MESSAGE);
			GlobalMessageService globalMessageService = new GlobalMessageService();
			String content = message.getString("content");
			globalMessageService.sendGlobalMessage(userLogin, content);
		} else {
			ChessGameService chessGameService = new ChessGameService();			
			ChessGameBean chessGameBean = new ChessGameBean();
			ChessGame chessGame = chessGameBean.getOngoingBySomePlayer(userLogin.getUserName());
			if (chessGame == null) {
				return;
			}

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

				PrivateMessageService privateMessageService = new PrivateMessageService();
				privateMessageService.sendPrivateMessage(userLogin, chessGame, content);
			}

			// --- Játék kezelése
			if (message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GAME_CONNECT)) {
				System.out.println("WS-Type: " + WS_TYPE_GAME_CONNECT);
				chessGameService.connectGameSocket(getSession(), dateNow, userLogin, chessGame);
			}

			if (message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GAME_DISCONNECT)) {
				System.out.println("WS-Type: " + WS_TYPE_GAME_DISCONNECT);
				chessGameService.disconnectGameSocket(getSession(), dateNow, userLogin, chessGame);
			}
			if (message.getString(WS_PROPERTY_TYPE).equals(WS_TYPE_GAME_MOVE)) {
				System.out.println("WS-Type: " + WS_TYPE_GAME_MOVE);
				chessGameService.sendMove(dateNow, message, userLogin, chessGame);
			}
		}
	}

	@Override
	public void onWebSocketBinary(byte[] payload, int offset, int len) {
		super.onWebSocketBinary(payload, offset, len);
		System.out.println("Received binary message - not implemented");
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);

		LoginBean loginBean = new LoginBean();
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
}
