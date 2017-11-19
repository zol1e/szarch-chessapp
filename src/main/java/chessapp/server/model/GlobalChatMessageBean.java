package chessapp.server.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;



import chessapp.server.EntityManagerFactorySingleton;
import chessapp.shared.entities.GlobalChatMessage;

public class GlobalChatMessageBean{

	private EntityManager em = EntityManagerFactorySingleton.getEntityManager();
	
	public void create(GlobalChatMessage msg) {
		em.getTransaction().begin();
		em.persist(msg);
		em.getTransaction().commit();
	}
	
	public void delete(GlobalChatMessage msg) {
		em.getTransaction().begin();
		em.remove(msg);
		em.getTransaction().commit();
	}
	
	public GlobalChatMessage findMessage(String id) {
		return em.find(GlobalChatMessage.class, id);
	}
	
	public List<GlobalChatMessage> findBySrcName(String srcName) {
		TypedQuery<GlobalChatMessage> query = em.createQuery("select u from GlobalChatMessage u where u.srcUserName = :uname", GlobalChatMessage.class)
				.setParameter("uname", srcName);
		return query.getResultList();
	}

}
