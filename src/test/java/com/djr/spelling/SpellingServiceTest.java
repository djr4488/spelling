package com.djr.spelling;

import com.djr.spelling.app.Constants;
import com.djr.spelling.app.exceptions.SpellingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import static org.mockito.Mockito.*;

/**
 * Created by IMac on 2/21/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class SpellingServiceTest extends BaseTest {
	@Mock
	private TypedQuery<Location> locationQuery;
	@Mock
	private TypedQuery<City> cityQuery;
	@Mock
	private TypedQuery<State> stateQuery;
	@Mock
	private TypedQuery<School> schoolQuery;
	@Mock
	private TypedQuery<Grade> gradeQuery;
	@Mock
	private EntityManager em;

	@InjectMocks
	private SpellingService spellingService = new SpellingService();

	@Before
	public void setup()
	throws Exception {
		addLogger(spellingService, "log");
	}

	@Test
	public void testCreateOrFindLocationWhenLocationExists() {
		Location location = mock(Location.class);
		when(em.createNamedQuery("findLocation", Location.class)).thenReturn(locationQuery);
		when(locationQuery.getSingleResult()).thenReturn(location);
		try {
			spellingService.createOrFindLocation(location);
			verify(em, never()).persist(location);
		} catch (SpellingException spEx) {
			fail("did not expect any spelling exception here");
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindLocationWhenLocationDoesNotExist() {
		Location location = mock(Location.class);
		when(em.createNamedQuery("findLocation", Location.class)).thenReturn(locationQuery);
		when(locationQuery.getSingleResult()).thenThrow(new NoResultException("no results"));
		try {
			spellingService.createOrFindLocation(location);
			verify(em, times(1)).persist(location);
		} catch (SpellingException spEx) {
			fail("did not expect any spelling exception here");
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindLocationWhenGeneralException() {
		Location location = mock(Location.class);
		when(em.createNamedQuery("findLocation", Location.class)).thenReturn(locationQuery);
		when(locationQuery.getSingleResult()).thenThrow(new RuntimeException("no results"));
		try {
			spellingService.createOrFindLocation(location);
			verify(em, never()).persist(location);
		} catch (SpellingException spEx) {
			assertEquals(Constants.CREATE_OR_FIND_LOCATION_FAILED, spEx.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindCityWhenExists() {
		City city = mock(City.class);
		when(em.createNamedQuery("findCity", City.class)).thenReturn(cityQuery);
		when(cityQuery.getSingleResult()).thenReturn(city);
		try {
			spellingService.createOrFindCity(city);
			verify(em, never()).persist(city);
		} catch (SpellingException spEx) {
			fail("did not expect any spelling exception here");
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindCityWhenNoResults() {
		City city = mock(City.class);
		when(em.createNamedQuery("findCity", City.class)).thenReturn(cityQuery);
		when(cityQuery.getSingleResult()).thenThrow(new NoResultException("no results"));
		try {
			spellingService.createOrFindCity(city);
			verify(em, times(1)).persist(city);
		} catch (SpellingException spEx) {
			fail("did not expect any spelling exception here");
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindCityWhenGeneralException() {
		City city = mock(City.class);
		when(em.createNamedQuery("findCity", City.class)).thenReturn(cityQuery);
		when(cityQuery.getSingleResult()).thenThrow(new RuntimeException("general exception"));
		try {
			spellingService.createOrFindCity(city);
			verify(em, times(1)).persist(city);
		} catch (SpellingException spEx) {
			assertEquals(Constants.CREATE_OR_FIND_CITY_FAILED, spEx.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindStateWhenExists() {
		State state = mock(State.class);
		when(em.createNamedQuery("findState", State.class)).thenReturn(stateQuery);
		when(stateQuery.getSingleResult()).thenReturn(state);
		try {
			spellingService.createOrFindState(state);
			verify(em, never()).persist(state);
		} catch (SpellingException spEx) {
			fail("did not expect any spelling exception here");
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindStateWhenNoResults() {
		State state = mock(State.class);
		when(em.createNamedQuery("findState", State.class)).thenReturn(stateQuery);
		when(stateQuery.getSingleResult()).thenThrow(new NoResultException("no results"));
		try {
			spellingService.createOrFindState(state);
			verify(em, times(1)).persist(state);
		} catch (SpellingException spEx) {
			fail("did not expect any spelling exception here");
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindStateWhenGeneralException() {
		State state = mock(State.class);
		when(em.createNamedQuery("findState", State.class)).thenReturn(stateQuery);
		when(stateQuery.getSingleResult()).thenThrow(new RuntimeException("general exception"));
		try {
			spellingService.createOrFindState(state);
			verify(em, never()).persist(state);
		} catch (SpellingException spEx) {
			assertEquals(Constants.CREATE_OR_FIND_STATE_FAILED, spEx.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindSchoolWhenExists() {
		School school = mock(School.class);
		when(em.createNamedQuery("findSchool", School.class)).thenReturn(schoolQuery);
		when(schoolQuery.getSingleResult()).thenReturn(school);
		try {
			spellingService.createOrFindSchool(school);
			verify(em, never()).persist(school);
		} catch (SpellingException spEx) {
			fail("did not expect any spelling exception here");
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindSchoolWhenNoResults() {
		School school = mock(School.class);
		when(em.createNamedQuery("findSchool", School.class)).thenReturn(schoolQuery);
		when(schoolQuery.getSingleResult()).thenThrow(new NoResultException("no results"));
		try {
			spellingService.createOrFindSchool(school);
			verify(em, times(1)).persist(school);
		} catch (SpellingException spEx) {
			fail("did not expect any spelling exception here");
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindSchoolWhenGeneralException() {
		School school = mock(School.class);
		when(em.createNamedQuery("findSchool", School.class)).thenReturn(schoolQuery);
		when(schoolQuery.getSingleResult()).thenThrow(new RuntimeException("general exception"));
		try {
			spellingService.createOrFindSchool(school);
			verify(em, never()).persist(school);
		} catch (SpellingException spEx) {
			assertEquals(Constants.CREATE_OR_FIND_SCHOOL_FAILED, spEx.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindGradeWhenExists() {
		Grade grade = mock(Grade.class);
		when(em.createNamedQuery("findGrade", Grade.class)).thenReturn(gradeQuery);
		when(gradeQuery.getSingleResult()).thenReturn(grade);
		try {
			spellingService.createOrFindGrade(grade);
			verify(em, never()).persist(grade);
		} catch (SpellingException spEx) {
			fail("did not expect any spelling exception here");
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindGradeWhenNoResults() {
		Grade grade = mock(Grade.class);
		when(em.createNamedQuery("findGrade", Grade.class)).thenReturn(gradeQuery);
		when(gradeQuery.getSingleResult()).thenThrow(new NoResultException("no results"));
		try {
			spellingService.createOrFindGrade(grade);
			verify(em, times(1)).persist(grade);
		} catch (SpellingException spEx) {
			fail("did not expect any spelling exception here");
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindGradeWhenGeneralException() {
		Grade grade = mock(Grade.class);
		when(em.createNamedQuery("findGrade", Grade.class)).thenReturn(gradeQuery);
		when(gradeQuery.getSingleResult()).thenThrow(new RuntimeException("general exception"));
		try {
			spellingService.createOrFindGrade(grade);
			verify(em, never()).persist(grade);
		} catch (SpellingException spEx) {
			assertEquals(Constants.CREATE_OR_FIND_GRADE_FAILED, spEx.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}
}
