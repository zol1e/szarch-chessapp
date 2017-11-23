package chessapp.server.game;

import java.util.concurrent.ThreadLocalRandom;

import chesspresso.Chess;
import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.position.FEN;
import chesspresso.position.Position;

public class ChesspressoUtility {

	public String generateRandomMove(String fen) {
		Position position = new Position();
		fen += " b KQkq - 0 1";
		FEN.initFromFEN(position, fen, false);
		short[] moves = position.getAllMoves();

		position.getLastMove();

		if (moves.length > 0) {
			try {
				int randomNum = ThreadLocalRandom.current().nextInt(0, moves.length);
				position.doMove(moves[randomNum]);
				return position.getFEN();
			} catch (IllegalMoveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String makeMove(String fen, short move) {
		Position position = new Position();
		FEN.initFromFEN(position, fen, false);
		
		if(!Move.isValid(move)) {
			return null;
		}
		
		boolean legal = false;
		for(short legalMove : position.getAllMoves()) {
			if(legalMove == move) {
				legal = true;
				break;
			}
		}
		if(!legal) {
			return null;
		}
		
		try {
			position.doMove(move);
		} catch (IllegalMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!position.isLegal()) {
			return null;
		}
		
		return position.getFEN();
	}
	
	public static short convertMoveToChesspressoFormat(String moveFrom, String moveTo, String flag, String color, String promotion) {
		if(flag.equals("k")) {
			if(color.equals("w")) {
				if(moveTo.equals("g1")) {
					return Move.WHITE_SHORT_CASTLE;
				}
				if(moveTo.equals("c1")) {
					return Move.WHITE_LONG_CASTLE;
				}
			}
			if(color.equals("b")) {
				if(moveTo.equals("g8")) {
					return Move.BLACK_SHORT_CASTLE;
				}
				if(moveTo.equals("c8")) {
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
		
		if(flag.equals("c")) {
			return Move.getRegularMove(fromSqi, toSqi, true);
		}
		
		if(flag.equals("n")) {
			return Move.getRegularMove(fromSqi, toSqi, false);
		}
		
		int promotionPiece = -1;
		if(promotion != null) {
			switch(promotion) {
				case "q" : promotionPiece = Chess.QUEEN; break;
				case "r" : promotionPiece = Chess.ROOK; break;
				case "b" : promotionPiece = Chess.BISHOP; break;
				case "n" : promotionPiece = Chess.KNIGHT; break;
				default: promotionPiece = -1;
			}
		}
		
		if(flag.equals("cp")) {
			return Move.getPawnMove(fromSqi, toSqi, true, promotionPiece);
		}
		
		if(flag.equals("np")) {
			return Move.getPawnMove(fromSqi, toSqi, false, promotionPiece);
		}
		
		return -1;
	}
}