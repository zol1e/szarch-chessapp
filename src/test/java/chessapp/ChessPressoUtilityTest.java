package chessapp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import chessapp.server.game.ChesspressoUtility;
import chesspresso.Chess;
import chesspresso.move.Move;

public class ChessPressoUtilityTest {

	@Test
	public void convertToChesspressoMoveFormatTest() {
		short captureMove = ChesspressoUtility.convertMoveToChesspressoFormat("d2", "d4", "c", "w", null);
		assertEquals("d2xd4", Move.getString(captureMove));
		
		short regularMove = ChesspressoUtility.convertMoveToChesspressoFormat("d2", "d4", "n", "b", null);
		assertEquals("d2-d4", Move.getString(regularMove));
		
		short whiteShortCastle = ChesspressoUtility.convertMoveToChesspressoFormat("e1", "g1", "k", "w", null);
		assertEquals(whiteShortCastle, Move.WHITE_SHORT_CASTLE);
		
		short whiteLongCastle = ChesspressoUtility.convertMoveToChesspressoFormat("e1", "c1", "k", "w", null);
		assertEquals(whiteLongCastle, Move.WHITE_LONG_CASTLE);
		
		short blackShortCastle = ChesspressoUtility.convertMoveToChesspressoFormat("e8", "g8", "k", "b", null);
		assertEquals(blackShortCastle, Move.BLACK_SHORT_CASTLE);
		
		short blackLongCastle = ChesspressoUtility.convertMoveToChesspressoFormat("e8", "c8", "k", "b", null);
		assertEquals(blackLongCastle, Move.BLACK_LONG_CASTLE);
		
		short promoteWithRegularMove = ChesspressoUtility.convertMoveToChesspressoFormat("e7", "e8", "np", "w", "b");
		assertEquals("e7-e8B", Move.getString(promoteWithRegularMove));

		short promoteWithCapture = ChesspressoUtility.convertMoveToChesspressoFormat("e7", "d8", "cp", "w", "n");
		assertEquals("e7xd8N", Move.getString(promoteWithCapture));
	}
	
	@Test
	public void makeMoveTest() {
		String fen = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
		
		short validNonCapturing = ChesspressoUtility.convertMoveToChesspressoFormat("e7", "e5", "n", "b", null);
		String fen1 = ChesspressoUtility.makeMove(fen, validNonCapturing);
		assertEquals("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2", fen1);
		
		short invalidNonCapturing = ChesspressoUtility.convertMoveToChesspressoFormat("e7", "e4", "n", "b", null);
		String fen2 = ChesspressoUtility.makeMove(fen, invalidNonCapturing);
		assertEquals(null, fen2);
		
		fen = "rnbqkbnr/ppp1p1pp/3p4/4Pp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 3";
		short enPassantMove = ChesspressoUtility.convertMoveToChesspressoFormat("e5", "f6", "e", "w", null);
		String fen3 = ChesspressoUtility.makeMove(fen, enPassantMove);
		assertEquals("rnbqkbnr/ppp1p1pp/3p1P2/8/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 3", fen3);
	}
	
	@Test
	public void onMoveTest() {
		String fen = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
		int result = ChesspressoUtility.onMove(fen);
		assertEquals(Chess.BLACK, result);

		fen = "rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2";
		result = ChesspressoUtility.onMove(fen);
		assertEquals(Chess.WHITE, result);
		
		fen = "rnb1kbnr/pppp1ppp/8/4p3/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 1 3";
		result = ChesspressoUtility.onMove(fen);
		assertEquals(Chess.WHITE, result);
	}
	
	@Test
	public void getGameStateTest() {
		String fen = "rnb1kbnr/pppp1ppp/8/4p3/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 1 3";
		int result = ChesspressoUtility.getGameState(fen);
		assertEquals(Chess.RES_BLACK_WINS, result);
	}
	
	@Test
	public void getMoveStringTest() {
		short captureMove = ChesspressoUtility.convertMoveToChesspressoFormat("d2", "d4", "n", "w", null);
		String result = ChesspressoUtility.getMoveString(captureMove);
		assertEquals(Chess.RES_BLACK_WINS, result);
	}
}