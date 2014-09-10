package com.djr.spelling.app.child.restapi;

import com.djr.spelling.SpellingService;
import com.djr.spelling.app.BaseApi;
import com.djr.spelling.app.child.model.ChildLoginRequest;
import com.djr.spelling.app.child.service.ChildServiceBean;
import com.djr.spelling.app.services.auth.AuthService;
import org.slf4j.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by danny.rucker on 9/2/14.
 */
@ApplicationScoped
@Path("child")
public class ChildApi extends BaseApi {
	@Inject
	private Logger log;
	@Inject
	private ChildServiceBean childServiceBean;
	@Inject
	private AuthService authService;
	@Inject
	private SpellingService spellingService;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("login/{trackingId}")
	public Response login(@PathParam("trackingId") String trackingId, ChildLoginRequest request) {
		return Response.ok().build();
	}
}
