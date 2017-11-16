package chessapp.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import chessapp.main.EntityManagerFactorySingleton;
import chessapp.shared.entities.User;
import chessapp.shared.entities.UserLogin;

public class LoginBean {
	private EntityManager em = EntityManagerFactorySingleton.getEntityManager();
	
	public void create(UserLogin user) {
		em.getTransaction().begin();
		em.persist(user);
		em.getTransaction().commit();
	}
	
	public void delete(UserLogin user) {
		em.getTransaction().begin();
		em.remove(user);
		em.getTransaction().commit();
	}

	public void modify(UserLogin newer) {
		if (newer.getUserName() == null)
			return;
		
		UserLogin old = findByName(newer.getUserName());
		if (old != null) {
			delete(old);
		}
		create(newer);
	}
	
	public UserLogin findUser(String id) {
		return em.find(UserLogin.class, id);
	}
	
	public UserLogin findByName(String name) {
		return em.createQuery("select u from UserLogin u where u.userName = :uname", UserLogin.class)
				.setParameter("uname", name)
				.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<UserLogin> findAll() {
		TypedQuery<UserLogin> lQuery = em.createQuery("select u from UserLogin u", UserLogin.class);
		List<UserLogin> users = lQuery.getResultList();
		return users;
	}
}
