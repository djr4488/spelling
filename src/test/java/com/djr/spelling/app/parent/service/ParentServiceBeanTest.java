package com.djr.spelling.app.parent.service;

import com.djr.spelling.User;
import com.djr.spelling.Word;
import com.djr.spelling.app.exceptions.AuthException;
import com.djr.spelling.app.parent.exceptions.ParentWordException;
import junit.framework.TestCase;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import javax.persistence.EntityManager;

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

	@InjectMocks
	private ParentServiceBean psb = new ParentServiceBean();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
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
