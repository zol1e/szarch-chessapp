package chessapp.server.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import chessapp.server.EntityManagerFactorySingleton;
import chessapp.shared.entities.ChessGame;
import chessapp.shared.entities.PrivateChatMessage;
import chessapp.shared.entities.User;

public class ChessGameBean {

	private EntityManager em = EntityManagerFactorySingleton.getEntityManager();
	
	private UserBean userBean;
	
	public void create(User whitePlayer, User blackPlayer) {
		
	}

	public ChessGame getOngoingBySomePlayer(String userName) {
		TypedQuery<ChessGame> query = em.createQuery("select u from ChessGame u where u.endDate = null and (u.whitePlayerName = :uname or u.blackPlayerName = :uname)", ChessGame.class)
				.setParameter("uname", userName);
		List<ChessGame> games = query.getResultList();
		if (games.isEmpty())
			return null;
		return games.get(0);
	}
	
}
