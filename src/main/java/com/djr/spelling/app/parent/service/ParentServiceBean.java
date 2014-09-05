package com.djr.spelling.app.parent.service;

import com.djr.spelling.ChildUser;
import com.djr.spelling.User;
import com.djr.spelling.Word;
import com.djr.spelling.app.exceptions.SpellingException;
import org.slf4j.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by IMac on 9/1/2014.
 */
@Stateless
public class ParentServiceBean {
	@PersistenceContext(name = "SpellingPersistence")
	private EntityManager em;
	@Inject
	private Logger log;

	public void createParentAccount(User user, String trackingId)
	throws SpellingException {
		log.debug("createParentAccount() user:{}, trackingId:{}", user, trackingId);
		try {
			TypedQuery<User> query = em.createNamedQuery("findExistingUserByEmailAddress", User.class);
			query.setParameter("emailAddress", user.emailAddress);
			query.getSingleResult();
			throw new SpellingException("Account already exists");
		} catch (NoResultException nrEx) {
			log.debug("createParentAccount() no results found, so good to continue");
		}
		em.persist(user);
	}

	public void createChildAccount(ChildUser user, String trackingId)
	throws SpellingException {
		log.debug("createChildAccount() user:{}, trackingId:{}", user, trackingId);
		try {
			TypedQuery<ChildUser> query = em.createNamedQuery("findExistingChildUserByUsername", ChildUser.class);
			query.setParameter("username", user.username);
			query.getSingleResult();
			throw new SpellingException("Account already exists");
		} catch (NoResultException nrEx) {
			log.debug("createChildAccount() no results found, so continuing");
		}
		em.persist(user);
	}

	public User findParentAccount(User user, String trackingId)
	throws SpellingException {
		log.debug("findParentAccount() user:{}, trackingId:{}", user, trackingId);
		try {
			TypedQuery<User> query = em.createNamedQuery("findExistingUserByUserNameAndPassword", User.class);
			query.setParameter("username", user.username);
			query.setParameter("password", user.password);
			return query.getSingleResult();
		} catch (NoResultException nrEx) {
			log.debug("findParentAccount() no user account found trackingId:{}", trackingId);
			throw new SpellingException("No user account found or incorrect username / password");
		}
	}

	public void editParentSettings(User original, User editted, String trackingId) {

	}

	public List<User> findParentChildren(User user, String trackingId) {
		return null;
	}

	public boolean changeChildPassword(User user, String trackingId) {
		return false;
	}

	public void createOrFindWord(Word word, String trackingId) {

	}
}
