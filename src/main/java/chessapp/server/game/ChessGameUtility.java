package chessapp.server.game;

import java.util.Arrays;
import java.util.List;

public class ChessGameUtility {
	
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
