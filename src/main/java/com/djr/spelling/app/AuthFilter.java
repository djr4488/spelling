package com.djr.spelling.app;

import com.djr.spelling.app.services.auth.AuthService;
import org.slf4j.Logger;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Created by IMac on 1/28/2015.
 */
@Provider
@PreMatching
public class AuthFilter implements ContainerRequestFilter {
	@Inject
	private AuthService authService;
	@Inject
	private Logger log;

	@Override
	public void filter(ContainerRequestContext context) {
		log.info("filter() context.requestUriPath:{}", context.getUriInfo().getPath());
		String path = context.getUriInfo().getPath();
		String trackingId;
		String authToken;
		boolean authenticated = false;
		if (path.contains("parent/sp/")) {
			trackingId = context.getHeaderString(Constants.TRACKING_ID);
			authToken = context.getHeaderString(Constants.AUTH_TOKEN);
			authenticated = authService.validateTrackingId(trackingId, authToken, false);
		} else if (path.contains("parent/login")) {
			trackingId = context.getHeaderString(Constants.TRACKING_ID);
			authenticated = authService.validateTrackingId(trackingId, null, true);
		} else {
			authenticated = true;
		}
		if (!authenticated) {
			context.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(getErrorResponse()).build());
		}
	}

	private ErrorResponse getErrorResponse() {
		return new ErrorResponse("Didn't recognize you?  Create an account maybe?", "Ooops!");
	}
}
