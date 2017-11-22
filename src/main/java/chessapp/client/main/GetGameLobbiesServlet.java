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

public class GetGameLobbiesServlet  extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			ChessGameBean cgb = new ChessGameBean();
			response.setStatus(200);
			List<ChessGame> games = cgb.getHalfEmptyGames();
			
			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            
			for(ChessGame game : games) {
				JsonObjectBuilder gameBuilder = Json.createObjectBuilder()
						.add("blackPlayer", game.getBlackPlayer() == null ? "" : game.getBlackPlayer())
						.add("whitePlayer", game.getWhitePlayer() == null ? "" : game.getWhitePlayer())
						.add("game", game.getChessGameId());

			    arrayBuilder.add(gameBuilder.build());
			}
			JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
			objectBuilder.add("gameLobbies", arrayBuilder);
			
			
	        
			try(Writer writer = new StringWriter()) {
			    Json.createWriter(writer).write(objectBuilder.build());
			    response.getWriter().write(writer.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
