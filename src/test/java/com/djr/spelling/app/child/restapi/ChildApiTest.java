package com.djr.spelling.app.child.restapi;

import com.djr.spelling.BaseTest;
import com.djr.spelling.ChildUser;
import com.djr.spelling.SpellingService;
import com.djr.spelling.app.Constants;
import com.djr.spelling.app.child.model.ChildLoginRequest;
import com.djr.spelling.app.child.model.ChildLoginResponse;
import com.djr.spelling.app.child.service.ChildServiceBean;
import com.djr.spelling.app.services.auth.AuthService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Created by IMac on 2/22/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class ChildApiTest extends BaseTest {
	@Mock
	private ChildServiceBean csb;
	@Mock
	private AuthService as;
	@Mock
	private SpellingService ss;

	@InjectMocks
	private ChildApi api = new ChildApi();

	@Before
	public void setup()
	throws Exception {
		MockitoAnnotations.initMocks(api);
		addLogger(api, "log");
	}

	@Test
	public void testLogin() {
		ChildLoginRequest clr = new ChildLoginRequest();
		clr.username = "test";
		clr.password = "test";
		ChildLoginResponse resp = null;
		ChildUser childUser = new ChildUser("test", "hashed");
		childUser.id = 1;
		try {
			when(as.getPasswordHash("test")).thenReturn("hashed");
			when(csb.findChildUser("test", "hashed", "123")).thenReturn(childUser);
			when(as.getAuthToken("123")).thenReturn("token");
			resp = api.login("123", clr);
			assertNotNull(resp);
			assertEquals(1, resp.id.intValue());
			assertEquals("token", resp.authToken);
			assertEquals(Constants.CHILD_QUIZ_LANDING, resp.forwardTo);
		} catch (Exception ex) {
			fail("did not expect any exception here");
		}
	}
}
