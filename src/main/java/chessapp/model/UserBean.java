package chessapp.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import chessapp.shared.entities.User;

public class UserBean {

	@PersistenceContext(unitName="chessapp")
	private EntityManager em;
	
	public void create(User user) {
		em.getTransaction().begin();
		em.persist(user);
		em.getTransaction().commit();
	}
	
	public void delete(User user) {
		em.getTransaction().begin();
		em.remove(user);
		em.getTransaction().commit();
	}

	public void modify(User user) {
		
	}
	
	public User findUser(int id) {
		return em.find(User.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<User> findAll() {
		Query query = em.createNativeQuery("db.find()", User.class);
		List<User> users = query.getResultList();
		return users;
	}
}
