package chessapp.model;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import chessapp.shared.entities.User;

@Stateless
public class ChessGameBean {

	@PersistenceContext(unitName="chessapp")
	private EntityManager entityManager;
	
	@EJB
	private UserBean userBean;
	
	public void create(User whitePlayer, User blackPlayer) {
		
	}
	
}
