package com.djr.spelling.app.parent.service;

import com.djr.spelling.*;
import com.djr.spelling.app.exceptions.AuthException;
import com.djr.spelling.app.parent.ParentApiConstants;
import com.djr.spelling.app.parent.exceptions.ParentApiException;
import com.djr.spelling.app.parent.exceptions.ParentWordException;
import junit.framework.TestCase;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import static org.mockito.Mockito.*;

/**
 * Created by IMac on 2/9/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class ParentServiceBeanTest extends TestCase {
	@Mock
	private EntityManager em;
	@Mock
	private Logger log;
	@Mock
	private DoubleMetaphone dm;
	@Mock
	private TypedQuery<Week> weekQuery;
	@Mock
	private TypedQuery<Sentence> sentenceQuery;
	@Mock
	private TypedQuery<WordLocation> wordLocationQuery;
	@Mock
	private TypedQuery<Word> wordQuery;

	@InjectMocks
	private ParentServiceBean psb = new ParentServiceBean();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testFindParentChildIsFound() {
		ChildUser original = new ChildUser();
		original.username = "test";
		original.password = "test";
		original.grade = mock(Grade.class);
		original.location = mock(Location.class);
		original.parent = mock(User.class);
		when(em.find(ChildUser.class, 1)).thenReturn(original);
		try {
			ChildUser result = psb.findParentChild(1, "test tracking");
			assertEquals("test", result.username);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testFindParentChildIsNotFound() {
		ChildUser original = new ChildUser();
		original.username = "test";
		original.password = "test";
		original.grade = mock(Grade.class);
		original.location = mock(Location.class);
		original.parent = mock(User.class);
		when(em.find(ChildUser.class, 1)).thenThrow(new NoResultException("no child id"));
		try {
			ChildUser result = psb.findParentChild(1, "test tracking");
			assertEquals("test", result.username);
		} catch (ParentApiException paEx) {
			assertEquals(ParentApiConstants.NO_CHILD_BY_ID, paEx.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testFindParentChildGeneralException() {
		ChildUser original = new ChildUser();
		original.username = "test";
		original.password = "test";
		original.grade = mock(Grade.class);
		original.location = mock(Location.class);
		original.parent = mock(User.class);
		when(em.find(ChildUser.class, 1)).thenThrow(new RuntimeException("find failed"));
		try {
			ChildUser result = psb.findParentChild(1, "test tracking");
			assertEquals("test", result.username);
		} catch (ParentApiException paEx) {
			assertEquals(ParentApiConstants.FIND_PARENT_CHILD_FAILED, paEx.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testEditChildWhenEmContainsOriginal() {
		ChildUser original = new ChildUser();
		original.username = "test";
		original.password = "test";
		original.grade = mock(Grade.class);
		original.location = mock(Location.class);
		original.parent = mock(User.class);
		ChildUser updated = new ChildUser();
		updated.username = "test";
		updated.password = "test1";
		updated.grade = mock(Grade.class);
		updated.location = mock(Location.class);
		updated.parent = mock(User.class);
		when(em.contains(original)).thenReturn(true);
		try {
			psb.editChild(original, updated, "test tracking");
			verify(em, times(1)).contains(original);
			verify(em, never()).merge(original);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testEditChildWhenEmDoesNotContainOriginal() {
		ChildUser original = new ChildUser();
		original.username = "test";
		original.password = "test";
		original.grade = mock(Grade.class);
		original.location = mock(Location.class);
		original.parent = mock(User.class);
		ChildUser updated = new ChildUser();
		updated.username = "test";
		updated.password = "test1";
		updated.grade = mock(Grade.class);
		updated.location = mock(Location.class);
		updated.parent = mock(User.class);
		when(em.contains(original)).thenReturn(false);
		try {
			psb.editChild(original, updated, "test tracking");
			verify(em, times(1)).contains(original);
			verify(em, times(1)).merge(original);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testEditChildWhenEmMergeThrowsException() {
		ChildUser original = new ChildUser();
		original.username = "test";
		original.password = "test";
		original.grade = mock(Grade.class);
		original.location = mock(Location.class);
		original.parent = mock(User.class);
		ChildUser updated = new ChildUser();
		updated.username = "test";
		updated.password = "test1";
		updated.grade = mock(Grade.class);
		updated.location = mock(Location.class);
		updated.parent = mock(User.class);
		when(em.contains(original)).thenReturn(false);
		when(em.merge(original)).thenThrow(new RuntimeException("merge fails"));
		try {
			psb.editChild(original, updated, "test tracking");
			verify(em, times(1)).contains(original);
			verify(em, times(1)).merge(original);
		} catch (ParentApiException paEx) {
			assertEquals(ParentApiConstants.EDIT_CHILD_PASSWORD_FAILED, paEx.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindWordWhenWordExists() {
		Word word = new Word("word", "ART");
		User parent = new User();
		when(em.createNamedQuery("findWord", Word.class)).thenReturn(wordQuery);
		when(wordQuery.getSingleResult()).thenReturn(word);
		try {
			Word result = psb.createOrFindWord(parent, word, "test tracking");
			assertEquals("word", result.word);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindWordWhenWordNotExists() {
		Word word = new Word("word", "ART");
		User parent = new User();
		when(em.createNamedQuery("findWord", Word.class)).thenReturn(wordQuery);
		when(wordQuery.getSingleResult()).thenThrow(new NoResultException("word not found"));
		try {
			Word result = psb.createOrFindWord(parent, word, "test tracking");
			assertEquals("word", result.word);
			verify(em, times(1)).persist(word);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindWordWhenPersistFails() {
		Word word = new Word("word", "ART");
		User parent = new User();
		when(em.createNamedQuery("findWord", Word.class)).thenReturn(wordQuery);
		when(wordQuery.getSingleResult()).thenThrow(new NoResultException("word not found"));
		doThrow(new RuntimeException("persist fails")).when(em).persist(word);
		try {
			psb.createOrFindWord(parent, word, "test tracking");
			verify(em, times(1)).persist(word);
		} catch (ParentWordException pwEx) {
			assertEquals(ParentApiConstants.CREATE_OR_FIND_WORD_FAILED, pwEx.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testFindWordToEditWhenWordFound() {
		Word word = new Word("word", "ART");
		when(em.createNamedQuery("findWord", Word.class)).thenReturn(wordQuery);
		when(wordQuery.getSingleResult()).thenReturn(word);
		try {
			Word result = psb.findWordToEdit(word, "test tracking");
			assertEquals("word", result.word);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testFindWordToEditWhenWordNotFound() {
		Word word = new Word("word", "ART");
		when(em.createNamedQuery("findWord", Word.class)).thenReturn(wordQuery);
		when(wordQuery.getSingleResult()).thenThrow(new NoResultException("word not found"));
		try {
			Word result = psb.findWordToEdit(word, "test tracking");
		} catch (ParentWordException pwEx) {
			assertEquals(ParentApiConstants.EDIT_WORD, pwEx.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindWordLocationExistsAlready() {
		Location locationMock = mock(Location.class);
		Grade gradeMock = mock(Grade.class);
		Word wordMock = mock(Word.class);
		WordLocation wordLocation = new WordLocation();
		wordLocation.location = locationMock;
		wordLocation.grade = gradeMock;
		wordLocation.word = wordMock;
		when(em.createNamedQuery("findWordLocation", WordLocation.class)).thenReturn(wordLocationQuery);
		when(wordLocationQuery.getSingleResult()).thenReturn(wordLocation);
		try {
			psb.createOrFindWordLocation(wordLocation, "test tracking");
			verify(em, never()).persist(wordLocation);
		} catch (Exception ex) {
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindWordLocationNoResultsFound() {
		Location locationMock = mock(Location.class);
		Grade gradeMock = mock(Grade.class);
		Word wordMock = mock(Word.class);
		WordLocation wordLocation = new WordLocation();
		wordLocation.location = locationMock;
		wordLocation.grade = gradeMock;
		wordLocation.word = wordMock;
		when(em.createNamedQuery("findWordLocation", WordLocation.class)).thenReturn(wordLocationQuery);
		when(wordLocationQuery.getSingleResult()).thenThrow(new NoResultException("test no results"));
		try {
			psb.createOrFindWordLocation(wordLocation, "test tracking");
			verify(em, times(1)).persist(wordLocation);
		} catch (Exception ex) {
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindWordLocationWhenPersistThrowsException() {
		Location locationMock = mock(Location.class);
		Grade gradeMock = mock(Grade.class);
		Word wordMock = mock(Word.class);
		WordLocation wordLocation = new WordLocation();
		wordLocation.location = locationMock;
		wordLocation.grade = gradeMock;
		wordLocation.word = wordMock;
		when(em.createNamedQuery("findWordLocation", WordLocation.class)).thenReturn(wordLocationQuery);
		when(wordLocationQuery.getSingleResult()).thenThrow(new NoResultException("test no results"));
		doThrow(new RuntimeException("persist throws exception")).when(em).persist(wordLocation);
		try {
			psb.createOrFindWordLocation(wordLocation, "test tracking");
			verify(em, times(1)).persist(wordLocation);
		} catch (ParentWordException pwEx) {
			assertEquals(ParentApiConstants.CREATE_OR_FIND_WORD_LOCATION_FAILED, pwEx.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindWordSentencesExistsAlready() {
		Sentence sentence = new Sentence("This is a sentence");
		when(em.createNamedQuery("findSentence", Sentence.class)).thenReturn(sentenceQuery);
		when(sentenceQuery.getSingleResult()).thenReturn(sentence);
		try {
			psb.createOrFindWordSentence(sentence, "test tracking");
			verify(em, never()).persist(sentence);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindWordSentenceNoResultFound() {
		Sentence sentence = new Sentence("This is a sentence");
		when(em.createNamedQuery("findSentence", Sentence.class)).thenReturn(sentenceQuery);
		when(sentenceQuery.getSingleResult()).thenThrow(new NoResultException("no result found test"));
		try {
			psb.createOrFindWordSentence(sentence, "test tracking");
			verify(em, times(1)).persist(sentence);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindWordSentenceWhenPersistThrowsException() {
		Sentence sentence = new Sentence("This is a sentence");
		when(em.createNamedQuery("findSentence", Sentence.class)).thenReturn(sentenceQuery);
		when(sentenceQuery.getSingleResult()).thenThrow(new NoResultException("no result found test"));
		doThrow(new RuntimeException("persist fails")).when(em).persist(sentence);
		try {
			psb.createOrFindWordSentence(sentence, "test tracking");
			verify(em, times(1)).persist(sentence);
		} catch (ParentWordException pwEx) {
			assertEquals(ParentApiConstants.CREATE_OR_FIND_WORD_SENTENCE_FAILED, pwEx.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindWeekWhenWeekExistsAlready() {
		DateTime startWeek = DateTime.now().withMonthOfYear(DateTimeConstants.JANUARY).withDayOfMonth(1).withYear(2015)
				.withTimeAtStartOfDay();
		DateTime endWeek = DateTime.now().withMonthOfYear(DateTimeConstants.JANUARY).withDayOfMonth(7).withYear(2015)
				.withTimeAtStartOfDay();
		Week week = new Week(startWeek.toDate(), endWeek.toDate());
		when(em.createNamedQuery("findWeek", Week.class)).thenReturn(weekQuery);
		when(weekQuery.getSingleResult()).thenReturn(week);
		try {
			Week result = psb.createOrFindWeek(week, "test tracking");
			assertNotNull(result);
			assertEquals(result.weekStart, week.weekStart);
			assertEquals(result.weekEnd, week.weekEnd);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testCreateOrFindWeekWhenNoWeekFound() {
		DateTime startWeek = DateTime.now().withMonthOfYear(DateTimeConstants.JANUARY).withDayOfMonth(1).withYear(2015)
				.withTimeAtStartOfDay();
		DateTime endWeek = DateTime.now().withMonthOfYear(DateTimeConstants.JANUARY).withDayOfMonth(7).withYear(2015)
				.withTimeAtStartOfDay();
		Week week = new Week(startWeek.toDate(), endWeek.toDate());
		when(em.createNamedQuery("findWeek", Week.class)).thenReturn(weekQuery);
		when(weekQuery.getSingleResult()).thenThrow(new NoResultException("testing week not found"));
		try {
			Week result = psb.createOrFindWeek(week, "test tracking");
			assertNotNull(result);
			assertEquals(result.weekStart, week.weekStart);
			assertEquals(result.weekEnd, week.weekEnd);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
		verify(em, times(1)).persist(week);
	}

	@Test(expected = ParentWordException.class)
	public void testCreateOrFindWeekWhenRuntimeExceptionDuringFindWeek()
	throws ParentWordException {
		DateTime startWeek = DateTime.now().withMonthOfYear(DateTimeConstants.JANUARY).withDayOfMonth(1).withYear(2015)
				.withTimeAtStartOfDay();
		DateTime endWeek = DateTime.now().withMonthOfYear(DateTimeConstants.JANUARY).withDayOfMonth(7).withYear(2015)
				.withTimeAtStartOfDay();
		Week week = new Week(startWeek.toDate(), endWeek.toDate());
		when(em.createNamedQuery("findWeek", Week.class)).thenReturn(weekQuery);
		when(weekQuery.getSingleResult()).thenThrow(new NoResultException("testing week not found"));
		doThrow(new RuntimeException("testing exception at perist")).when(em).persist(week);
		Week result = psb.createOrFindWeek(week, "test tracking");
		assertNotNull(result);
		assertEquals(result.weekStart, week.weekStart);
		assertEquals(result.weekEnd, week.weekEnd);
		verify(em, times(1)).persist(week);
	}

	@Test
	public void testCreateOrFindWeekWhenPersistThrowsRuntimeException() {
		DateTime startWeek = DateTime.now().withMonthOfYear(DateTimeConstants.JANUARY).withDayOfMonth(1).withYear(2015)
				.withTimeAtStartOfDay();
		DateTime endWeek = DateTime.now().withMonthOfYear(DateTimeConstants.JANUARY).withDayOfMonth(7).withYear(2015)
				.withTimeAtStartOfDay();
		Week week = new Week(startWeek.toDate(), endWeek.toDate());
		when(em.createNamedQuery("findWeek", Week.class)).thenReturn(weekQuery);
		when(weekQuery.getSingleResult()).thenThrow(new RuntimeException("testing week not found"));
		try {
			Week result = psb.createOrFindWeek(week, "test tracking");
			assertNotNull(result);
			assertEquals(result.weekStart, week.weekStart);
			assertEquals(result.weekEnd, week.weekEnd);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
	}

	//scenario: parent is editing a word
	//given: User who is the parent doing the editing
	//and: Word which represents the original word
	//and: Word which represents the edited word
	//and: trackingId
	//when: parent submits the word for editing
	//then: original word should be updated
	@Test
	public void testEditWordWithoutMergeNeededAndNoExceptions() {
		User user = new User("test@test.com", "test");
		Word originalWord = new Word("wrd", "rd");
		Word editedWord = new Word("word", "rd");
		String trackingId = "Testing Edit Word";
		try {
			when(em.contains(originalWord)).thenReturn(true);
			psb.editWord(user, originalWord, editedWord, trackingId);
		} catch (Exception ex) {
			fail("Exception should not have been thrown here:" + ex.toString());
		}
		assertEquals("word", originalWord.word);
	}

	//scenario: parent is editing a word
	// given: user who is a parent
	//   and: word which represents the original word
	//   and: word which represents the edited word
	//   and: trackingId
	//  when: parent submits the word for editing
	//   and: original word is not contained in the em
	//  then: original word should be updated
	@Test
	public void testEditWordWithMergeNeededAndNoExceptions() {
		User user = new User("test@test.com", "test");
		Word originalWord = new Word("wrd", "rd");
		Word editedWord = new Word("word", "rd");
		String trackingId = "Testing Edit Word";
		try {
			when(em.contains(originalWord)).thenReturn(false);
			when(em.merge(originalWord)).thenReturn(editedWord);
			psb.editWord(user, originalWord, editedWord, trackingId);
		} catch (Exception ex) {
			fail("Exception should not have been thrown here:" + ex.toString());
		}
		assertEquals("word", originalWord.word);
	}

	//scenario: parent is editing a word when something goes wrong at the database
	// given: user who is a parent
	//   and: word which represents the original word
	//   and: word which represents the edited word
	//   and: trackingId
	//  when: parent submits the word for editing
	//   and: an exception occurs
	//  then: exception should be thrown
	@Test(expected = ParentWordException.class)
	public void testEditWordException()
	throws ParentWordException {
		User user = new User("test@test.com", "test");
		Word originalWord = new Word("wrd", "rd");
		Word editedWord = new Word("word", "rd");
		String trackingId = "Testing Edit Word";
		when(em.contains(originalWord)).thenReturn(false);
		when(em.merge(originalWord)).thenThrow(new RuntimeException("edit word test"));
		psb.editWord(user, originalWord, editedWord, trackingId);
	}

	@Test
	public void testConfirmPasswordsEqual() {
		try {
			boolean shouldBeTrue = psb.confirmPasswords("test", "test");
			assertTrue(shouldBeTrue);
		} catch (Exception ex) {
			fail("did not expect any exception here");
		}
	}

	@Test(expected = AuthException.class)
	public void testConfirmPasswordsNotEqual()
	throws Exception {
		psb.confirmPasswords("test", "test1");
	}

	@Test(expected = AuthException.class)
	public void testConfirmPasswordsWithNullPasswords()
	throws AuthException {
		psb.confirmPasswords(null, "test");
	}

	@Test(expected = AuthException.class)
	public void testConfirmPasswordsWithNullConfirmPassword()
	throws AuthException {
		psb.confirmPasswords("test", null);
	}
}
