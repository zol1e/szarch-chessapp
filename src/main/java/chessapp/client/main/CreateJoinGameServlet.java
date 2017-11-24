package chessapp.client.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chessapp.server.service.LobbyService;

public class CreateJoinGameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String sessionId = request.getSession().getId();
		LobbyService lobbyService = new LobbyService();
		int status = lobbyService.createLobby(sessionId);
		response.setStatus(status);
		
		System.out.println("lobby creation status: " + status);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String sessionId = request.getSession().getId();
		String gameId = request.getParameter("game");
		if(gameId == null) {
			return;
		}
		
		LobbyService lobbyService = new LobbyService();
		int status = lobbyService.joinLobby(sessionId, gameId);
		response.setStatus(status);
		
		System.out.println("player added to lobby");
	}
}
