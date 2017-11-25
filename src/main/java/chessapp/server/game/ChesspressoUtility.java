package chessapp.server.game;

import chessapp.server.game.GameStatus.GameResult;
import chesspresso.Chess;
import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.position.FEN;
import chesspresso.position.Position;

public class ChesspressoUtility {

	// Make the move, and if its valid in the current position, return the position
	// in FEN.
	// In other cases, return null.
	public static String makeMove(String fen, String moveFrom, String moveTo, String flag, String color,
			String promotion) {
		return makeMove(fen, convertMoveToChesspressoFormat(moveFrom, moveTo, flag, color, promotion));
	}

	// Make the move, and if its valid in the current position, return the position
	// in FEN.
	// In other cases, return null.
	public static String makeMove(String fen, short move) {
		Position position = new Position();
		FEN.initFromFEN(position, fen, false);

		if (!Move.isValid(move)) {
			return null;
		}

		boolean legal = false;
		for (short legalMove : position.getAllMoves()) {
			if (legalMove == move) {
				legal = true;
				break;
			}
		}
		if (!legal) {
			return null;
		}

		try {
			position.doMove(move);
		} catch (IllegalMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!position.isLegal()) {
			return null;
		}

		return position.getFEN();
	}

	public static String getMoveString(short move) {
		return Move.getString(move);
	}
	
	// Convert the chessjs move object to chesspresso move
	public static short convertMoveToChesspressoFormat(String moveFrom, String moveTo, String flag, String color,
			String promotion) {
		// Castle move
		if (flag.equals("k")) {
			if (color.equals("w")) {
				if (moveTo.equals("g1")) {
					return Move.WHITE_SHORT_CASTLE;
				}
				if (moveTo.equals("c1")) {
					return Move.WHITE_LONG_CASTLE;
				}
			}
			if (color.equals("b")) {
				if (moveTo.equals("g8")) {
					return Move.BLACK_SHORT_CASTLE;
				}
				if (moveTo.equals("c8")) {
					return Move.BLACK_LONG_CASTLE;
				}
			}
		}

		int fromCol = Chess.charToCol(moveFrom.charAt(0));
		int fromRow = Chess.charToRow(moveFrom.charAt(1));

		int toCol = Chess.charToCol(moveTo.charAt(0));
		int toRow = Chess.charToRow(moveTo.charAt(1));

		int fromSqi = Chess.coorToSqi(fromCol, fromRow);
		int toSqi = Chess.coorToSqi(toCol, toRow);

		// Enpassant move
		if (flag.equals("e")) {
			return Move.getEPMove(fromSqi, toSqi);
		}

		// Capture
		if (flag.equals("c")) {
			return Move.getRegularMove(fromSqi, toSqi, true);
		}

		// Normal move
		if (flag.equals("n") || flag.equals("b")) {
			return Move.getRegularMove(fromSqi, toSqi, false);
		}

		// If we are here, it's probably a pawn promotion
		int promotionPiece = -1;
		if (promotion != null) {
			switch (promotion) {
			case "q":
				promotionPiece = Chess.QUEEN;
				break;
			case "r":
				promotionPiece = Chess.ROOK;
				break;
			case "b":
				promotionPiece = Chess.BISHOP;
				break;
			case "n":
				promotionPiece = Chess.KNIGHT;
				break;
			default:
				promotionPiece = -1;
			}
		}

		// Promotion with capture
		if (flag.equals("cp") && promotionPiece != -1) {
			return Move.getPawnMove(fromSqi, toSqi, true, promotionPiece);
		}

		// Promotion with move
		if (flag.equals("np") && promotionPiece != -1) {
			return Move.getPawnMove(fromSqi, toSqi, false, promotionPiece);
		}
		
		return -1;
	}

	// Return the side which is on move
	public static int onMove(String fen) {
		Position position = new Position();
		FEN.initFromFEN(position, fen, false);
		return position.getToPlay();
	}

	// Return the side which is on move
	public static String onMoveString(String fen) {
		int side = onMove(fen);
		if(side == Chess.WHITE) {
			return "white";
		}
		if(side == Chess.BLACK) {
			return "black";
		}
		return null;
	}

	// Return the state of the game
	public static int getGameState(String fen) {
		Position position = new Position();
		FEN.initFromFEN(position, fen, false);
		if (position.isMate() && position.getToPlay() == Chess.WHITE) {
			return Chess.RES_BLACK_WINS;
		}
		if (position.isMate() && position.getToPlay() == Chess.BLACK) {
			return Chess.RES_WHITE_WINS;
		}
		if (position.isStaleMate()) {
			return Chess.RES_DRAW;
		}
		return Chess.RES_NOT_FINISHED;
	}
	
	private static String getGameResult(int gameState) {
		if(gameState == Chess.RES_BLACK_WINS) {
			return GameResult.RESULT_BLACK_WON.getStringValue();
		} else if(gameState == Chess.RES_WHITE_WINS) {
			return GameResult.RESULT_WHITE_WON.getStringValue();
		} else if(gameState == Chess.RES_DRAW) {
			return GameResult.RESULT_DRAW.getStringValue();
		} else {
			return null;
		}
	}

	// Get game result in text format
	public static String getGameResult(String fen) {
		return getGameResult(getGameState(fen));
	}
	
	// Create a starting position and return it in FEN format
	public static String getStartingPositionFEN() {
		return Position.createInitialPosition().getFEN();
	}
}