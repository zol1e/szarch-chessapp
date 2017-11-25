package chessapp.server.game;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import chessapp.server.game.GameStatus.GameResult;
import chessapp.server.model.ChessGameBean;
import chessapp.shared.entities.ChessGame;
import chesspresso.Chess;

public class ChessGameUtility {

	public static ChessGame updateChessGameTime(ChessGameBean chessGameBean, ChessGame chessGame, int onMove, Date dateNow) {
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
	
	/**
	 * Return the list of moves represented as one string in the databse. Moves
	 * separated with "|" symbols and the order of properties: from + " " + to + " "
	 * + color + " " + flags + " " + promotion
	 */
	public static List<String> createListFromDBMoves(String dbMoves) {
		return Arrays.asList(dbMoves.split("|"));
	}

	/**
	 * Concatenate a new move to the string representation of the moves in the database
	 */
	public static String addMoveToDBMoves(String oldMoves, String from, String to, String flags,
			String color, String promotion) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(oldMoves);
		
		// If there is no moves yet, the separator is not needed
		if(oldMoves.length() > 0) { 
			builder.append("|");
		}
		
		builder.append(from)
		.append(" ")
		.append(to)
		.append(" ")
		.append(color)
		.append(" ")
		.append(flags)
		.append(" ")
		.append(promotion);
		
		return builder.toString();
	}
}
