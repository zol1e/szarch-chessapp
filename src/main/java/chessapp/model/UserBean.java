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
		TypedQuery<User> query = em.createQuery("select u from User u where u.userName = :uname", User.class)
				.setParameter("uname", name);
		List<User> users = query.getResultList();
		if (users.isEmpty())
			return null;
		return users.get(0);
	}
	
	public User findByNameNPassword(String name, String password) {
		TypedQuery<User> query = em.createQuery("select u from User u where u.userName = :uname and u.password = :passw", User.class)
				.setParameter("uname", name)
				.setParameter("passw", password);
		List<User> users = query.getResultList();
		if (users.isEmpty())
			return null;
		return users.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public List<User> findAll() {
		TypedQuery<User> lQuery = em.createQuery("select u from User u", User.class);
		List<User> users = lQuery.getResultList();
		return users;
	}
}
