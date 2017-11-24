package chessapp.server;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServerInitDestroyHandler implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("Server has been started - ServerInit contextInitialized method called!");
		EntityManagerFactorySingleton.initConnection();
		GlobalSocketRepository.initConnections();
		GameSocketRepository.initConnections();
		PrivateSocketRepository.initConnections();
		
		// Scheduled task for refreshing chessgame times
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new ChessGameRefreshTask(), 0, 3, TimeUnit.SECONDS);
		sce.getServletContext().setAttribute("ChessGameRefreshTask", executor);
	}

	public void contextDestroyed(ServletContextEvent sce) {
		EntityManagerFactorySingleton.closeConnection();
		GlobalSocketRepository.releaseConnections();
		GameSocketRepository.releaseConnections();
		PrivateSocketRepository.releaseConnections();
		ScheduledExecutorService executor =  (ScheduledExecutorService) sce.getServletContext().getAttribute("ChessGameRefreshTask");
		executor.shutdown();
		System.out.println("Server stoped - ServerInit contextDestroyed method called!");
	}

}
