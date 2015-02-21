package com.djr.spelling.app.services.auth;

import com.djr.spelling.app.Constants;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static org.mockito.Mockito.*;

/**
 * Created by IMac on 2/21/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthFilterTest extends TestCase {
	@Mock
	private AuthService as;
	@Mock
	private Logger log;
	@Mock
	private ContainerRequestContext requestContext;

	@InjectMocks
	AuthFilter authFilter = new AuthFilter();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(authFilter);
	}

	@Test
	public void testWhenTrackingIdRequest() {
		UriInfo uriInfo = mock(UriInfo.class);
		when(requestContext.getUriInfo()).thenReturn(uriInfo);
		when(uriInfo.getPath()).thenReturn("getTrackingId");
		authFilter.filter(requestContext);
		verify(requestContext, never()).abortWith(any(Response.class));
		verify(log, times(1)).debug("filter() tracking id request");
	}

	@Test
	public void testWhenAuthorizedForParentResource() {
		UriInfo uriInfo = mock(UriInfo.class);
		when(requestContext.getUriInfo()).thenReturn(uriInfo);
		when(requestContext.getHeaderString(Constants.TRACKING_ID)).thenReturn("123");
		when(requestContext.getHeaderString(Constants.AUTH_TOKEN)).thenReturn("auth-token");
		when(uriInfo.getPath()).thenReturn("parent/sp/");
		when(as.validateTrackingId("123", "auth-token", false)).thenReturn(true);
		authFilter.filter(requestContext);
		verify(requestContext, never()).abortWith(any(Response.class));
		verify(log, times(1)).debug("filter() checking semi-protected request");
	}

	@Test
	public void testWhenNotAuthorizedForParentResource() {
		UriInfo uriInfo = mock(UriInfo.class);
		when(requestContext.getUriInfo()).thenReturn(uriInfo);
		when(requestContext.getHeaderString(Constants.TRACKING_ID)).thenReturn("123");
		when(requestContext.getHeaderString(Constants.AUTH_TOKEN)).thenReturn("auth-token");
		when(uriInfo.getPath()).thenReturn("parent/sp/");
		when(as.validateTrackingId("123", "auth-token", false)).thenReturn(false);
		authFilter.filter(requestContext);
		verify(requestContext, times(1)).abortWith(any(Response.class));
		verify(log, times(1)).debug("filter() checking semi-protected request");
		verify(log, times(1)).debug("filter() aborting with status:{}, msg:{}, bold:{}", Response.Status.UNAUTHORIZED,
				"Didn't recognize you!  Maybe you typed the wrong password or need to create a user name?", "Oops!");
		verify(log, times(1)).debug("filter() completed");
	}

	@Test
	public void testWhenSuccessfulTrackingIdCheckDuringLogin() {
		UriInfo uriInfo = mock(UriInfo.class);
		when(requestContext.getUriInfo()).thenReturn(uriInfo);
		when(requestContext.getHeaderString(Constants.TRACKING_ID)).thenReturn("123");
		when(requestContext.hasEntity()).thenReturn(true);
		when(uriInfo.getPath()).thenReturn("parent/login");
		when(as.validateTrackingId("123", null, true)).thenReturn(true);
		authFilter.filter(requestContext);
		verify(requestContext, never()).abortWith(any(Response.class));
		verify(log, times(1)).debug("filter() checking tracked request");
	}

	@Test
	public void testWhenSuccessfulTrackingIdCheckDuringParentCreate() {
		UriInfo uriInfo = mock(UriInfo.class);
		when(requestContext.getUriInfo()).thenReturn(uriInfo);
		when(requestContext.getHeaderString(Constants.TRACKING_ID)).thenReturn("123");
		when(requestContext.hasEntity()).thenReturn(true);
		when(uriInfo.getPath()).thenReturn("parent/createParent");
		when(as.validateTrackingId("123", null, true)).thenReturn(true);
		authFilter.filter(requestContext);
		verify(requestContext, never()).abortWith(any(Response.class));
		verify(log, times(1)).debug("filter() checking tracked request");
	}

	@Test
	public void testWhenTrackingIdInvalid() {
		UriInfo uriInfo = mock(UriInfo.class);
		when(requestContext.getUriInfo()).thenReturn(uriInfo);
		when(requestContext.getHeaderString(Constants.TRACKING_ID)).thenReturn("123");
		when(requestContext.hasEntity()).thenReturn(true);
		when(uriInfo.getPath()).thenReturn("parent/login");
		when(as.validateTrackingId("123", null, true)).thenReturn(false);
		authFilter.filter(requestContext);
		verify(requestContext, times(1)).abortWith(any(Response.class));
		verify(log, times(1)).debug("filter() checking tracked request");
		verify(log, times(1)).debug("filter() aborting with status:{}, msg:{}, bold:{}", Response.Status.UNAUTHORIZED,
				"Haven't seen you around these parts!  Try again now, and it outta be all kinds of good.", "Oops!");
	}

	@Test
	public void testWhenValidTrackingIdButNoEntity() {
		UriInfo uriInfo = mock(UriInfo.class);
		when(requestContext.getUriInfo()).thenReturn(uriInfo);
		when(requestContext.getHeaderString(Constants.TRACKING_ID)).thenReturn("123");
		when(requestContext.hasEntity()).thenReturn(false);
		when(uriInfo.getPath()).thenReturn("parent/login");
		when(as.validateTrackingId("123", null, true)).thenReturn(true);
		authFilter.filter(requestContext);
		verify(requestContext, times(1)).abortWith(any(Response.class));
		verify(log, times(1)).debug("filter() checking tracked request");
		verify(log, times(1)).debug("filter() aborting with status:{}, msg:{}, bold:{}", Response.Status.BAD_REQUEST,
				"Something wasn't quite right with the request, can you try again?", "Oops!");
	}
}
