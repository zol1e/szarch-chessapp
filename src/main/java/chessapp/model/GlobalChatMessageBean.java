package chessapp.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import chessapp.main.EntityManagerFactorySingleton;
import chessapp.shared.entities.GlobalChatMessage;

public class GlobalChatMessageBean {
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
	
	/*public List<GlobalChatMessage> findByDstName(String dstName) {
		TypedQuery<GlobalChatMessage> query = em.createQuery("select u from GlobalChatMessage u where u.dstUserName = :uname", GlobalChatMessage.class)
				.setParameter("uname", dstName);
		return query.getResultList();
		
	}*/
	

	

}
