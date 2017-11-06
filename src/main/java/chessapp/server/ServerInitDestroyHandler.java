package chessapp.server;

import java.util.Iterator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.bson.Document;

import com.mongodb.client.FindIterable;

import database.MongoInstance;

public class ServerInitDestroyHandler implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("Server has been started - ServerInit contextInitialized method called!");
		
		MongoInstance mongoInstance = new MongoInstance("localhost", 27017, "chessapp");
		
		Document testDocument = new Document()
				.append("name", "First game on the server")
				.append("white", "Zoli")
				.append("black", "Peti")
				.append("result", "1-0");
		mongoInstance.insert("ChessGames", testDocument);
		
		sce.getServletContext().setAttribute("mongo", mongoInstance);
	}

	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("Server stoped - ServerInit contextDestroyed method called!");
		MongoInstance mongoInstance = (MongoInstance) sce.getServletContext().getAttribute("mongo");
		
		FindIterable<Document> documents = mongoInstance.findAll("ChessGames");
		Iterator<Document> documentIterator = documents.iterator();
		while(documentIterator.hasNext()) {
			Document document = documentIterator.next();
			System.out.println(document.toString());
		}
		
		mongoInstance.closeConnection();
	}

}
