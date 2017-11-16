package chessapp.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import chessapp.main.EntityManagerFactorySingleton;
import chessapp.shared.entities.User;
import chessapp.shared.entities.UserLogin;

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
	
	public User findUser(String id) {
		return em.find(User.class, id);
	}
	public User findByName(String name) {
		return em.createQuery("select u from User u where u.userName = :uname", User.class)
				.setParameter("uname", name)
				.getSingleResult();
	}
	public User findByNameNPassword(String name, String password) {
		return em.createQuery("select u from User u where u.userName = :uname and u.password = :passw", User.class)
				.setParameter("uname", name)
				.setParameter("passw", password)
				.getSingleResult();
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
