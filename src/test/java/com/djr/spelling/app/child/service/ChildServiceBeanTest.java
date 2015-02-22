package com.djr.spelling.app.child.service;

import com.djr.spelling.BaseTest;
import com.djr.spelling.ChildUser;
import com.djr.spelling.app.exceptions.AuthException;
import com.djr.spelling.app.services.auth.AuthConstants;
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
 * Created by IMac on 2/22/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class ChildServiceBeanTest extends BaseTest {
	@Mock
	private EntityManager em;
	@Mock
	private TypedQuery<ChildUser> childUserQuery;

	@InjectMocks
	private ChildServiceBean csb = new ChildServiceBean();

	@Before
	public void setup()
	throws Exception {
		addLogger(csb, "log");
	}

	@Test
	public void testFindChildUserWhenChildUserExists() {
		ChildUser childUser = mock(ChildUser.class);
		childUser.id = 1;
		when(em.createNamedQuery("findChildUser", ChildUser.class)).thenReturn(childUserQuery);
		when(childUserQuery.getSingleResult()).thenReturn(childUser);
		try {
			ChildUser result = csb.findChildUser("child", "password", "test tracking");
			assertEquals(result.id, childUser.id);
		} catch (AuthException aEx) {
			fail("did not expect auth exception here");
		} catch (Exception ex) {
			fail("did not expect any exception here");
		}
	}

	@Test
	public void testFindChildUserWhenChildUserDoesNotExist() {
		when(em.createNamedQuery("findChildUser", ChildUser.class)).thenReturn(childUserQuery);
		when(childUserQuery.getSingleResult()).thenThrow(new NoResultException("no results"));
		try {
			ChildUser result = csb.findChildUser("child", "password", "test tracking");
			fail("expected no result exception");
		} catch (AuthException aEx) {
			assertEquals(AuthConstants.USER_NOT_FOUND, aEx.getMessage());
		} catch (Exception ex) {
			fail("expected user not found auth exception");
		}
	}

	@Test
	public void testFindChildUserWhenGeneralExceptionOccurs() {
		when(em.createNamedQuery("findChildUser", ChildUser.class)).thenReturn(childUserQuery);
		when(childUserQuery.getSingleResult()).thenThrow(new RuntimeException("general exception"));
		try {
			ChildUser result = csb.findChildUser("child", "password", "test tracking");
			fail("expected general auth exception");
		} catch (AuthException aEx) {
			assertEquals(AuthConstants.GENERAL_AUTH, aEx.getMessage());
		} catch (Exception ex) {
			fail("expected general auth exception");
		}
	}
}
