package chessapp.server.service;


import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import chessapp.server.model.ChessGameBean;
import chessapp.shared.entities.ChessGame;

public class ExploreLatestGamesService {
	public int makeResponse(String gameId, JsonObjectBuilder objectBuilder) {
		ChessGameBean chessGameBean = new ChessGameBean();
		if (gameId == null || gameId.isEmpty()) {
			List<ChessGame> games = chessGameBean.getLatestSomeGames(10);
			
			objectBuilder.add("latestGames", makePreviewTableRows(games));
			
			return 200;
		} else {
			ChessGame selectedGame = chessGameBean.findGame(gameId);
			if (selectedGame == null) {
				return 404;
			}
			objectBuilder.add("selectedGame", makeSelectedGameJSON(selectedGame));
			
			return 200;
		}
	}

	private JsonArrayBuilder makePreviewTableRows(List<ChessGame> games) {
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		
		for(ChessGame game : games) {
		    arrayBuilder.add(makeGamePreviewJSON(game));
		}
		return arrayBuilder;
	}
	
	private JsonObjectBuilder makeSelectedGameJSON(ChessGame game) {
		JsonObjectBuilder gameBuilder = Json.createObjectBuilder()
				.add("blackPlayer", game.getBlackPlayer() == null ? "" : game.getBlackPlayer())
				.add("whitePlayer", game.getWhitePlayer() == null ? "" : game.getWhitePlayer())
				.add("winner", game.getResult() == null ? "" : game.getResult())
				.add("startTime", game.getStartDate() == null ? "" : game.getStartDate().toString())
				.add("endTime", game.getEndDate() == null ? "" : game.getEndDate().toString())
				.add("white time left", game.getWhiteTimeLeft() == null ? "" : game.getWhiteTimeLeft().toString())
				.add("black time left", game.getBlackTimeLeft() == null ? "" : game.getBlackTimeLeft().toString())
				.add("moves", game.getMoves() == null ? "" : game.getMoves());
		return gameBuilder;
	}
	
	private JsonObjectBuilder makeGamePreviewJSON(ChessGame game) {
		if (game == null)
			return Json.createObjectBuilder();
		JsonObjectBuilder gameBuilder = Json.createObjectBuilder()
				.add("blackPlayer", game.getBlackPlayer() == null ? "" : game.getBlackPlayer())
				.add("whitePlayer", game.getWhitePlayer() == null ? "" : game.getWhitePlayer())
				.add("gameId", game.getChessGameId())
				.add("winner", game.getResult() == null ? "" : game.getResult());
		return gameBuilder;
	}
}
