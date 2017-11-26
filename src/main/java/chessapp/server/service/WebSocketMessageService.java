package chessapp.server.service;

import org.eclipse.jetty.websocket.api.Session;
import chessapp.server.game.GameStatus;

public class WebSocketMessageService {
	
	/** Constants in sync with chessapp.js constants */

	public static final String WS_USERNAME = "username";
	public static final String WS_TRUE = "true";
	public static final String WS_FALSE = "false";
	public static final String WS_EMPTY = "empty";

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
	public static final String WS_GAME_STATUS_RESULT = "result";

	// Move message property constants
	public static final String WS_MOVE_COLOR = "color";
	public static final String WS_MOVE_FLAGS = "flags";
	public static final String WS_MOVE_FROM = "from";
	public static final String WS_MOVE_TO = "to";
	public static final String WS_MOVE_PROMOTION = "promotion";
	public static final String WS_MOVE_WHITE_DRAW_OFFER = "whitedraw";
	public static final String WS_MOVE_BLACK_DRAW_OFFER = "blackdraw";
	public static final String WS_MOVE_RESIGN = "resign";

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
	
	/**
	 * Helyes WebSocket üzenet küldéséhez, csak ezt tudja értelmezni a kliensünk,
	 * kötelező használni!
	 * 
	 * @param session
	 * @param messageType
	 */
	public static void sendMessage(Session session, MessageType messageType, String content, GameStatus gameStatus) {
		if (messageType.equals(MessageType.GLOBAL)) {
			GlobalMessageService.sendGlobalMessageOnSocket(session, content);
			return;
		} else if (messageType.equals(MessageType.PRIVATE)) {
			PrivateMessageService.sendPrivateMessageOnSocket(session, content);
			return;
		} else if (messageType.equals(MessageType.GAME)) {
			ChessGameMessageService.sendChessGameMessage(session, gameStatus);
			return;
		}
	}
}
