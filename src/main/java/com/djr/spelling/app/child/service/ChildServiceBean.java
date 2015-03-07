package com.djr.spelling.app.child.service;


import com.djr.spelling.*;
import com.djr.spelling.app.child.ChildConstants;
import com.djr.spelling.app.child.exceptions.ChildApiException;
import com.djr.spelling.app.child.model.QuizWordModel;
import com.djr.spelling.app.child.model.QuizWordWrapper;
import com.djr.spelling.app.exceptions.AuthException;
import com.djr.spelling.app.exceptions.SpellingException;
import com.djr.spelling.app.services.auth.AuthConstants;
import org.slf4j.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IMac on 9/1/2014.
 */
@Stateless
public class ChildServiceBean {
	@Inject
	private Logger log;
	@PersistenceContext(name = "SpellingPersistence")
	private EntityManager em;

	public ChildUser findChildUser(String username, String password, String trackingId)
	throws AuthException {
		log.debug("findChildUser() trackingId:{}, username:{}", trackingId, username);
		try {
			TypedQuery<ChildUser> query = em.createNamedQuery("findChildUser", ChildUser.class);
			query.setParameter("username", username);
			query.setParameter("password", password);
			return query.getSingleResult();
		} catch (NoResultException nrEx) {
			log.debug("findChildUser() no results found");
			throw new AuthException(AuthConstants.USER_NOT_FOUND);
		} catch (Exception ex) {
			throw new AuthException(AuthConstants.GENERAL_AUTH);
		}
	}

	public void getWeeklyNationalQuiz() {

	}

	public void saveWeeklyNationalQuizResults() {

	}
}
