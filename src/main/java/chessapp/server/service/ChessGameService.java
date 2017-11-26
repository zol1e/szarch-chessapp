package chessapp.server.service;

import static chessapp.server.service.WebSocketMessageService.WS_MOVE_BLACK_DRAW_OFFER;
import static chessapp.server.service.WebSocketMessageService.WS_MOVE_COLOR;
import static chessapp.server.service.WebSocketMessageService.WS_MOVE_FLAGS;
import static chessapp.server.service.WebSocketMessageService.WS_MOVE_FROM;
import static chessapp.server.service.WebSocketMessageService.WS_MOVE_PROMOTION;
import static chessapp.server.service.WebSocketMessageService.WS_MOVE_RESIGN;
import static chessapp.server.service.WebSocketMessageService.WS_MOVE_TO;
import static chessapp.server.service.WebSocketMessageService.WS_MOVE_WHITE_DRAW_OFFER;
import static chessapp.server.service.WebSocketMessageService.WS_TRUE;

import java.util.Date;

import javax.json.JsonObject;

import org.eclipse.jetty.websocket.api.Session;

import chessapp.server.GameSocketRepository;
import chessapp.server.game.ChessGameUtility;
import chessapp.server.game.ChesspressoUtility;
import chessapp.server.game.GameStatus;
import chessapp.server.game.GameStatus.GameResult;
import chessapp.server.model.ChessGameBean;
import chessapp.server.service.WebSocketMessageService.MessageType;
import chessapp.shared.entities.ChessGame;
import chessapp.shared.entities.UserLogin;
import chesspresso.Chess;

public class ChessGameService {
	
	private ChessGameBean chessGameBean = new ChessGameBean();
	
	public void connectGameSocket(Session session, Date dateNow, UserLogin userLogin, ChessGame chessGame) {
		boolean amIBlack = userLogin.getUserName().equals(chessGame.getBlackPlayer());

		if (chessGame != null) {
			int onMove = ChesspressoUtility.onMove(chessGame.getFen());
			ChessGameService.updateChessGameTime(chessGameBean, chessGame, onMove, dateNow);
		}

		GameSocketRepository.addPlayer(chessGame.getChessGameId(), userLogin.getUserName(), session, amIBlack);
		if (session.isOpen()) {
			WebSocketMessageService.sendMessage(session, MessageType.GAME, "game status message ",
					ChessGameBean.getGameStatus(chessGame));
		}
	}

	public void disconnectGameSocket(Session session, Date dateNow, UserLogin userLogin, ChessGame chessGame) {
		if (chessGame != null)
			GameSocketRepository.removeConnection(chessGame.getChessGameId(), userLogin.getUserName());
	}
	
	public static ChessGame updateChessGameTime(ChessGameBean chessGameBean, ChessGame chessGame, int onMove,
			Date dateNow) {
		Long lastMoveTime = chessGame.getLastMoveTime().getTime();
		Long nowMillis = dateNow.getTime();
		Long difference = nowMillis - lastMoveTime;
		if (Chess.WHITE == onMove) {
			Long whiteTimeLeftNow = chessGame.getWhiteTimeLeft() - difference;
			if (whiteTimeLeftNow <= 0) {
				chessGame.setWhiteTimeLeft(new Long(0));
				chessGame.setEndDate(dateNow);
				chessGame.setResult(GameResult.RESULT_BLACK_WON.getStringValue());
			} else {
				chessGame.setWhiteTimeLeft(whiteTimeLeftNow);
			}
		}
		if (Chess.BLACK == onMove) {
			Long blackTimeLeftNow = chessGame.getBlackTimeLeft() - difference;
			if (blackTimeLeftNow <= 0) {
				chessGame.setBlackTimeLeft(new Long(0));
				chessGame.setEndDate(dateNow);
				chessGame.setResult(GameResult.RESULT_WHITE_WON.getStringValue());
			} else {
				chessGame.setBlackTimeLeft(blackTimeLeftNow);
			}
		}
		chessGame.setLastMoveTime(dateNow);

		return chessGame;
	}

