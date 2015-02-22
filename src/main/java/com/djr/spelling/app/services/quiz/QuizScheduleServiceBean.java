package com.djr.spelling.app.services.quiz;

import com.djr.spelling.Grade;
import com.djr.spelling.Quiz;
import com.djr.spelling.WordLocation;
import org.slf4j.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IMac on 2/22/2015.
 */
@Stateless
@Singleton
public class QuizScheduleServiceBean {
	@PersistenceContext(name="SpellingPersistence")
	private EntityManager em;
	@Inject
	private Logger log;
	@Inject
	private Integer nationalQuizLimit;

	@Schedule(second = "0", minute = "0", hour = "0", dayOfWeek = "Sat")
	public void generateWeeklyNationalQuiz() {
		log.info("generateWeeklyNationalQuiz() entered nationalQuizLimit:{}", nationalQuizLimit);
		try {
			//get words for the weekly quiz
			List<Grade> grades = findAllGrades();
			TypedQuery<WordLocation> wordLocationQuery = em.createNamedQuery("findWordLocationsByGrade", WordLocation.class);
			Map<String, List<WordLocation>> gradeToWordLocations = new HashMap<>();
			for (Grade grade : grades) {
				wordLocationQuery.setParameter("schoolGrade", grade);
				wordLocationQuery.setMaxResults(nationalQuizLimit);
				gradeToWordLocations.put(grade.gradeName, wordLocationQuery.getResultList());
			}
			//clear out the words for prior quiz
			em.createNativeQuery("truncate quizzes").executeUpdate();
			//create new quiz
			for (String grade : gradeToWordLocations.keySet()) {
				for (WordLocation wordLocation : gradeToWordLocations.get(grade)) {
					Quiz quiz = new Quiz("National", wordLocation);
					em.persist(quiz);
				}
			}
		} catch (Exception ex) {
			log.error("generateWeeklyNationalQuizzes() ex:{}", ex);
		}
		log.debug("generateWeeklyNationalQuizzes() completed");
	}

	public List<Grade> findAllGrades() {
		TypedQuery<Grade> query = em.createNamedQuery("getAllGrades", Grade.class);
		return query.getResultList();
	}
}
