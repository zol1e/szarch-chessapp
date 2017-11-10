package chessapp.model;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import chessapp.shared.entities.User;

public class ChessGameBean {

	@PersistenceContext(unitName="chessapp")
	private EntityManager entityManager;
	
	private UserBean userBean;
	
	public void create(User whitePlayer, User blackPlayer) {
		
	}
	
}
