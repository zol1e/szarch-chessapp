package chessapp.server.service;

import static chessapp.server.service.WebSocketMessageService.WS_PROPERTY_CONTENT;
import static chessapp.server.service.WebSocketMessageService.WS_PROPERTY_TYPE;
import static chessapp.server.service.WebSocketMessageService.WS_TYPE_GLOBAL_MESSAGE;

import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.eclipse.jetty.websocket.api.Session;

import chessapp.server.GlobalSocketRepository;
import chessapp.server.model.GlobalChatMessageBean;
import chessapp.server.service.WebSocketMessageService.MessageType;
import chessapp.shared.entities.GlobalChatMessage;
import chessapp.shared.entities.UserLogin;

public class GlobalMessageService {

	private GlobalChatMessageBean globalChatMsgBean = new GlobalChatMessageBean();

	public static void connectGlobalChat(String httpSessionId, Session session) {
		GlobalSocketRepository.putConnection(httpSessionId, session);
	}

	public static void disconnectGlobalChat(String httpSessionId, Session session) {
		GlobalSocketRepository.removeConnection(httpSessionId, session);
	}

	public void sendGlobalMessage(UserLogin userLogin, String content) {
		globalChatMsgBean.create(new GlobalChatMessage(userLogin.getUserName(), content));

		for (Entry<String, Session> entry : GlobalSocketRepository.connections.entrySet()) {
			Session sess = entry.getValue();
			if (sess.isOpen()) {
				WebSocketMessageService.sendMessage(sess, MessageType.GLOBAL, userLogin.getUserName() + ": " + content,
						null);
			} else {
				GlobalSocketRepository.removeConnection(entry.getKey(), sess);
			}
		}
	}

	public static void sendGlobalMessageOnSocket(Session session, String content) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add(WS_PROPERTY_TYPE, WS_TYPE_GLOBAL_MESSAGE);
		builder.add(WS_PROPERTY_CONTENT, content);
		JsonObject message = builder.build();
		session.getRemote().sendString(message.toString(), null);
		return;
	}
}
