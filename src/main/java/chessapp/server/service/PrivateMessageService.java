package chessapp.server.service;

import static chessapp.server.service.WebSocketMessageService.WS_PROPERTY_CONTENT;
import static chessapp.server.service.WebSocketMessageService.WS_PROPERTY_TYPE;
import static chessapp.server.service.WebSocketMessageService.WS_TYPE_PRIVATE_MESSAGE;

import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.eclipse.jetty.websocket.api.Session;

import chessapp.server.PrivateSocketRepository;
import chessapp.server.game.Subscriber;
import chessapp.server.model.PrivateChatMessageBean;
import chessapp.server.service.WebSocketMessageService.MessageType;
import chessapp.shared.entities.ChessGame;
import chessapp.shared.entities.PrivateChatMessage;
import chessapp.shared.entities.UserLogin;

public class PrivateMessageService {

	private PrivateChatMessageBean privateChatMsgBean = new PrivateChatMessageBean();
	
	public static void sendPrivateMessageOnSocket(Session session, String content) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add(WS_PROPERTY_TYPE, WS_TYPE_PRIVATE_MESSAGE);
		builder.add(WS_PROPERTY_CONTENT, content);
		JsonObject message = builder.build();
		session.getRemote().sendString(message.toString(), null);
		return;
	}
	

	public void sendPrivateMessage(UserLogin userLogin, ChessGame chessGame, String content) {
		privateChatMsgBean
				.create(new PrivateChatMessage(userLogin.getUserName(), content, chessGame.getChessGameId()));

		PrivateMessageService.broadcastPrivateMessage(userLogin, chessGame, content);
	}
	
	public static void broadcastPrivateMessage(UserLogin userLogin, ChessGame chessGame, String content) {
		List<Subscriber> subscribers = PrivateSocketRepository.connections.get(chessGame.getChessGameId());
		if (subscribers == null || subscribers.isEmpty())
			return;
		subscribers.forEach(x -> {
			if (x.socket.isOpen())
				WebSocketMessageService.sendMessage(x.socket, MessageType.PRIVATE, userLogin.getUserName() + ": " + content, null);
		});
	}
}
