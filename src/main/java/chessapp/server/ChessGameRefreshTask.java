package chessapp.server;

import java.util.Date;
import java.util.List;

import chessapp.server.game.ChesspressoUtility;
import chessapp.server.game.GameStatus;
import chessapp.server.game.GameStatus.GameResult;
import chessapp.server.model.ChessGameBean;
import chessapp.server.service.ChessGameMessageService;
import chessapp.shared.entities.ChessGame;
import chesspresso.Chess;

public class ChessGameRefreshTask implements Runnable {

	@Override
	public void run() {
		ChessGameBean chessGameBean = new ChessGameBean();
		List<ChessGame> ongoingGames = chessGameBean.getOngoingChessGames();
		Date dateNow = new Date();

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
					chessGame.setResult(GameResult.RESULT_BLACK_WON.getStringValue());
					gameOver = true;
				}
			}
			if (Chess.BLACK == onMove) {
				Long blackTimeLeftNow = chessGame.getBlackTimeLeft() - difference;
				if (blackTimeLeftNow <= 0) {
					chessGame.setBlackTimeLeft(new Long(0));
					chessGame.setEndDate(dateNow);
					chessGame.setResult(GameResult.RESULT_WHITE_WON.getStringValue());
					gameOver = true;
				}
			}

			// Csak akkor csinálunk valamit, ha lejárt az idő
			if (gameOver) {
				chessGameBean.update(chessGame);
				GameStatus gameStatus = ChessGameBean.getGameStatus(chessGame);
				ChessGameMessageService.broadcastGameStatus(chessGame, gameStatus);
			}
		}
	}

}
