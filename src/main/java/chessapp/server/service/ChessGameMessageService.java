package chessapp.server.service;

import java.util.List;

import javax.json.JsonObject;

import org.eclipse.jetty.websocket.api.Session;

import chessapp.server.GameSocketRepository;
import chessapp.server.game.ColoredSubscriber;
import chessapp.server.game.GameStatus;
import chessapp.server.game.GameWsMessageUtility;
import chessapp.server.service.WebSocketMessageService.MessageType;
import chessapp.shared.entities.ChessGame;

public class ChessGameMessageService {

	public static void sendChessGameMessage(Session session, GameStatus gameStatus) {
		String ongoingChessGameId = gameStatus.getGameId();
		List<ColoredSubscriber> subscribers = GameSocketRepository.connections.get(ongoingChessGameId);

		String userName = null;
		for (ColoredSubscriber subscriber : subscribers) {
			if (subscriber.socket.equals(session)) {
				userName = subscriber.playerName;
				break;
			}
		}

		if (userName == null) {
			System.out.println("Username error: cannot send game status");
			return;
		}

		JsonObject message = GameWsMessageUtility.buildGameStatusMessage(gameStatus, userName);
		session.getRemote().sendString(message.toString(), null);
		return;
	}
	

	public static void broadcastGameStatus(ChessGame chessGame, GameStatus gameStatus) {
		List<ColoredSubscriber> subscribers = GameSocketRepository.connections.get(chessGame.getChessGameId());
		if (subscribers == null || subscribers.isEmpty())
			return;

		subscribers.forEach(x -> {
			if (x.socket.isOpen())
				WebSocketMessageService.sendMessage(x.socket, MessageType.GAME, "game status message ", gameStatus);
		});
	}
}
