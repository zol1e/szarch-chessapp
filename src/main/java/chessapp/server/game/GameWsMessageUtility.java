package chessapp.server.game;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import chessapp.client.main.WebSocketHandler;

public class GameWsMessageUtility {

	/**
	 * Create a JsonObject from game status which can be sent to the client as a websocket message.
	 */
	public static JsonObject buildGameStatusMessage(GameStatus gameStatus, String userName) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add(WebSocketHandler.WS_PROPERTY_TYPE, WebSocketHandler.WS_TYPE_GAME_STATUS);
		builder.add(WebSocketHandler.WS_GAME_STATUS_FEN, gameStatus.getFen());
		builder.add(WebSocketHandler.WS_GAME_STATUS_WHITE_TIME, gameStatus.getWhiteTime());
		builder.add(WebSocketHandler.WS_GAME_STATUS_BLACK_TIME, gameStatus.getBlackTime());
		builder.add(WebSocketHandler.WS_GAME_STATUS_WHITE_USER, gameStatus.getWhiteUserName());
		builder.add(WebSocketHandler.WS_GAME_STATUS_BLACK_USER, gameStatus.getBlackUserName());
		builder.add(WebSocketHandler.WS_USERNAME, userName);
		return builder.build();
	}
	
}