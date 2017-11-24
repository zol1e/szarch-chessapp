package chessapp.server.service;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import chessapp.client.main.WebSocketHandler;
import chessapp.client.main.WebSocketHandler.MessageType;
import chessapp.server.GameSocketRepository;
import chessapp.server.game.ChesspressoUtility;
import chessapp.server.game.ColoredSubscriber;
import chessapp.server.game.GameStatus;
import chessapp.server.model.ChessGameBean;
import chessapp.server.model.LoginBean;
import chessapp.shared.entities.ChessGame;

public class LobbyService {
	ChessGameBean chessGameBean = new ChessGameBean();
	LoginBean loginBean = new LoginBean();
	public LobbyService() {
		
	}
	
	public int createLobby(String sessionId) {
		
		String userName = loginBean.findBySessionId(sessionId).getUserName();
		
		if (hasOngoingGame(userName) || hasPendingGame(userName)) {
			return 401;
		}
				
		Long timeLimit = new Long(120000);
		ChessGame newChessGame = getNewChessGame(timeLimit);
		
		if (getZeroOrOne() == 0)
			newChessGame.setWhitePlayer(userName);
		else
			newChessGame.setBlackPlayer(userName);
		

		chessGameBean.create(newChessGame);
		return 200;
	}

	private ChessGame getNewChessGame(Long timeLimit) {
		ChessGame newChessGame = new ChessGame();
		newChessGame.setWhiteTimeLeft(timeLimit);
		newChessGame.setBlackTimeLeft(timeLimit);
		newChessGame.setFen(ChesspressoUtility.getStartingPositionFEN());
		newChessGame.setMoves("");
		return newChessGame;
	}

	private int getZeroOrOne() {
		Random rand = new Random();
		return rand.nextInt(2);
	}
	
	private boolean hasPendingGame(String userName) {
		List<ChessGame> lobbies = chessGameBean.getPendingBySomePlayer(userName);
		if (lobbies != null && !lobbies.isEmpty())
			return true;
		return false;
	}

	private boolean hasOngoingGame(String userName) {
		if (chessGameBean.getOngoingBySomePlayer(userName) != null)
			return true;
		return false;
	}

	public int joinLobby(String sessionId, String gameId) {
		if (gameId == null || gameId.isEmpty()|| sessionId == null || sessionId.isEmpty()) {
			return 400;
		}
		String userName = loginBean.findBySessionId(sessionId).getUserName();
		if (userName == null || userName.isEmpty()) {
			return 400;
		}
		ChessGame ongoing = chessGameBean.getOngoingBySomePlayer(userName);
		
		List<ChessGame> pending = chessGameBean.getPendingBySomePlayer(userName);
		ChessGame chessGame = chessGameBean.findGame(gameId);
		
		if (ongoing != null || chessGame == null || (pending != null && !pending.isEmpty())) {
			return 400;
		}
		
		boolean success = setSecondPlayer(userName, chessGame);
		if (!success)
			return 400;
		
		// Time limit currently hardcoded
		// TODO: get parameter or decided if its okay
		
		Date currentDate = new Date();
		chessGame.setStartDate(currentDate);
		chessGame.setLastMoveTime(currentDate);
		chessGameBean.update(chessGame);
		
		List<ColoredSubscriber> subscribers = GameSocketRepository.getSubscribers(chessGame.getChessGameId());
		if (subscribers == null || subscribers.isEmpty()) {
			return 200;
		}
		
		GameStatus gameStatus = ChessGameBean.getGameStatus(chessGame);
		
		// Send message to the players, that the game has started
		for (ColoredSubscriber subscriber : subscribers) {
			if(subscriber.socket.isOpen())
			    WebSocketHandler.sendMessage(subscriber.socket, MessageType.GAME, "Game has started", gameStatus);
		}
		return 200;
	}

	private boolean setSecondPlayer(String userName, ChessGame chessGame) {
		if (isBlackSeatFree(userName, chessGame)) {
			chessGame.setBlackPlayer(userName);
		} else if (isWhiteSeatFree(userName, chessGame)) {
			chessGame.setWhitePlayer(userName);
		} else {
			return false;
		}
		return true;
	}

	private boolean isWhiteSeatFree(String userName, ChessGame chessGame) {
		return chessGame.getWhitePlayer() == null && !userName.equals(chessGame.getBlackPlayer());
	}

	private boolean isBlackSeatFree(String userName, ChessGame chessGame) {
		return chessGame.getBlackPlayer() == null && !userName.equals(chessGame.getWhitePlayer());
	}

	public int getPendingLobbies(JsonObjectBuilder objectBuilder) {
		List<ChessGame> games = chessGameBean.getHalfEmptyGames();
		
		JsonArrayBuilder tableRows = Json.createArrayBuilder();
        
		for(ChessGame game : games) {
			tableRows.add(makeSingleLobbyTableRow(game));
		}
		objectBuilder.add("gameLobbies", tableRows);
		return 200;
	}

	private JsonObjectBuilder makeSingleLobbyTableRow(ChessGame game) {
		JsonObjectBuilder gameBuilder = Json.createObjectBuilder()
				.add("blackPlayer", game.getBlackPlayer() == null ? "" : game.getBlackPlayer())
				.add("whitePlayer", game.getWhitePlayer() == null ? "" : game.getWhitePlayer())
				.add("game", game.getChessGameId());
		return gameBuilder;
	}

}
