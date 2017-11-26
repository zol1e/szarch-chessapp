package chessapp.server.game;

import static chessapp.server.service.WebSocketMessageService.WS_EMPTY;
import static chessapp.server.service.WebSocketMessageService.WS_GAME_STATUS_BLACK_TIME;
import static chessapp.server.service.WebSocketMessageService.WS_GAME_STATUS_BLACK_USER;
import static chessapp.server.service.WebSocketMessageService.WS_GAME_STATUS_FEN;
import static chessapp.server.service.WebSocketMessageService.WS_GAME_STATUS_RESULT;
import static chessapp.server.service.WebSocketMessageService.WS_GAME_STATUS_WHITE_TIME;
import static chessapp.server.service.WebSocketMessageService.WS_GAME_STATUS_WHITE_USER;
import static chessapp.server.service.WebSocketMessageService.WS_MOVE_BLACK_DRAW_OFFER;
import static chessapp.server.service.WebSocketMessageService.WS_MOVE_WHITE_DRAW_OFFER;
import static chessapp.server.service.WebSocketMessageService.WS_PROPERTY_TYPE;
import static chessapp.server.service.WebSocketMessageService.WS_TYPE_GAME_STATUS;
import static chessapp.server.service.WebSocketMessageService.WS_USERNAME;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class GameWsMessageUtility {

	/**
	 * Create a JsonObject from game status which can be sent to the client as a websocket message.
	 */
	public static JsonObject buildGameStatusMessage(GameStatus gameStatus, String userName) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add(WS_PROPERTY_TYPE, WS_TYPE_GAME_STATUS);
		builder.add(WS_GAME_STATUS_FEN, gameStatus.getFen());
		builder.add(WS_GAME_STATUS_WHITE_TIME, gameStatus.getWhiteTime());
		builder.add(WS_GAME_STATUS_BLACK_TIME, gameStatus.getBlackTime());
		builder.add(WS_GAME_STATUS_WHITE_USER, gameStatus.getWhiteUserName());
		builder.add(WS_GAME_STATUS_BLACK_USER, gameStatus.getBlackUserName());
		builder.add(WS_USERNAME, userName);
		builder.add(WS_MOVE_WHITE_DRAW_OFFER, gameStatus.isWhiteDrawOffer());
		builder.add(WS_MOVE_BLACK_DRAW_OFFER, gameStatus.isBlackDrawOffer());
		
		if(gameStatus.getGameResult() == null) {
			builder.add(WS_GAME_STATUS_RESULT, WS_EMPTY);
		} else {
			builder.add(WS_GAME_STATUS_RESULT, gameStatus.getGameResult().getStringValue());
		}
		
		return builder.build();
	}
	
}