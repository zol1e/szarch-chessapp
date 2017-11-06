package chessapp.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServerInitDestroyHandler implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("Server has been started - ServerInit contextInitialized method called!");
	}

	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("Server stoped - ServerInit contextDestroyed method called!");
	}

}
