package chessapp.server.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import chessapp.server.EntityManagerFactorySingleton;
import chessapp.shared.entities.PrivateChatMessage;

public class PrivateChatMessageBean {

	private EntityManager em = EntityManagerFactorySingleton.getEntityManager();
	
	public void create(PrivateChatMessage msg) {
		em.getTransaction().begin();
		em.persist(msg);
		em.getTransaction().commit();
	}
	
	public void delete(PrivateChatMessage msg) {
		em.getTransaction().begin();
		em.remove(msg);
		em.getTransaction().commit();
	}
	
	public PrivateChatMessage findMessage(String id) {
		return em.find(PrivateChatMessage.class, id);
	}
	
	public List<PrivateChatMessage> findBySrcName(String srcName) {
		TypedQuery<PrivateChatMessage> query = em.createQuery("select u from PrivateChatMessage u where u.srcUserName = :uname", PrivateChatMessage.class)
				.setParameter("uname", srcName);
		return query.getResultList();
	}
	
	public List<PrivateChatMessage> findByGameId(String gameId) {
		TypedQuery<PrivateChatMessage> query = em.createQuery("select u from PrivateChatMessage u where u.gameId = :gameid", PrivateChatMessage.class)
				.setParameter("gameid", gameId);
		return query.getResultList();
	}
}
