package com.djr.spelling.app.parent.restapi;

import com.djr.spelling.app.parent.model.UserCreateRequest;
import com.djr.spelling.app.parent.model.UserLoginRequest;
import com.djr.spelling.app.parent.service.ParentServiceBean;
import org.slf4j.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by danny.rucker on 9/2/14.
 */
@ApplicationScoped
@Path("parent")
public class ParentApi {
	@Inject
	private Logger log;
	@Inject
	private ParentServiceBean parentService;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("createUser")
	public Response createUser(UserCreateRequest request) {
		return Response.ok().build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("login")
	public Response login(UserLoginRequest request) {
		return Response.ok().build();
	}
}
