package chessapp.server.game;

import java.util.concurrent.ThreadLocalRandom;

import chesspresso.move.IllegalMoveException;
import chesspresso.position.FEN;
import chesspresso.position.Position;

public class ChesspressoUtility {

	public String generateRandomMove(String fen) {
		 Position position = new Position();
		 fen += " b KQkq - 0 1";
		 FEN.initFromFEN(position, fen, false);
		 short[] moves = position.getAllMoves();
		 if(moves.length > 0) {
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
	
}