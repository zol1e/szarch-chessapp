package chessapp.client.main;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chessapp.server.service.ExploreLatestGamesService;

public class ExploreLatestGamesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public ExploreLatestGamesServlet() {
        super();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String gameId = request.getParameter("game");
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
		ExploreLatestGamesService exploreService = new ExploreLatestGamesService();

		int status = exploreService.makeResponse(gameId, objectBuilder);
		response.setStatus(status);
		if (status == 200)
			JsonResponseWriterHelper.writeResponse(response, objectBuilder);
		else
			response.sendError(status);
	}
	

}
