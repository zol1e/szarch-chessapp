package chessapp.client.main;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chessapp.server.model.ChessGameBean;
import chessapp.server.model.LoginBean;
import chessapp.shared.entities.ChessGame;

public class CreateJoinGameServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LoginBean lb = new LoginBean();
		ChessGameBean cgb = new ChessGameBean();
		String player = lb.findBySessionId(request.getSession().getId()).getUserName();
		System.out.println(player + " pressed a create button");
		ChessGame ongoing = cgb.getOngoingBySomePlayer(player);
		if (ongoing != null)
			return;
		
		Random rand = new Random();
		int  n = rand.nextInt(2);
		if (n == 0)
			cgb.create(player, null);
		else 
			cgb.create(null, player);

		System.out.println("new lobby created");	
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LoginBean lb = new LoginBean();
		ChessGameBean cgb = new ChessGameBean();
		String player = lb.findBySessionId(request.getSession().getId()).getUserName();
		System.out.println(player + " pressed a join button");
		ChessGame ongoing = cgb.getOngoingBySomePlayer(player);
		if (ongoing != null)
			return;

		String gameId = (String)request.getParameter("game");
		ChessGame game = cgb.findGame(gameId);
		if (game == null)
			return;
		if (game.getBlackPlayer() == null)
			game.setBlackPlayer(player);
		else if (game.getWhitePlayer() == null)
			game.setWhitePlayer(player);
		cgb.update(game);
		System.out.println("player added to lobby");
				
	}
}
