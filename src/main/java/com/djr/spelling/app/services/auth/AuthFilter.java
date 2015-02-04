package com.djr.spelling.app.services.auth;

import com.djr.spelling.app.Constants;
import com.djr.spelling.app.ErrorResponse;
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
		String msg = null;
		String bold = null;
		Response.Status status = null;
		boolean authenticated = false;
		if (path.contains("parent/sp/")) {
			log.debug("filter() checking semi-protected request");
			trackingId = context.getHeaderString(Constants.TRACKING_ID);
			authToken = context.getHeaderString(Constants.AUTH_TOKEN);
			authenticated = authService.validateTrackingId(trackingId, authToken, false);
			msg = "Didn't recognize you!  Maybe you typed the wrong password or need to create a user name?";
			bold = "Oops!";
			status = Response.Status.UNAUTHORIZED;
		} else if (path.contains("parent/login") || path.contains("parent/createParent")) {
			log.debug("filter() checking tracked request");
			trackingId = context.getHeaderString(Constants.TRACKING_ID);
			authenticated = authService.validateTrackingId(trackingId, null, true);
			if (!authenticated) {
				msg = "Haven't seen you around these parts!  Try again now, and it outta be all kinds of good.";
				bold = "Oops!";
				status = Response.Status.UNAUTHORIZED;
			} else if (!context.hasEntity()) {
				authenticated = false;
				msg = "Something wasn't quite right with the request, can you try again?";
				bold = "Oops!";
				status = Response.Status.BAD_REQUEST;
			}
		} else {
			log.debug("filter() tracking id request");
			authenticated = true;
		}
		if (!authenticated) {
			log.debug("filter() aborting with status:{}, msg:{}, bold:{}", status, msg, bold);
			context.abortWith(Response.status(status).entity(getErrorResponse(msg, bold)).build());
		}
		log.debug("filter() completed");
	}

	private ErrorResponse getErrorResponse(String msg, String bold) {
		return new ErrorResponse(msg, bold);
	}
}
