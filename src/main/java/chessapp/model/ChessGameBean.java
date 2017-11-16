package chessapp.model;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import chessapp.main.EntityManagerFactorySingleton;
import chessapp.shared.entities.User;

public class ChessGameBean {

	private EntityManager em = EntityManagerFactorySingleton.getEntityManager();
	
	private UserBean userBean;
	
	public void create(User whitePlayer, User blackPlayer) {
		
	}
	
}
