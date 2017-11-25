package chessapp.client.main;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chessapp.server.service.LobbyService;

public class GetGameLobbiesServlet  extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sessionId = request.getSession().getId();
		LobbyService lobbyService = new LobbyService();

		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
		int status = lobbyService.getPendingLobbies(objectBuilder, sessionId);
		response.setStatus(status);
		if (status == 200)
			JsonResponseWriterHelper.writeResponse(response, objectBuilder);
		else
			response.sendError(status);
	}

}