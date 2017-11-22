package chessapp.client.main;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chessapp.server.model.ChessGameBean;
import chessapp.shared.entities.ChessGame;

public class ExploreLatestGamesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public ExploreLatestGamesServlet() {
        super();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ChessGameBean cgb = new ChessGameBean();
		String gameid = request.getParameter("game");
		
		if (gameid == null || gameid.isEmpty()) {
			response.setStatus(200);
			List<ChessGame> games = cgb.getLatestSomeGames(10);
			
			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
	        
			for(ChessGame game : games) {
				JsonObjectBuilder gameBuilder = Json.createObjectBuilder()
						.add("blackPlayer", game.getBlackPlayer() == null ? "" : game.getBlackPlayer())
						.add("whitePlayer", game.getWhitePlayer() == null ? "" : game.getWhitePlayer())
						.add("gameId", game.getChessGameId())
						.add("winner", game.getResult() == null ? "" : game.getResult());
	
			    arrayBuilder.add(gameBuilder.build());
			}
			JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
			objectBuilder.add("latestGames", arrayBuilder);
			
			
	        
			try(Writer writer = new StringWriter()) {
			    Json.createWriter(writer).write(objectBuilder.build());
			    response.getWriter().write(writer.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			response.setStatus(200);
			ChessGame game = cgb.findGame(gameid);
			if (game == null) {
				response.setStatus(404);
				return;
			}
				
			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
	        
			JsonObjectBuilder gameBuilder = Json.createObjectBuilder()
					.add("blackPlayer", game.getBlackPlayer() == null ? "" : game.getBlackPlayer())
					.add("whitePlayer", game.getWhitePlayer() == null ? "" : game.getWhitePlayer())
					.add("gameId", game.getChessGameId())
					.add("winner", game.getResult() == null ? "" : game.getResult())
					.add("startDate", game.getStartDate() == null ? "" : game.getStartDate().toString())
					.add("endDate", game.getEndDate() == null ? "" : game.getEndDate().toString());
			JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
			objectBuilder.add("selectedGame", gameBuilder);
			
			
	        
			try(Writer writer = new StringWriter()) {
			    Json.createWriter(writer).write(objectBuilder.build());
			    response.getWriter().write(writer.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
