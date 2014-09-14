package com.djr.spelling.app.child.service;


import com.djr.spelling.ChildUser;
import com.djr.spelling.Quiz;
import com.djr.spelling.QuizWord;
import com.djr.spelling.WordLocation;
import com.djr.spelling.app.child.model.QuizWordModel;
import com.djr.spelling.app.child.model.QuizWordWrapper;
import com.djr.spelling.app.exceptions.SpellingException;
import org.slf4j.Logger;
import javax.inject.Inject;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

	public QuizWordWrapper createQuiz(String timeType, String locationType, Integer childId, String trackingId)
	throws SpellingException {
		log.debug("getQuiz() timeType:{}, locationType:{}, childId:{}, trackingId:{}", timeType, locationType,
				childId, trackingId);
		String query = getQuizQuery(timeType, locationType, trackingId);
		try {
			TypedQuery<WordLocation> wlQuery = em.createQuery(query, WordLocation.class);
			List<WordLocation> wordLocations = wlQuery.getResultList();
			Quiz quiz = new Quiz();
			QuizWordWrapper qww;
			List<QuizWordModel> qwmList = new ArrayList<>();
			for (WordLocation wl : wordLocations) {
				QuizWord qw = new QuizWord(quiz, wl);
				em.persist(qw);
				//todo: find sentence
				String sentence = null;
				QuizWordModel qwm = new QuizWordModel(qw.id, wl.word.word, sentence);
				qwmList.add(qwm);
			}
			em.persist(quiz);
			return new QuizWordWrapper(qwmList, quiz.id);
		} catch (NoResultException nrEx) {
			log.debug("getQuiz() no results found");
			throw new SpellingException("Something went wrong generating quiz");
		}
	}

	protected String getQuizQuery(String timeType, String locationType, String trackingId) {
		String query = "select wordLocation from WordLocation wordLocation ";
		String locationCriteria = getLocationCriteria(locationType, trackingId);
		String timeCriteria = getTimeCriteria(timeType, trackingId);
		if (locationCriteria != null || timeCriteria != null) {
			query += "where ";
		}
		query += locationCriteria;
		if (locationCriteria != null && timeCriteria != null) {
			query += "and ";
		}
		query += timeCriteria;
		return query;
	}

	protected String getLocationCriteria(String locationType, String trackingId) {
		log.debug("getLocationCriteria() locationType:{}, trackingId:{}", locationType, trackingId);
		String locationCriteria = null;
		switch (locationType) {
			case "full": {
				locationCriteria += "wordLocation.location.school = child.location.school and ";
			}
			case "city": {
				locationCriteria += "wordLocation.location.city = child.location.city and ";
			}
			case "state": {
				locationCriteria += "wordLocation.location.state = child.location.state ";
				break;
			}
			default: {
				locationCriteria = null;
			}
		}
		return locationCriteria;
	}

	protected String getTimeCriteria(String timeType, String trackingId) {
		log.debug("getTimeCriteria() timeType:{}, trackingId:{}", timeType, trackingId);
		String timeCriteria = null;
		switch (timeType) {
			case "weekly": {
				timeCriteria += "wordLocation.location.week.weekStart >= :today and wordLocation.location.week.weekEnd <= :today " +
						"and ";
			}
			case "gradeYear": {
				timeCriteria += "wordLocation.grade = child.grade";
				break;
			}
			default: {
				timeCriteria = null;
			}
		}
		return timeCriteria;
	}
}
