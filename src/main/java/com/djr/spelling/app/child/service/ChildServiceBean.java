package com.djr.spelling.app.child.service;


import com.djr.spelling.ChildUser;
import com.djr.spelling.app.child.model.QuizWordWrapper;
import com.djr.spelling.app.exceptions.SpellingException;
import org.slf4j.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Created by IMac on 9/1/2014.
 */
public class ChildServiceBean {
	@Inject
	private Logger log;
	@PersistenceContext(name = "jdbc/SpellingPersistence")
	private EntityManager em;

	public ChildUser findChildUser(String username, String password, String trackingId)
	throws SpellingException {
		log.debug("findChildUser() trackingId:{}, username:{}", trackingId, username);
		try {
			TypedQuery<ChildUser> query = em.createNamedQuery("findChildUser", ChildUser.class);
			query.setParameter("username", username);
			query.setParameter("password", password);
			return query.getSingleResult();
		} catch (NoResultException nrEx) {
			log.debug("findChildUser() no results found");
			throw new SpellingException("No child found");
		}
	}

	public QuizWordWrapper getQuiz(String timeType, String locationType, Integer childId, String trackingId)
	throws SpellingException {
		QuizWordWrapper quizWordWrapper = null;
		try {
			return quizWordWrapper;
		} catch (NoResultException nrEx) {
			log.debug("getQuiz() no results found");
			throw new SpellingException("Something went wrong generating quiz");
		}
	}
}
