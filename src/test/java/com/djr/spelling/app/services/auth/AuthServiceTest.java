package com.djr.spelling.app.services.auth;

import com.djr.spelling.app.BaseTest;
import com.djr.spelling.app.services.auth.model.AuthModel;
import com.djr.spelling.app.services.auth.util.HashingUtil;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Created by IMac on 2/21/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest extends BaseTest {
	@Mock
	private AuthModelManager authModelManager;
	@Mock
	private HashingUtil hashUtil;

	@InjectMocks
	private AuthService authSvc = new AuthService();

	@Before
	public void setup()
	throws Exception {
		MockitoAnnotations.initMocks(authSvc);
		addIntegerField(authSvc, "timeToLive", 1);
		addLogger(authSvc, "log");
	}

	@Test
	public void testValidateTrackingIdOnlySuccess() {
		AuthModel authModel = new AuthModel("123", 1, 1);
		authModel.timestamp = DateTime.now();
		when(authModelManager.getAuthModelByTrackingId("123")).thenReturn(authModel);
		assertTrue(authSvc.validateTrackingId("123", null, true));
	}

	@Test
	public void testValidateTrackingIdOnlyFails() {
		AuthModel authModel = new AuthModel("123", 1, 1);
		authModel.timestamp = DateTime.now();
		authModel.exipiry = DateTime.now().minusMinutes(400);
		when(authModelManager.getAuthModelByTrackingId("123")).thenReturn(authModel);
		assertFalse(authSvc.validateTrackingId("123", null, true));
	}

	@Test
	public void testValidateAuthToken() {
		AuthModel authModel = new AuthModel("123", 1, 1);
		authModel.timestamp = DateTime.now();
		when(authModelManager.getAuthModelByTrackingId("123")).thenReturn(authModel);
		when(hashUtil.generateHmacHash(anyString())).thenReturn("auth-token");
		assertTrue(authSvc.validateTrackingId("123", "auth-token", false));
	}

	@Test
	public void testValidateAuthTokenWhenAuthTokenIsNull() {
		AuthModel authModel = new AuthModel("123", 1, 1);
		authModel.timestamp = DateTime.now();
		when(authModelManager.getAuthModelByTrackingId("123")).thenReturn(authModel);
		when(hashUtil.generateHmacHash(anyString())).thenReturn("auth-token");
		assertFalse(authSvc.validateTrackingId("123", null, false));
	}

	@Test
	public void testAddTrackingIdSuccess() {
		authSvc.addTrackingId("123", 1);
		verify(authModelManager, times(1)).addAuthModel(any(AuthModel.class));
	}

	@Test
	public void testGetAuthToken() {
		AuthModel authModel = new AuthModel("123", 1, 1);
		authModel.timestamp = DateTime.now();
		when(authModelManager.getAuthModelByTrackingId("123")).thenReturn(authModel);
		when(hashUtil.generateHmacHash(anyString())).thenReturn("hashtest");
		assertEquals("hashtest", authSvc.getAuthToken("123"));
	}

	@Test
	public void testGetUserId() {
		AuthModel authModel = new AuthModel("123", 1, 1);
		authModel.timestamp = DateTime.now();
		when(authModelManager.getAuthModelByTrackingId("123")).thenReturn(authModel);
		assertEquals(1, authSvc.getUserId("123").intValue());
	}

	@Test
	public void testGetPasswordHash() {
		when(hashUtil.generateHmacHash(anyString())).thenReturn("hashed");
		assertEquals("hashed", authSvc.getPasswordHash("test"));
	}

	@Test
	public void testIsProvidedAuthTokenNull() {
		assertTrue(authSvc.isProvidedAuthTokenNull(null));
		assertFalse(authSvc.isProvidedAuthTokenNull("auth token"));
	}

	@Test
	public void testIsAuthModelNull() {
		AuthModel authModel = new AuthModel("123", 1, 1);
		assertFalse(authSvc.isAuthModelNull(authModel));
		assertTrue(authSvc.isAuthModelNull(null));
	}
}
