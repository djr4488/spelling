package com.djr.spelling.app.services.quiz;

import com.djr.spelling.*;
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
	private TypedQuery<State> stateQuery;
	@Mock
	private EntityManager em;

	@InjectMocks
	private QuizScheduleServiceBean qssb = new QuizScheduleServiceBean();

	@Before
	public void setup()
	throws Exception {
		MockitoAnnotations.initMocks(qssb);
		addIntegerField(qssb, "nationalQuizLimit", 1);
		addIntegerField(qssb, "stateQuizLimit", 1);
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
		when(em.createNativeQuery("delete * from quizzes where quiz_type = 'National'")).thenReturn(nativeQuery);
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
		when(em.createNativeQuery("delete * from quizzes where quiz_type = 'National'")).thenReturn(nativeQuery);
		when(nativeQuery.executeUpdate()).thenThrow(new RuntimeException("the unthinkable happens"));
		qssb.generateWeeklyNationalQuiz();
		verify(em, never()).persist(any(Quiz.class));
	}

	@Test
	public void testGenerateWeeklyStateAndGradeQuiz() {
		List<Grade> grades = new ArrayList<>();
		Grade grade = new Grade("4th");
		grade.id = 1;
		grades.add(grade);
		List<State> states = new ArrayList<>();
		State state = new State("KS");
		state.id = 1;
		states.add(state);
		List<WordLocation> wordLocations = new ArrayList<>();
		WordLocation wl = new WordLocation();
		wl.id = 1;
		wordLocations.add(wl);
		Query nativeQuery = mock(Query.class);
		when(em.createNamedQuery("getAllGrades", Grade.class)).thenReturn(gradeQuery);
		when(gradeQuery.getResultList()).thenReturn(grades);
		when(em.createNamedQuery("getAllStates", State.class)).thenReturn(stateQuery);
		when(stateQuery.getResultList()).thenReturn(states);
		when(em.createNamedQuery("findWordLocationsByStateAndGrade", WordLocation.class)).thenReturn(wordLocationQuery);
		when(wordLocationQuery.getResultList()).thenReturn(wordLocations);
		when(em.createNativeQuery("delete * from quizzes where quiz_type = 'State And Grade'")).thenReturn(nativeQuery);
		qssb.generateWeeklyStateQuiz();
		verify(em, times(1)).persist(any(Quiz.class));
	}

	@Test
	public void testGenerateWeeklyStateAndGradeExceptionOccurs() {
		List<Grade> grades = new ArrayList<>();
		Grade grade = new Grade("4th");
		grade.id = 1;
		grades.add(grade);
		List<State> states = new ArrayList<>();
		State state = new State("KS");
		state.id = 1;
		states.add(state);
		List<WordLocation> wordLocations = new ArrayList<>();
		WordLocation wl = new WordLocation();
		wl.id = 1;
		wordLocations.add(wl);
		Query nativeQuery = mock(Query.class);
		when(em.createNamedQuery("getAllGrades", Grade.class)).thenReturn(gradeQuery);
		when(gradeQuery.getResultList()).thenReturn(grades);
		when(em.createNamedQuery("getAllStates", State.class)).thenReturn(stateQuery);
		when(stateQuery.getResultList()).thenReturn(states);
		when(em.createNamedQuery("findWordLocationsByStateAndGrade", WordLocation.class)).thenReturn(wordLocationQuery);
		when(wordLocationQuery.getResultList()).thenReturn(wordLocations);
		when(em.createNativeQuery("delete * from quizzes where quiz_type = 'State And Grade'")).thenThrow(new RuntimeException("blew up"));
		qssb.generateWeeklyStateQuiz();
		verify(em, never()).persist(any(Quiz.class));
	}
}
