package com.djr.spelling.app.parent.restapi;

import com.djr.spelling.ChildUser;
import com.djr.spelling.User;
import com.djr.spelling.app.Constants;
import com.djr.spelling.app.exceptions.SpellingException;
import com.djr.spelling.app.parent.restapi.model.*;
import com.djr.spelling.app.parent.service.ParentServiceBean;
import com.djr.spelling.app.services.auth.AuthService;
import org.slf4j.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

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
	@Inject
	private AuthService authService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getTrackingId")
	public Response getTrackingId() {
		TrackingIdResponse tir = new TrackingIdResponse();
		tir.trackingId = UUID.randomUUID().toString();
		tir.forwardTo = Constants.HOME;
		return Response.ok().entity(tir).build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("createParentUser/{trackingId}")
	public Response createParentUser(@PathParam(Constants.TRACKING_ID) String trackingId, ParentCreateRequest request) {
		log.info("createParentUser() request:{}, trackingId:{}", request, trackingId);
		ParentCreateResponse resp;
		Response response;
		if (request != null) {
			try {
				parentService.createParentAccount(request.getUserEntity(), trackingId);
				resp = new ParentCreateResponse(Constants.LOGIN_LANDING);
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
	@Path("createChildUser/{trackingId}")
	public Response createChildUser(ChildUserCreateRequest request, @PathParam(Constants.TRACKING_ID) String trackingId,
	                                @HeaderParam(Constants.AUTH_TOKEN) String authToken) {
		log.info("createChildUser() request:{}, trackingId:{}", request, trackingId);
		ChildUserCreateResponse resp;
		Response response;
		if (request != null && authService.validateTrackingId(trackingId, authToken, false)) {
			try {
				parentService.createChildAccount(
						request.getChildUserEntity(parentService.findParentAccount(authService.getUserId(trackingId),
								trackingId)),
						trackingId);
				resp = new ChildUserCreateResponse(Constants.CREATE_CHILD_LANDING);
				resp.authToken = authService.getAuthToken(trackingId);
				response = Response.status(Response.Status.CREATED).entity(resp).build();
			} catch (SpellingException spEx) {
				resp = new ChildUserCreateResponse("Apparently the child user name already exists.", "Oops!");
				response = Response.status(Response.Status.CONFLICT).entity(resp).build();
			} catch (Exception ex) {
				resp = new ChildUserCreateResponse("We seemed to have an issue creating the child's account.  Try again?", "Doh!");
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
	@Path("loginParent/{trackingId}")
	public Response login(@PathParam(Constants.TRACKING_ID) String trackingId, ParentLoginRequest request) {
		log.info("login() request:{}, trackingId:{}", request, trackingId);
		ParentLoginResponse resp;
		Response response;
		if (request != null) {
			try {
				User parent = parentService.findParentAccount(request.getUserEntity(), trackingId);
				resp = new ParentLoginResponse("parentLanding");
				resp.authToken = authService.getAuthToken(trackingId);
				resp.id = parent.id;
				authService.addTrackingId(trackingId, parent.id);
				response = Response.status(Response.Status.CREATED).entity(resp).build();
			} catch (SpellingException spEx) {
				resp = new ParentLoginResponse("Well, wouldn't you know it, can't seem to log you in.", "Oops!");
				response = Response.status(Response.Status.UNAUTHORIZED).entity(resp).build();
			} catch (Exception ex) {
				resp = new ParentLoginResponse("We seem to have a problem figuring out how to login today.  Try again?", "Doh!");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
			}
		} else if (request == null) {
			resp = new ParentLoginResponse("Something wasn't quite right with the request, can you try again?", "Oops!");
			response = Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		} else {
			resp = new ParentLoginResponse("Request invalid", "Oops!");
			resp.forwardTo = Constants.LOGIN_LANDING;
			response = Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
		log.info("login() completed. trackingId:{}", trackingId);
		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("editParent/{trackingId}/{parentId}")
	public Response editParent(EditParentRequest request, @PathParam(Constants.TRACKING_ID) String trackingId,
	                           @PathParam(Constants.PARENT_ID) Integer userId, @HeaderParam(Constants.AUTH_TOKEN) String authToken) {
		log.info("editParent() request:{}, trackingId:{}, userId:{}", request, trackingId, userId);
		EditParentResponse resp;
		Response response;
		if (request != null && authService.validateTrackingId(trackingId, authToken, false)) {
			try {
				User originalUser = parentService.findParentAccount(userId, trackingId);
				parentService.editParentPassword(originalUser, request.getUserEntity(), trackingId);
				resp = new EditParentResponse(Constants.EDIT_PARENT_LANDING);
				resp.authToken = authService.getAuthToken(trackingId);
				response = Response.status(Response.Status.OK).entity(resp).build();
			} catch (SpellingException spEx) {
				resp = new EditParentResponse("It appears there was a problem changing your password.  Try again later?", "Oops!");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
			} catch (Exception ex) {
				resp = new EditParentResponse("It appears there was a problem changing your password.  Try again later?", "Doh!");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
			}
		} else {
			resp = new EditParentResponse("Something wasn't quite right with the request, can you try again?", "Oops!");
			response = Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
		log.info("editParent() completed. trackingId:{}", trackingId);
		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("findParentChildren//{trackingId}/{parentId}")
	public Response findParentChildren(@PathParam(Constants.TRACKING_ID) String trackingId,
	                                   @PathParam(Constants.PARENT_ID) Integer userId, @HeaderParam(Constants.AUTH_TOKEN) String authToken) {
		log.info("findParentChildren() trackingId:{}, userId:{}", trackingId, userId);
		FindChildrenResponse resp;
		Response response;
		if (authService.validateTrackingId(trackingId, authToken, false)) {
			try {
				User parent = parentService.findParentAccount(userId, trackingId);
				List<ChildUser> children = parentService.findParentChildren(parent, trackingId);
				resp = new FindChildrenResponse();
				resp.setParentChildren(children);
				resp.forwardTo = Constants.FIND_PARENT_CHILDREN;
				resp.authToken = authService.getAuthToken(trackingId);
				response = Response.status(Response.Status.OK).entity(resp).build();
			} catch (SpellingException spEx) {
				resp = new FindChildrenResponse("There was an issue finding children for this parent.  Try again later?", "Oops!");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
			} catch (Exception ex) {
				resp = new FindChildrenResponse("We had a pretty big oops moment.  Try again later?", "Doh!");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
			}
		} else {
			resp = new FindChildrenResponse("Something wasn't quite right with the request, can you try again?", "Oops!");
			response = Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
		log.info("findParentChildren() completed. trackingId:{}", trackingId);
		return response;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getChild/{trackingId}/{parentId}/{childId}")
	public Response getChild(@PathParam(Constants.TRACKING_ID) String trackingId,
	                         @PathParam(Constants.PARENT_ID) Integer parentId, @PathParam(Constants.CHILD_ID) Integer childId,
	                         @HeaderParam(Constants.AUTH_TOKEN) String authToken) {
		log.info("getChild() trackingId:{}, parentId:{}, childId:{}", trackingId, parentId, childId);
		EditChildResponse resp;
		Response response;
		if (authService.validateTrackingId(trackingId, authToken, false)) {
			try {
				ChildUser child = parentService.findParentChild(childId, trackingId);
				resp = new EditChildResponse(Constants.EDIT_CHILD_LANDING);
				resp.username = child.username;
				resp.authToken = authService.getAuthToken(trackingId);
				response = Response.status(Response.Status.OK).entity(resp).build();
			} catch (SpellingException spEx) {
				resp = new EditChildResponse("There was a problem finding the child by this name.  Try again later?", "Oops!");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
			} catch (Exception ex) {
				resp = new EditChildResponse("Big oops moment, sorry.  Try again later?", "Doh!");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
			}
		} else {
			resp = new EditChildResponse("Something wasn't quite right with the request, can you try again?", "Oops!");
			response = Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
		log.info("editChild() completed. trackingId:{}", trackingId);
		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("editChild/{trackingId}/{parentId}/{childId}")
	public Response editChild(EditChildRequest request, @PathParam(Constants.TRACKING_ID) String trackingId,
	                          @PathParam(Constants.PARENT_ID) Integer parentId, @PathParam(Constants.CHILD_ID) Integer childId,
	                          @HeaderParam(Constants.AUTH_TOKEN) String authToken) {
		log.info("editChild() request:{}, trackingId:{}", request, trackingId);
		EditChildResponse resp;
		Response response;
		if (request != null && authService.validateTrackingId(trackingId, authToken, false)) {
			try {
				ChildUser originalUser = parentService.findParentChild(childId, trackingId);
				parentService.editChildPassword(originalUser, request.getUserEntity(), trackingId);
				resp = new EditChildResponse(Constants.EDIT_CHILD_LANDING);
				resp.authToken = authService.getAuthToken(trackingId);
				response = Response.status(Response.Status.OK).entity(resp).build();
			} catch (SpellingException spEx) {
				resp = new EditChildResponse("It appears there was a problem changing your password.  Try again later?", "Oops!");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
			} catch (Exception ex) {
				resp = new EditChildResponse("It appears there was a problem changing your password.  Try again later?", "Doh!");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
			}
		} else {
			resp = new EditChildResponse("Something wasn't quite right with the request, can you try again?", "Oops!");
			response = Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
		log.info("editChild() completed. trackingId:{}", trackingId);
		return response;
	}


}