	public void sendMove(Date dateNow, JsonObject message, UserLogin userLogin, ChessGame chessGame) {
		String from;
		String to;
		String color;
		String flags;
		String promotion;
		String whiteDrawOffer;
		String blackDrawOffer;
		String resign;

		try {
			from = message.getString(WS_MOVE_FROM);
			to = message.getString(WS_MOVE_TO);
			color = message.getString(WS_MOVE_COLOR);
			flags = message.getString(WS_MOVE_FLAGS);
			promotion = message.getString(WS_MOVE_PROMOTION);
			whiteDrawOffer = message.getString(WS_MOVE_WHITE_DRAW_OFFER);
			blackDrawOffer = message.getString(WS_MOVE_BLACK_DRAW_OFFER);
			resign = message.getString(WS_MOVE_RESIGN);
		} catch (Exception e) {
			return;
		}

		String oldPosition = chessGame.getFen();
		int onMove = ChesspressoUtility.onMove(oldPosition);

		boolean rightUserOnmove = false;
		{
			String playerOnMove;

			// It should be checked if the right player is on move
			if (onMove == Chess.WHITE) {
				playerOnMove = chessGame.getWhitePlayer();
			} else {
				playerOnMove = chessGame.getBlackPlayer();
			}
			if (userLogin.getUserName().equals(playerOnMove)) {
				rightUserOnmove = true;
			}
		}

		boolean playerResigned = false;
		{
			if (resign.equals(WS_TRUE)) {
				chessGame.setEndDate(dateNow);
				playerResigned = true;
				if (chessGame.getWhitePlayer().equals(userLogin.getUserName())) {
					chessGame.setResult(GameResult.RESULT_BLACK_WON.getStringValue());
				}
				if (chessGame.getBlackPlayer().equals(userLogin.getUserName())) {
					chessGame.setResult(GameResult.RESULT_WHITE_WON.getStringValue());
				}
			}
		}

		boolean drawAgree = false;
		// User sent a draw offer
		if (whiteDrawOffer.equals(WS_TRUE) && chessGame.getWhitePlayer().equals(userLogin.getUserName())) {
			drawAgree = true;
			if (chessGame.isBlackDrawOffer()) {
				// Draw agreed
				chessGame.setResult(GameResult.RESULT_DRAW.getStringValue());
				chessGame.setEndDate(dateNow);
			} else {
				chessGame.setWhiteDrawOffer(true);
			}
		}
		if (blackDrawOffer.equals(WS_TRUE) && chessGame.getBlackPlayer().equals(userLogin.getUserName())) {
			drawAgree = true;
			if (chessGame.isWhiteDrawOffer()) {
				// Draw agreed
				chessGame.setResult(GameResult.RESULT_DRAW.getStringValue());
				chessGame.setEndDate(dateNow);
			} else {
				chessGame.setBlackDrawOffer(true);
			}
		}

		if (rightUserOnmove && !drawAgree && !playerResigned) {
			String newPosition = ChesspressoUtility.makeMove(oldPosition, from, to, flags, color, promotion);
			if (newPosition != null) {
				chessGame.setFen(newPosition);
				String moves = ChessGameUtility.addMoveToDBMoves(chessGame.getMoves(), from, to, flags, color,
						promotion);
				chessGame.setMoves(moves);

				String resultString = ChesspressoUtility.getGameResult(newPosition);

				if (resultString != null) {
					chessGame.setEndDate(dateNow);
					chessGame.setResult(resultString);
				}

				chessGame.setBlackDrawOffer(false);
				chessGame.setWhiteDrawOffer(false);
				chessGame = ChessGameService.updateChessGameTime(chessGameBean, chessGame, onMove, dateNow);
				chessGameBean.update(chessGame);
			}
		}

		if (playerResigned || drawAgree) {
			chessGame = ChessGameService.updateChessGameTime(chessGameBean, chessGame, onMove, dateNow);
			chessGameBean.update(chessGame);
		}

		GameStatus gameStatus = ChessGameBean.getGameStatus(chessGame);

		ChessGameMessageService.broadcastGameStatus(chessGame, gameStatus);
	}
}
