package com.djr.spelling.app.services.quiz;

import com.djr.spelling.BaseTest;
import com.djr.spelling.Grade;
import com.djr.spelling.Quiz;
import com.djr.spelling.WordLocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;
/**
 * Created by IMac on 2/22/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class QuizScheduleServiceBeanTest extends BaseTest {
	@Mock
	private TypedQuery<WordLocation> wordLocationQuery;
	@Mock
	private TypedQuery<Grade> gradeQuery;
	@Mock
	private EntityManager em;

	@InjectMocks
	private QuizScheduleServiceBean qssb = new QuizScheduleServiceBean();

	@Before
	public void setup()
	throws Exception {
		MockitoAnnotations.initMocks(qssb);
		addIntegerField(qssb, "nationalQuizLimit", 1);
		addLogger(qssb, "log");
	}

	@Test
	public void testGenerateWeeklyNationalQuiz() {
		List<Grade> grades = new ArrayList<>();
		Grade grade = new Grade("4th");
		grade.id = 1;
		grades.add(grade);
		List<WordLocation> wordLocations = new ArrayList<>();
		WordLocation wl = new WordLocation();
		wl.id = 1;
		wordLocations.add(wl);
		Query nativeQuery = mock(Query.class);
		when(em.createNamedQuery("getAllGrades", Grade.class)).thenReturn(gradeQuery);
		when(gradeQuery.getResultList()).thenReturn(grades);
		when(em.createNamedQuery("findWordLocationsByGrade", WordLocation.class)).thenReturn(wordLocationQuery);
		when(wordLocationQuery.getResultList()).thenReturn(wordLocations);
		when(em.createNativeQuery("truncate quizzes")).thenReturn(nativeQuery);
		qssb.generateWeeklyNationalQuiz();
		verify(em, times(1)).persist(any(Quiz.class));
	}

	@Test
	public void testGenerateWeeklyNationalQuizExceptionOccurs() {
		List<Grade> grades = new ArrayList<>();
		Grade grade = new Grade("4th");
		grade.id = 1;
		grades.add(grade);
		List<WordLocation> wordLocations = new ArrayList<>();
		WordLocation wl = new WordLocation();
		wl.id = 1;
		wordLocations.add(wl);
		Query nativeQuery = mock(Query.class);
		when(em.createNamedQuery("getAllGrades", Grade.class)).thenReturn(gradeQuery);
		when(gradeQuery.getResultList()).thenReturn(grades);
		when(em.createNamedQuery("findWordLocationsByGrade", WordLocation.class)).thenReturn(wordLocationQuery);
		when(wordLocationQuery.getResultList()).thenReturn(wordLocations);
		when(em.createNativeQuery("truncate quizzes")).thenReturn(nativeQuery);
		when(nativeQuery.executeUpdate()).thenThrow(new RuntimeException("the unthinkable happens"));
		qssb.generateWeeklyNationalQuiz();
		verify(em, never()).persist(any(Quiz.class));
	}
}
