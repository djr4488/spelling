package com.djr.spelling.app.parent.restapi;

import com.djr.spelling.User;
import com.djr.spelling.app.Constants;
import com.djr.spelling.app.exceptions.SpellingException;
import com.djr.spelling.app.parent.restapi.model.ChildUserCreateRequest;
import com.djr.spelling.app.parent.restapi.model.ChildUserCreateResponse;
import com.djr.spelling.app.parent.restapi.model.ParentCreateRequest;
import com.djr.spelling.app.parent.restapi.model.ParentCreateResponse;
import com.djr.spelling.app.parent.restapi.model.ParentLoginRequest;
import com.djr.spelling.app.parent.restapi.model.ParentLoginResponse;
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
	@Path("createParentUser")
	public Response createParentUser(ParentCreateRequest request, @Context HttpServletRequest httpReq) {
		String trackingId = (String)httpReq.getSession(false).getAttribute(Constants.TRACKING_ID);
		log.info("createParentUser() request:{}, trackingId:{}", request, trackingId);
		ParentCreateResponse resp;
		Response response;
		if (request != null) {
			try {
				parentService.createParentAccount(request.getUserEntity(), trackingId);
				resp = new ParentCreateResponse("parentLanding");
				response = Response.status(Response.Status.CREATED).entity(resp).build();
			} catch (SpellingException spellingEx) {
				resp = new ParentCreateResponse("It appears the email address already exists.", "Oops!");
				response = Response.status(Response.Status.CONFLICT).entity(resp).build();
			} catch (Exception ex) {
				resp = new ParentCreateResponse("We seemed to have an issue creating your account.  Try again?", "Doh!");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
			}
		} else {
			resp = new ParentCreateResponse("Something wasn't quite right with the request, can you try again?", "Oops!");
			response = Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
		log.info("createUser() completed. trackingId:{}", trackingId);
		return response;
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("createChildUser")
	public Response createChildUser(ChildUserCreateRequest request, @Context HttpServletRequest httpReq) {
		String trackingId = (String)httpReq.getSession(false).getAttribute(Constants.TRACKING_ID);
		log.info("createChildUser() request:{}, trackingId:{}", request, trackingId);
		ChildUserCreateResponse resp;
		Response response;
		if (request != null) {
			try {
				parentService.createChildAccount(request.getChildUserEntity((User)httpReq.getSession(false).getAttribute("user")),
					trackingId);
				resp = new ChildUserCreateResponse("createChildUserLanding");
				response = Response.status(Response.Status.CREATED).entity(resp).build();
			} catch (SpellingException spEx) {
				resp = new ChildUserCreateResponse("It appears the email address already exists.", "Oops!");
				response = Response.status(Response.Status.CONFLICT).entity(resp).build();
			} catch (Exception ex) {
				resp = new ChildUserCreateResponse("We seemed to have an issue creating your account.  Try again?", "Doh!");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
			}
		} else {
			resp = new ChildUserCreateResponse("Something wasn't quite right with the request, can you try again?", "Oops!");
			response = Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
		log.info("createChildUser() completed. trackingId:{}", trackingId);
		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("loginParent")
	public Response login(ParentLoginRequest request, @Context HttpServletRequest httpReq) {
		String trackingId = (String)httpReq.getSession(false).getAttribute(Constants.TRACKING_ID);
		log.info("createChildUser() request:{}, trackingId:{}", request, trackingId);
		ParentLoginResponse resp;
		Response response;
		if (request != null) {
			try {
				parentService.findParentAccount(request.getUserEntity(), trackingId);
				resp = new ParentLoginResponse("parentLanding");
				response = Response.status(Response.Status.CREATED).entity(resp).build();
			} catch (SpellingException spEx) {
				resp = new ParentLoginResponse("It appears the email address already exists.", "Oops!");
				response = Response.status(Response.Status.CONFLICT).entity(resp).build();
			} catch (Exception ex) {
				resp = new ParentLoginResponse("We seemed to have an issue creating your account.  Try again?", "Doh!");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
			}
		} else {
			resp = new ParentLoginResponse("Something wasn't quite right with the request, can you try again?", "Oops!");
			response = Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
		log.info("login() completed. trackingId:{}", trackingId);
		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("editParent")
	public Response editParent() {
		return null;
	}
}
