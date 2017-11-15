package chessapp.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import chessapp.main.EntityManagerFactorySingleton;
import chessapp.shared.entities.User;

public class UserBean {

	private EntityManager em = EntityManagerFactorySingleton.getEntityManager();
	
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
		//em.getTransaction().begin();
		//'User' in query must match the class' name. https://stackoverflow.com/questions/20193581/error-on-compiling-query-the-abstract-schema-type-entity-is-unknown  
		TypedQuery<User> lQuery = em.createQuery("select u from User u", User.class);
		List<User> users = lQuery.getResultList();
		//em.getTransaction().commit();
		//em.close();
		return users;
	}
}
