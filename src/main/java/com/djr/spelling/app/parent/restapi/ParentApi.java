package com.djr.spelling.app.parent.restapi;

import com.djr.spelling.User;
import com.djr.spelling.app.exceptions.SpellingException;
import com.djr.spelling.app.parent.restapi.model.ChildUserCreateRequest;
import com.djr.spelling.app.parent.restapi.model.ChildUserCreateResponse;
import com.djr.spelling.app.parent.restapi.model.UserCreateRequest;
import com.djr.spelling.app.parent.restapi.model.UserCreateResponse;
import com.djr.spelling.app.UserLoginRequest;
import com.djr.spelling.app.parent.service.ParentServiceBean;
import org.slf4j.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
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

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("createUser")
	public Response createUser(UserCreateRequest request) {
		log.info("createUser() request:{}", request);
		UserCreateResponse createResp;
		Response response;
		if (request != null) {
			try {
				parentService.createParentAccount(request.getUserEntity());
				createResp = new UserCreateResponse("parentLanding");
				response = Response.status(Response.Status.CREATED).entity(createResp).build();
			} catch (SpellingException spellingEx) {
				createResp = new UserCreateResponse("It appears the email address already exists.", "Oops!");
				response = Response.status(Response.Status.CONFLICT).entity(createResp).build();
			} catch (Exception ex) {
				createResp = new UserCreateResponse("We seemed to have an issue creating your account.  Try again?", "Doh!");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(createResp).build();
			}
		} else {
			createResp = new UserCreateResponse("Something wasn't quite right with the request, can you try again?", "Oops!");
			response = Response.status(Response.Status.BAD_REQUEST).entity(createResp).build();
		}
		log.info("createUser() completed");
		return response;
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("createChildUser")
	public Response createChildUser(ChildUserCreateRequest request, @Context HttpServletRequest httpRequest) {
		log.info("createChildUser() request:{}", request);
		ChildUserCreateResponse createResponse;
		Response response;
		if (request != null) {
			try {
				parentService.createChildAccount(request.getChildUserEntity((User)httpRequest.getSession(false).getAttribute("user")));
				createResponse = new ChildUserCreateResponse("createChildUserLanding");
				response = Response.status(Response.Status.CREATED).entity(createResponse).build();
			} catch (SpellingException spEx) {
				createResponse = new ChildUserCreateResponse("It appears the email address already exists.", "Oops!");
				response = Response.status(Response.Status.CONFLICT).entity(createResponse).build();
			} catch (Exception ex) {
				createResponse = new ChildUserCreateResponse("We seemed to have an issue creating your account.  Try again?", "Doh!");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(createResponse).build();
			}
		} else {
			createResponse = new ChildUserCreateResponse("Something wasn't quite right with the request, can you try again?", "Oops!");
			response = Response.status(Response.Status.BAD_REQUEST).entity(createResponse).build();
		}
		log.info("createChildUser() completed");
		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("login")
	public Response login(UserLoginRequest request) {
		return Response.ok().build();
	}
}
