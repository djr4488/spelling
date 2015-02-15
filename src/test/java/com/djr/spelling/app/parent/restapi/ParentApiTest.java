package com.djr.spelling.app.parent.restapi;

import com.djr.spelling.*;
import com.djr.spelling.app.Constants;
import com.djr.spelling.app.parent.restapi.model.*;
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

import java.util.ArrayList;
import java.util.List;
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
	private SpellingService ss;
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
			when(ss.createOrFindSchool(any(School.class))).thenReturn(new School());
			when(ss.createOrFindState(any(State.class))).thenReturn(new State());
			when(ss.createOrFindCity(any(City.class))).thenReturn(new City());
			when(ss.createOrFindGrade(any(Grade.class))).thenReturn(new Grade());
			when(ss.createOrFindLocation(any(Location.class))).thenReturn(new Location());
			doNothing().when(psb).createParentAccount(any(User.class), anyString());
			resp = api.createParentUser("test tracking", parentCreateRequest);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect exception");
		}
		assertNotNull(resp);
		assertEquals(Constants.LOGIN_LANDING, resp.forwardTo);
	}

	//scenario: parent creating a child
	//given: a valid request with valid tracking id
	//and: a user who is a parent who is logged in
	//when: request is submitted to create a child
	//then: expect a response indicating success
	@Test
	public void testCreateChildUser() {
		ChildUserCreateRequest childUserCreateRequest = new ChildUserCreateRequest();
		ChildUserCreateResponse resp = null;
		try {
			when(psb.confirmPasswords("test", "test")).thenReturn(true);
			when(psb.findParentAccount(1, "test tracking")).thenReturn(new User());
			when(as.getPasswordHash("test")).thenReturn("password hash");
			doNothing().when(psb).createChildAccount(any(ChildUser.class), anyString());
			resp = api.createChildUser(childUserCreateRequest, 1, "test tracking", "test auth");
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
		assertNotNull(resp);
		assertEquals(Constants.CREATE_CHILD_LANDING, resp.forwardTo);
	}

	//scenario: a user is logging in as a parent
	//given: a user logging in
	//and: a valid tracking id
	//and: a valid user and password found
	//when: user submits form to login
	//then: expect a response indicating success
	@Test
	public void testLogin() {
		ParentLoginRequest parentLoginRequest = new ParentLoginRequest();
		parentLoginRequest.emailAddress = "test@test.com";
		parentLoginRequest.password = "test";
		ParentLoginResponse resp = null;
		User parent = new User();
		parent.id = 1;
		try {
			when(psb.findParentAccount(any(User.class), anyString())).thenReturn(parent);
			when(as.getPasswordHash("test")).thenReturn("password hash");
			when(as.getAuthToken("test tracking")).thenReturn("auth token");
			resp = api.login("test tracking", parentLoginRequest);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
		assertNotNull(resp);
		assertEquals(Constants.PARENT_LANDING, resp.forwardTo);
		assertEquals("auth token", resp.authToken);
	}

	//scenario: a parent who is changing his/her password
	//given: a valid logged in parent
	//and: confirmed passwords
	//when: edit parent form submitted
	//then: expect a response indicating success
	@Test
	public void testEditParent() {
		EditParentRequest req = new EditParentRequest();
		req.originalPassword = "test";
		req.password = "test";
		req.confirmPassword = "test";
		User parent = new User();
		parent.id = 1;
		EditParentResponse resp = null;
		try {
			when(psb.confirmPasswords("test", "test")).thenReturn(true);
			when(psb.findParentAccount(1, "test tracking")).thenReturn(parent);
			doNothing().when(psb).editParentPassword(any(User.class), any(User.class), anyString());
			resp = api.editParent(req, 1, "test tracking", "test auth");
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
		assertNotNull(resp);
		assertEquals(Constants.EDIT_PARENT_LANDING, resp.forwardTo);
	}

	//scenario: a parent who is viewing his/her children
	//given: a valid logged in parent
	//and: parent has one child
	//when: finding parent children
	//then: expect a valid response indicating success and contains the child information
	@Test
	public void testFindParentChildren() {
		FindChildrenResponse resp = null;
		List<ChildUser> children = new ArrayList<>();
		State state = new State("KS");
		City city = new City("Kansas City");
		Grade grade = new Grade("4th");
		School school = new School("Test School", false, false);
		Location location = new Location(state, city, school);
		ChildUser childUser = new ChildUser();
		childUser.username = "testChild";
		childUser.id = 1;
		childUser.grade = grade;
		childUser.location = location;
		children.add(childUser);
		User parent = new User();
		parent.id = 1;
		try {
			when(psb.findParentAccount(1, "test tracking")).thenReturn(parent);
			when(psb.findParentChildren(parent, "test tracking")).thenReturn(children);
			resp = api.findParentChildren("test tracking", "test auth", 1);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("did not expect any exception here");
		}
		assertNotNull(resp);
		assertEquals(Constants.FIND_PARENT_CHILDREN, resp.forwardTo);
		assertNotNull(resp.parentChildren);
		assertEquals(1, resp.parentChildren.parentChildren.size());
		assertEquals("testChild", (resp.parentChildren.parentChildren.get(0).username));
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
