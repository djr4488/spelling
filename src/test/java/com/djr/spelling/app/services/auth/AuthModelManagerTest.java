package com.djr.spelling.app.services.auth;

import com.djr.spelling.app.BaseTest;
import com.djr.spelling.app.services.auth.model.AuthModel;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by IMac on 2/21/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthModelManagerTest extends BaseTest {
	@InjectMocks
	private AuthModelManager amm = new AuthModelManager();

	@Before
	public void setup()
	throws Exception {
		addIntegerField(amm, "timeToLive", 1);
		addLogger(amm, "log");
	}

	@Test
	public void testAddAuthModel() {
		AuthModel authModel = new AuthModel("123", 1, 1);
		amm.addAuthModel(authModel);
		assertEquals(authModel, amm.getAuthModelByTrackingId("123"));
	}

	@Test
	public void testRemoveAuthModel() {
		AuthModel authModel = new AuthModel("123", 1, 1);
		amm.addAuthModel(authModel);
		assertEquals(authModel, amm.getAuthModelByTrackingId("123"));
		amm.removeAuthModel(authModel);
		assertNull(amm.getAuthModelByTrackingId("123"));
	}

	@Test
	public void testKeepCleaning() {
		DateTime now = DateTime.now();
		assertTrue(amm.keepCleaning(now));
		assertFalse(amm.keepCleaning(now.minusSeconds(10)));
	}

	@Test
	public void removeExpired() {
		AuthModel authModelExpired = new AuthModel("123", 1, 1);
		authModelExpired.exipiry = DateTime.now().minusMinutes(500);
		AuthModel authModelNotExpired = new AuthModel("124", 1, 1);
		authModelNotExpired.exipiry = DateTime.now().plusMinutes(500);
		amm.addAuthModel(authModelExpired);
		amm.addAuthModel(authModelNotExpired);
		assertEquals(authModelExpired, amm.getAuthModelByTrackingId("123"));
		assertEquals(authModelNotExpired, amm.getAuthModelByTrackingId("124"));
		amm.removeExpired();
		assertNull(amm.getAuthModelByTrackingId("123"));
		assertEquals(authModelNotExpired, amm.getAuthModelByTrackingId("124"));
	}
}
