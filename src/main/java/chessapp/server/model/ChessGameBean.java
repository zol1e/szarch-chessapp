package chessapp.server.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import chessapp.server.EntityManagerFactorySingleton;
import chessapp.shared.entities.ChessGame;
import chessapp.shared.entities.PrivateChatMessage;
import chessapp.shared.entities.User;

public class ChessGameBean {

	private EntityManager em = EntityManagerFactorySingleton.getEntityManager();
	
	private UserBean userBean;
	
	public void create(String whitePlayer, String blackPlayer) {
		em.getTransaction().begin();
		em.persist(new ChessGame(blackPlayer, whitePlayer));
		em.getTransaction().commit();
	}
	
	public void create(ChessGame game) {
		em.getTransaction().begin();
		em.persist(game);
		em.getTransaction().commit();
	}
	
	public ChessGame findGame(String id) {
		System.out.println("looking for game with id: " + id);
		TypedQuery<ChessGame> query = em.createQuery("select u from ChessGame u where u.chessGameId = :id", ChessGame.class)
				.setParameter("id", id);
		List<ChessGame> games = query.getResultList();
		System.out.println("found: " + games.size() + " pieces");
		if (games.isEmpty())
			return null;
		return games.get(0);
	}
	
	
	
	public void delete(ChessGame game) {
		em.getTransaction().begin();
		em.remove(game);
		em.getTransaction().commit();
	}
	public void update(ChessGame newGame) {
		delete(em.find(ChessGame.class, newGame.getChessGameId()));
		create(newGame);
	}

	public ChessGame getOngoingBySomePlayer(String userName) {
		TypedQuery<ChessGame> query = em.createQuery("select u from ChessGame u where u.endDate = null and (u.whitePlayerName = :uname or u.blackPlayerName = :uname)", ChessGame.class)
				.setParameter("uname", userName);
		List<ChessGame> games = query.getResultList();
		if (games.isEmpty())
			return null;
		return games.get(0);
	}

	public List<ChessGame> getHalfEmptyGames() {
		TypedQuery<ChessGame> query = em.createQuery("select u from ChessGame u where u.whitePlayerName = null or u.blackPlayerName = null", ChessGame.class);
		return query.getResultList();
	}
	
	public List<ChessGame> getLatestSomeGames(int some) {
		TypedQuery<ChessGame> query = em.createQuery("select u from ChessGame u where u.endDate != null order by u.endDate desc", ChessGame.class).setMaxResults(some);
		return query.getResultList();
	}
	

	
}
