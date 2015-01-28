package com.djr.spelling.app.parent.restapi;

import com.djr.spelling.ChildUser;
import com.djr.spelling.Sentence;
import com.djr.spelling.SpellingService;
import com.djr.spelling.User;
import com.djr.spelling.Week;
import com.djr.spelling.Word;
import com.djr.spelling.WordLocation;
import com.djr.spelling.app.BaseApi;
import com.djr.spelling.app.Constants;
import com.djr.spelling.app.exceptions.SpellingException;
import com.djr.spelling.app.parent.restapi.model.*;
import com.djr.spelling.app.parent.service.ParentServiceBean;
import com.djr.spelling.app.services.auth.AuthService;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.slf4j.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by danny.rucker on 9/2/14.
 */
@ApplicationScoped
@Path("parent")
public class ParentApi extends BaseApi {
	@Inject
	private Logger log;
	@Inject
	private ParentServiceBean parentService;
	@Inject
	private AuthService authService;
	@Inject
	private SpellingService spellingService;
	@Inject
	private DoubleMetaphone dm;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("createParent")
	public Response createParentUser(@HeaderParam(Constants.TRACKING_ID) String trackingId, ParentCreateRequest request) {
		log.info("createParentUser() request:{}, trackingId:{}", request, trackingId);
		ParentCreateResponse resp;
		Response response;
		if (request != null && authService.validateTrackingId(trackingId, null, true)) {
			if (!parentService.confirmPasswords(request.password, request.confirmPassword)) {
				resp = new ParentCreateResponse("Passwords not the same.", "It seems ");
				response = Response.status(Response.Status.NOT_ACCEPTABLE).entity(resp).build();
				return response;
			}
			try {
				parentService.createParentAccount(request.getUserEntity(authService), trackingId);
				resp = new ParentCreateResponse(Constants.LOGIN_LANDING);
				response = Response.status(Response.Status.CREATED).entity(resp).build();
			} catch (SpellingException spellingEx) {
				resp = new ParentCreateResponse("It appears the email address already exists.", "Oops!");
				response = Response.status(Response.Status.CONFLICT).entity(resp).build();
			} catch (Exception ex) {
				log.error("createParentUser() ", ex);
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

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{parentId}/createChild")
	public Response createChildUser(ChildUserCreateRequest request, @PathParam("parentId") Integer parentId,
	                                @HeaderParam(Constants.TRACKING_ID) String trackingId,
	                                @HeaderParam(Constants.AUTH_TOKEN) String authToken) {
		log.info("createChildUser() request:{}, trackingId:{}, parentId:{}", request, trackingId, parentId);
		ChildUserCreateResponse resp;
		Response response;
		if (request != null && authService.validateTrackingId(trackingId, authToken, false)) {
			if (!parentService.confirmPasswords(request.password, request.confirmPassword)) {
				resp = new ChildUserCreateResponse("Passwords not the same.", "It seems ");
				response = Response.status(Response.Status.NOT_ACCEPTABLE).entity(resp).build();
				return response;
			}
			try {
				parentService.createChildAccount(request.getChildUserEntity(spellingService,
						parentService.findParentAccount(parentId, trackingId), authService), trackingId);
				resp = new ChildUserCreateResponse(Constants.CREATE_CHILD_LANDING);
				resp.authToken = authService.getAuthToken(trackingId);
				response = Response.status(Response.Status.CREATED).entity(resp).build();
			} catch (SpellingException spEx) {
				resp = new ChildUserCreateResponse("Apparently the child user name already exists.", "Oops!");
				response = Response.status(Response.Status.CONFLICT).entity(resp).build();
			} catch (Exception ex) {
				log.error("createChildUser() ", ex);
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
	@Path("login")
	public Response login(@HeaderParam(Constants.TRACKING_ID) String trackingId, ParentLoginRequest request)
	throws Exception {
		log.info("login() request:{}, trackingId:{}", request, trackingId);
		ParentLoginResponse resp;
		Response response;
		if (request != null && authService.validateTrackingId(trackingId, null, true)) {
			User parent = parentService.findParentAccount(request.getUserEntity(authService), trackingId);
			authService.addTrackingId(trackingId, parent.id);
			resp = new ParentLoginResponse("parentLanding");
			resp.authToken = authService.getAuthToken(trackingId);
			resp.id = parent.id;
			response = Response.status(Response.Status.CREATED).entity(resp).build();
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

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{parentId}/edit}")
	public Response editParent(EditParentRequest request, @PathParam(Constants.PARENT_ID) Integer userId,
	                           @HeaderParam(Constants.TRACKING_ID) String trackingId,
	                           @HeaderParam(Constants.AUTH_TOKEN) String authToken) {
		log.info("editParent() request:{}, trackingId:{}, userId:{}", request, trackingId, userId);
		EditParentResponse resp;
		Response response;
		if (request != null && authService.validateTrackingId(trackingId, authToken, false)) {
			if (!parentService.confirmPasswords(request.password, request.confirmPassword)) {
				resp = new EditParentResponse("Passwords not the same.", "It seems ");
				response = Response.status(Response.Status.NOT_ACCEPTABLE).entity(resp).build();
				return response;
			}
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
				log.error("editParent() ", ex);
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

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{parentId}/children")
	public Response findParentChildren(@HeaderParam(Constants.TRACKING_ID) String trackingId,
	                                   @HeaderParam(Constants.AUTH_TOKEN) String authToken,
	                                   @PathParam(Constants.PARENT_ID) String userId) {
		log.info("findParentChildren() trackingId:{}, userId:{}", trackingId, userId);
		FindChildrenResponse resp;
		Response response;
		Integer uid = Integer.parseInt(userId);
		if (authService.validateTrackingId(trackingId, authToken, false)) {
			try {
				User parent = parentService.findParentAccount(uid, trackingId);
				List<ChildUser> children = parentService.findParentChildren(parent, trackingId);
				resp = new FindChildrenResponse();
				resp.parentChildren = new ParentChildren(children);
				resp.forwardTo = Constants.FIND_PARENT_CHILDREN;
				resp.authToken = authService.getAuthToken(trackingId);
				response = Response.status(Response.Status.OK).entity(resp).build();
			} catch (SpellingException spEx) {
				resp = new FindChildrenResponse("There was an issue finding children for this parent.  Try again later?", "Oops!");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
			} catch (Exception ex) {
				log.error("findParentChildren() ", ex);
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
	@Path("{parentId}/child/{childId}")
	public Response getChild(@HeaderParam(Constants.TRACKING_ID) String trackingId, @HeaderParam(Constants.AUTH_TOKEN) String authToken,
	                         @PathParam(Constants.PARENT_ID) Integer parentId, @PathParam(Constants.CHILD_ID) Integer childId) {
		log.info("getChild() trackingId:{}, parentId:{}, childId:{}", trackingId, parentId, childId);
		EditChildResponse resp;
		Response response;
		if (authService.validateTrackingId(trackingId, authToken, false)) {
			try {
				ChildUser child = parentService.findParentChild(childId, trackingId);
				resp = new EditChildResponse(Constants.EDIT_CHILD_LANDING);
				resp.username = child.username;
				resp.childId = child.id;
				resp.authToken = authService.getAuthToken(trackingId);
				response = Response.status(Response.Status.OK).entity(resp).build();
			} catch (SpellingException spEx) {
				resp = new EditChildResponse("There was a problem finding the child by this name.  Try again later?", "Oops!");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
			} catch (Exception ex) {
				log.error("getChild() ", ex);
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
	@Path("{parentId}/editChild/{childId}")
	public Response editChild(EditChildRequest request, @PathParam(Constants.PARENT_ID) Integer parentId,
	                          @PathParam(Constants.CHILD_ID) Integer childId, @HeaderParam(Constants.AUTH_TOKEN) String authToken,
	                          @HeaderParam(Constants.TRACKING_ID) String trackingId) {
		log.info("editChild() request:{}, trackingId:{}", request, trackingId);
		EditChildResponse resp;
		Response response;
		if (request != null && authService.validateTrackingId(trackingId, authToken, false)) {
			if (!parentService.confirmPasswords(request.password, request.confirmPassword)) {
				resp = new EditChildResponse("Passwords not the same.", "It seems ");
				response = Response.status(Response.Status.NOT_ACCEPTABLE).entity(resp).build();
				return response;
			}
			try {
				ChildUser originalUser = parentService.findParentChild(childId, trackingId);
				parentService.editChildPassword(originalUser, authService.getPasswordHash(request.password), trackingId);
				resp = new EditChildResponse(Constants.EDIT_CHILD_LANDING);
				resp.authToken = authService.getAuthToken(trackingId);
				response = Response.status(Response.Status.OK).entity(resp).build();
			} catch (SpellingException spEx) {
				resp = new EditChildResponse("It appears there was a problem changing your password.  Try again later?", "Oops!");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
			} catch (Exception ex) {
				log.error("editChild() ", ex);
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

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{parentId}/word/{childId}")
	public Response addWord(AddWordRequest request, @PathParam(Constants.PARENT_ID) Integer parentId,
	                        @PathParam(Constants.CHILD_ID) Integer childId, @HeaderParam(Constants.AUTH_TOKEN) String authToken,
	                        @HeaderParam(Constants.TRACKING_ID) String trackingId) {
		log.info("addWord() request:{}, trackingId:{}", request, trackingId);
		AddWordResponse resp;
		Response response;
		if (request != null && authService.validateTrackingId(trackingId, authToken, false)) {
			try {
				User parent = parentService.findParentAccount(parentId, trackingId);
				ChildUser child = parentService.findParentChild(childId, trackingId);
				Word word = parentService.createOrFindWord(parent, request.getWordEntity(dm), trackingId);
				Week week = parentService.createOrFindWeek(getWeekEntity(request.startOfWeek, request.endOfWeek), trackingId);
				WordLocation wordLocation = getWordLocationEntity(child, word, week);
				parentService.createOrFindWordLocation(wordLocation, trackingId);
				parentService.createOrFindWordSentence(getSentenceEntity(request.sentence, word), trackingId);
				resp = new AddWordResponse("addWord");
				resp.id = parentId;
				resp.childId = childId;
				resp.authToken = authService.getAuthToken(trackingId);
				resp.forwardTo = Constants.ADD_WORD;
				return Response.status(Response.Status.CREATED).entity(resp).build();
			} catch (SpellingException spEx) {
				resp = new AddWordResponse("Something prevented me from adding this word.", "Can you try again?");
				response = Response.status(Response.Status.CONFLICT).entity(resp).build();
			} catch (Exception ex) {
				log.error("addWord() trackingId:{}, Exception:{}", trackingId, ex);
				resp = new AddWordResponse("It appears there was a problem.  If it keeps not working, send " +
					trackingId + " to djr4488(dot)(at)gmail(dot)com");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
			}
		} else {
			resp = new AddWordResponse("Something wasn't quite right with the request, can you try again?", "Doh!");
			response = Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
		return response;
	}

	private WordLocation getWordLocationEntity(ChildUser child, Word word, Week week) {
		WordLocation wordLocation = new WordLocation();
		wordLocation.grade = child.grade;
		wordLocation.location = child.location;
		wordLocation.word = word;
		wordLocation.week = week;
		return wordLocation;
	}

	private Sentence getSentenceEntity(String sentence, Word word) {
		return new Sentence(sentence, word);
	}

	private Week getWeekEntity(Date startWeek, Date endWeek) {
		return new Week(startWeek, endWeek);
	}
}
