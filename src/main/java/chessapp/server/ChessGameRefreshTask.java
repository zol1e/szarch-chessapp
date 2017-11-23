package chessapp.server;

import java.util.Date;
import java.util.List;

import chessapp.client.main.WebSocketHandler;
import chessapp.client.main.WebSocketHandler.MessageType;
import chessapp.server.game.ChesspressoUtility;
import chessapp.server.game.ColoredSubscriber;
import chessapp.server.game.GameStatus;
import chessapp.server.model.ChessGameBean;
import chessapp.shared.entities.ChessGame;
import chesspresso.Chess;

public class ChessGameRefreshTask implements Runnable {

	@Override
	public void run() {
		ChessGameBean chessGameBean = new ChessGameBean();
		List<ChessGame> ongoingGames = chessGameBean.getOngoingChessGames();
		Date dateNow = new Date();

		System.out.println(ongoingGames.size() + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
				+ dateNow.toString() + ": refreshing job call");

		for (ChessGame chessGame : ongoingGames) {
			int onMove = ChesspressoUtility.onMove(chessGame.getFen());

			// TODO: ez egy paraszt kódmásolás a websocket handlerből, meg kell csinálni
			// egységesre
			Long lastMoveTime = chessGame.getLastMoveTime().getTime();
			Long nowMillis = dateNow.getTime();
			Long difference = nowMillis - lastMoveTime;

			boolean gameOver = false;
			if (Chess.WHITE == onMove) {
				Long whiteTimeLeftNow = chessGame.getWhiteTimeLeft() - difference;
				if (whiteTimeLeftNow <= 0) {
					chessGame.setWhiteTimeLeft(new Long(0));
					chessGame.setEndDate(dateNow);
					chessGame.setResult("white lost on time");
					gameOver = true;
				}
			}
			if (Chess.BLACK == onMove) {
				Long blackTimeLeftNow = chessGame.getBlackTimeLeft() - difference;
				if (blackTimeLeftNow <= 0) {
					chessGame.setBlackTimeLeft(new Long(0));
					chessGame.setEndDate(dateNow);
					chessGame.setResult("black lost on time");
					gameOver = true;
				}
			}

			// Csak akkor csinálunk valamit, ha lejárt az idő
			if (gameOver) {
				chessGameBean.update(chessGame);
				GameStatus gameStatus = ChessGameBean.getGameStatus(chessGame);

				List<ColoredSubscriber> subscribers = GameSocketRepository.connections.get(chessGame.getChessGameId());
				if (subscribers == null || subscribers.isEmpty())
					return;

				subscribers.forEach(x -> {
					if (x.socket.isOpen())
						WebSocketHandler.sendMessage(x.socket, MessageType.GAME, "game status message ", gameStatus);
				});
			}
		}
	}

}
