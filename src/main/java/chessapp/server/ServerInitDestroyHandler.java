package chessapp.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServerInitDestroyHandler implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("Server has been started - ServerInit contextInitialized method called!");
		EntityManagerFactorySingleton.initConnection();
		// persistence.xml-ben megadott unit-name kell a paraméterbe
		/*EntityManagerFactory factory = Persistence.createEntityManagerFactory("chessapp");
		EntityManager entityManager = factory.createEntityManager();
		
		User user = new User();
		user.setNickName("zol1e");
		user.setFirstName("Zoltán");
		user.setLastName("Szegedi");
		user.setJoinDate(new Date());
		
		entityManager.getTransaction().begin();
		entityManager.merge(user);
		entityManager.getTransaction().commit();*/
	}

	public void contextDestroyed(ServletContextEvent sce) {
		EntityManagerFactorySingleton.closeConnection();
		System.out.println("Server stoped - ServerInit contextDestroyed method called!");
	}

}
