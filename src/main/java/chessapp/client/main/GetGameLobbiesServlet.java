package chessapp.client.main;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chessapp.server.model.ChessGameBean;
import chessapp.server.service.LobbyService;
import chessapp.shared.entities.ChessGame;

public class GetGameLobbiesServlet  extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		LobbyService lobbyService = new LobbyService();

		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
		int status = lobbyService.getPendingLobbies(objectBuilder);
		response.setStatus(status);
		if (status == 200)
			JsonResponseWriterHelper.writeResponse(response, objectBuilder);
		else
			response.sendError(status);
	}

}
