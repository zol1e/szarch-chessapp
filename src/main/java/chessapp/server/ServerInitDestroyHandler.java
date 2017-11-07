package chessapp.server;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import chessapp.shared.entities.User;

public class ServerInitDestroyHandler implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("Server has been started - ServerInit contextInitialized method called!");
		
		// persistence.xml-ben megadott unit-name kell a paraméterbe
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("chessapp");
		EntityManager entityManager = factory.createEntityManager();
		
		User user = new User();
		user.setNickName("zol1e");
		user.setFirstName("Zoltán");
		user.setLastName("Szegedi");
		user.setJoinDate(new Date());
		
		entityManager.getTransaction().begin();
		entityManager.merge(user);
		entityManager.getTransaction().commit();
	}

	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("Server stoped - ServerInit contextDestroyed method called!");
	}

}
