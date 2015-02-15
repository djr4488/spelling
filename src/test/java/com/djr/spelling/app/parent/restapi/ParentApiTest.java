package com.djr.spelling.app.parent.restapi;

import com.djr.spelling.User;
import com.djr.spelling.Word;
import com.djr.spelling.app.Constants;
import com.djr.spelling.app.parent.restapi.model.EditWordRequest;
import com.djr.spelling.app.parent.restapi.model.EditWordResponse;
import com.djr.spelling.app.parent.restapi.model.ParentCreateRequest;
import com.djr.spelling.app.parent.restapi.model.ParentCreateResponse;
import com.djr.spelling.app.parent.service.ParentServiceBean;
import com.djr.spelling.app.services.auth.AuthService;
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

import static org.mockito.Mockito.*;

/**
 * Created by IMac on 2/9/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class ParentApiTest extends TestCase {
	@Mock
	private Logger log;
	@Mock
	private ParentServiceBean psb;
	@Mock
	private AuthService as;
	@Mock
	private DoubleMetaphone dm;

	@InjectMocks
	private ParentApi api = new ParentApi();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	//scenario: user is creating a user as a parent
	//given: a valid request with password and confirm password the same
	//and: non used parent user name
	//and: a valid tracking id
	//when: request is submitted
	//then: expect a response returned indicating success
	@Test
	public void testCreateParentUser() {
		ParentCreateRequest parentCreateRequest = new ParentCreateRequest();
		parentCreateRequest.username = "test";
		parentCreateRequest.password = "test";
		parentCreateRequest.confirmPassword = "test";
		parentCreateRequest.emailAddress = "test@test.com";
		ParentCreateResponse resp = null;
		try {
			when(psb.confirmPasswords("test", "test")).thenReturn(true);
			when(as.getPasswordHash("test")).thenReturn("password hash");
			doNothing().when(psb).createParentAccount(any(User.class), anyString());
			resp = api.createParentUser("test tracking", parentCreateRequest);
		} catch (Exception ex) {
			fail("did not expect exception");
		}
		assertNotNull(resp);
		assertEquals(Constants.LOGIN_LANDING, resp.forwardTo);
	}

	//scenario: parent who is editing word
	//given: user who is a parent
	//and: successful finding of original word
	//and: successful edit of original word
	//when: word is edited
	//then: expect success response and edit word landing with success message
	@Test
	public void testEditWord() {
		EditWordRequest request = new EditWordRequest();
		request.editedWord = "word";
		request.sentence = "A word has meaning";
		request.word = "wrd";
		EditWordResponse resp = null;
		User parent = new User();
		parent.id = 1;
		parent.username = "parent";
		Word wronglySpelledWord = new Word();
		wronglySpelledWord.word = "wrd";
		wronglySpelledWord.metaphone="rd";
		wronglySpelledWord.id = 1;
		Word correctedWord = new Word();
		correctedWord.word = "word";
		correctedWord.metaphone = "rd";
		try {
			when(psb.findParentAccount(1, "Testing EditWord")).thenReturn(parent);
			when(psb.findWordToEdit(any(Word.class), anyString())).thenReturn(wronglySpelledWord);
			when(as.getAuthToken("Testing EditWord")).thenReturn("New Auth");
			doNothing().when(psb).editWord(parent, wronglySpelledWord, correctedWord, "Testing EditWord");
			resp = api.editWord(request, 1, "auth", "Testing EditWord");
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Did not expect any exceptions here");
		}
		assertNotNull(resp);
		assertEquals(1, (int)resp.wordId);
		assertEquals("New Auth", resp.authToken);
		assertEquals(Constants.EDIT_WORD_LANDING, resp.forwardTo);
	}
}
