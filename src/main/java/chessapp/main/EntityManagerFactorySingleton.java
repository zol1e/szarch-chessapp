package chessapp.main;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerFactorySingleton {
    private static volatile EntityManagerFactory instance = null;

    private EntityManagerFactorySingleton() {}

    public static void initConnection() {
    	if (instance == null) {
            instance = Persistence.createEntityManagerFactory("chessapp");
        }
    }
    
    public static void closeConnection() {
    	if (instance != null && instance.isOpen()) {
            instance.close();
        }
    }
    
    public static EntityManager getEntityManager() {
        if (instance == null) {
            instance = Persistence.createEntityManagerFactory("chessapp");
        }
        return instance.createEntityManager();
    }
}
