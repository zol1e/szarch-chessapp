package chessapp.client.main;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chessapp.client.main.WebSocketHandler.MessageType;
import chessapp.server.PrivateSocketRepository;
import chessapp.server.game.Subscriber;
import chessapp.server.model.ChessGameBean;
import chessapp.server.model.LoginBean;
import chessapp.shared.entities.ChessGame;

public class CreateJoinGameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		LoginBean loginBean = new LoginBean();
		ChessGameBean chessGameBean = new ChessGameBean();
		
		String userName = loginBean.findBySessionId(request.getSession().getId()).getUserName();
		System.out.println(userName + " pressed a create button");
		ChessGame ongoing = chessGameBean.getOngoingBySomePlayer(userName);
		if (ongoing != null) {
			System.out.println(userName + " user tried to create a game, but another game is in progress");
			return;
		}
		
		Random rand = new Random();
		int  n = rand.nextInt(2);
		if (n == 0) {
			chessGameBean.create(userName, null);
		} else { 
			chessGameBean.create(null, userName);
		}

		System.out.println("new lobby created");	
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		LoginBean loginBean = new LoginBean();
		ChessGameBean chessGameBean = new ChessGameBean();
		
		String userName = loginBean.findBySessionId(request.getSession().getId()).getUserName();
		System.out.println(userName + " pressed a join button");
		ChessGame ongoing = chessGameBean.getOngoingBySomePlayer(userName);
		if (ongoing != null) {
			return;
		}

		String gameId = request.getParameter("game");
		if(gameId == null) {
			System.out.println(userName + "named user tried to connect with null game id");
			return;
		}
		ChessGame chessGame = chessGameBean.findGame(gameId);
		
		if (chessGame == null) {
			return;
		}
		
		if (chessGame.getBlackPlayer() == null) {
			chessGame.setBlackPlayer(userName);
		} else if (chessGame.getWhitePlayer() == null) {
			chessGame.setWhitePlayer(userName);
		}
		
		// Time limit currently hardcoded
		// TODO: get parameter or decided if its okay
		Long timeLimit = new Long(60000);
		
		Date currentDate = new Date();
		chessGame.setStartDate(currentDate);
		chessGame.setOnMove(ChessGame.WHITE);
		chessGame.setLastMoveTime(currentDate);
		chessGame.setWhiteTimeLeft(timeLimit);
		chessGame.setWhiteTimeLeft(timeLimit);
		
		chessGameBean.update(chessGame);
		
		List<Subscriber> subscribers = PrivateSocketRepository.connections.get(chessGame.getChessGameId());
		if (subscribers == null || subscribers.isEmpty()) {
			return;
		}
		
		// Send message to the players, that the game has started
		subscribers.forEach(subscriber -> {
			if(subscriber.socket.isOpen())
			    WebSocketHandler.sendMessage(subscriber.socket, MessageType.GAME, "Game has started", null);
		});
		
		System.out.println("player added to lobby");			
	}
}
