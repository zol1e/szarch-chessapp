package chessapp.client.main;

import java.io.IOException;
import java.util.Enumeration;

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
		String func = request.getParameter("func");
		int status = 200;
		LobbyService lobbyService = new LobbyService();
		if(gameId == null || func == null || gameId.isEmpty() || func.isEmpty()) {
			return;
		} else if ("Join".equals(func)) {
			status = lobbyService.joinLobby(sessionId, gameId);
		} else if ("Cancel".equals(func)){
			status = lobbyService.cancelLobby(sessionId, gameId);
		}
		
		 
		response.setStatus(status);
		
		System.out.println("player added to lobby");
	}
	
}
