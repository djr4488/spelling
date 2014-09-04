package com.djr.spelling.app.parent.service;

import com.djr.spelling.ChildUser;
import com.djr.spelling.Location;
import com.djr.spelling.User;
import com.djr.spelling.Word;
import com.djr.spelling.app.exceptions.SpellingException;
import com.djr.spelling.app.parent.restapi.model.UserCreateRequest;
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

	public void createParentAccount(User user)
	throws SpellingException {
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

	public void createChildAccount(ChildUser user)
	throws SpellingException {
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

	public User findParentAccount(String username, String password) {
		return null;
	}

	public void editParentUserSettings(User original, User editted) {

	}

	public List<User> findParentChildren(User user) {
		return null;
	}

	public boolean changeChildPassword(User user) {
		return false;
	}

	public void createOrFindWord(Word word) {

	}
}
