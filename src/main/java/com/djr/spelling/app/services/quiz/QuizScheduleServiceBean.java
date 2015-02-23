package com.djr.spelling.app.services.quiz;

import com.djr.spelling.Grade;
import com.djr.spelling.Quiz;
import com.djr.spelling.State;
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
	@Inject
	private Integer stateQuizLimit;

	@Schedule(second = "0", minute = "0", hour = "0", dayOfWeek = "Sat")
	public void generateWeeklyNationalQuiz() {
		log.info("generateWeeklyNationalQuiz() entered nationalQuizLimit:{}", nationalQuizLimit);
		try {
			//get words for the weekly quiz
			List<Grade> grades = findAllGrades();
			TypedQuery<WordLocation> wordLocationQuery = em.createNamedQuery("findWordLocationsByGrade",
					WordLocation.class);
			Map<String, List<WordLocation>> gradeToWordLocations = new HashMap<>();
			for (Grade grade : grades) {
				wordLocationQuery.setParameter("schoolGrade", grade);
				wordLocationQuery.setMaxResults(nationalQuizLimit);
				gradeToWordLocations.put(grade.gradeName, wordLocationQuery.getResultList());
			}
			//clear out the words for prior quiz
			em.createNativeQuery("delete * from quizzes where quiz_type = 'National'").executeUpdate();
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

	@Schedule(second = "0", minute = "0", hour = "0", dayOfWeek = "Sat")
	public void generateWeeklyStateQuiz() {
		log.info("generateWeeklyStateQuiz() entered stateQuizLimit:{}", stateQuizLimit);
		try {
			List<Grade> grades = findAllGrades();
			List<State> states = findAllStates();
			TypedQuery<WordLocation> query = em.createNamedQuery("findWordLocationsByStateAndGrade", WordLocation.class);
			Map<String, List<WordLocation>> stateAndGradeToWordLocations = new HashMap<>();
			for (State state : states) {
				for (Grade grade: grades) {
					query.setParameter("state", state);
					query.setParameter("grade", grade);
					query.setMaxResults(stateQuizLimit);
					stateAndGradeToWordLocations.put(state.stateAbbr + grade.gradeName, query.getResultList());
				}
			}
			em.createNativeQuery("delete * from quizzes where quiz_type = 'State And Grade'").executeUpdate();
			for (String stateAndGrade : stateAndGradeToWordLocations.keySet()) {
				for (WordLocation wordLocation : stateAndGradeToWordLocations.get(stateAndGrade)) {
					Quiz quiz = new Quiz("State And Grade", wordLocation);
					em.persist(quiz);
				}
			}
		} catch (Exception ex) {
			log.error("generateWeeklyStateQuiz() ex:{}", ex);
		}
		log.debug("generateWeeklyStateQuiz() completed");
	}

	public List<Grade> findAllGrades() {
		TypedQuery<Grade> query = em.createNamedQuery("getAllGrades", Grade.class);
		return query.getResultList();
	}

	public List<State> findAllStates() {
		TypedQuery<State> query = em.createNamedQuery("getAllStates", State.class);
		return query.getResultList();
	}
}
