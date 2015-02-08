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
import com.djr.spelling.app.exceptions.AuthException;
import com.djr.spelling.app.parent.exceptions.ParentApiException;
import com.djr.spelling.app.parent.exceptions.ParentWordException;
import com.djr.spelling.app.parent.restapi.model.*;
import com.djr.spelling.app.parent.service.ParentServiceBean;
import com.djr.spelling.app.services.auth.AuthService;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

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
	public Response createParentUser(@HeaderParam(Constants.TRACKING_ID) String trackingId, ParentCreateRequest request)
	throws Exception {
		log.info("createParentUser() request:{}, trackingId:{}", request, trackingId);
		ParentCreateResponse resp;
		Response response;
		parentService.confirmPasswords(request.password, request.confirmPassword);
		parentService.createParentAccount(request.getUserEntity(authService), trackingId);
		resp = new ParentCreateResponse(Constants.LOGIN_LANDING);
		response = Response.status(Response.Status.CREATED).entity(resp).build();
		log.info("createUser() completed. trackingId:{}", trackingId);
		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("sp/{parentId}/createChild")
	public Response createChildUser(ChildUserCreateRequest request, @PathParam("parentId") Integer parentId,
	                                @HeaderParam(Constants.TRACKING_ID) String trackingId,
	                                @HeaderParam(Constants.AUTH_TOKEN) String authToken)
	throws AuthException, ParentApiException {
		log.info("createChildUser() request:{}, trackingId:{}, parentId:{}", request, trackingId, parentId);
		ChildUserCreateResponse resp;
		Response response;
		parentService.confirmPasswords(request.password, request.confirmPassword);
		parentService.createChildAccount(request.getChildUserEntity(spellingService,
				parentService.findParentAccount(parentId, trackingId), authService), trackingId);
		resp = new ChildUserCreateResponse(Constants.CREATE_CHILD_LANDING);
		resp.authToken = authService.getAuthToken(trackingId);
		response = Response.status(Response.Status.CREATED).entity(resp).build();
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
		User parent = parentService.findParentAccount(request.getUserEntity(authService), trackingId);
		authService.addTrackingId(trackingId, parent.id);
		resp = new ParentLoginResponse("parentLanding");
		resp.authToken = authService.getAuthToken(trackingId);
		resp.id = parent.id;
		response = Response.status(Response.Status.CREATED).entity(resp).build();
		log.info("login() completed. trackingId:{}", trackingId);
		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("sp/{parentId}/editParent")
	public Response editParent(EditParentRequest request, @PathParam(Constants.PARENT_ID) Integer userId,
	                           @HeaderParam(Constants.TRACKING_ID) String trackingId,
	                           @HeaderParam(Constants.AUTH_TOKEN) String authToken)
	throws Exception {
		log.info("editParent() request:{}, trackingId:{}, userId:{}", request, trackingId, userId);
		EditParentResponse resp;
		Response response;
		parentService.confirmPasswords(request.password, request.confirmPassword);
		User originalUser = parentService.findParentAccount(userId, trackingId);
		parentService.editParentPassword(originalUser, request.getUserEntity(), trackingId);
		resp = new EditParentResponse(Constants.EDIT_PARENT_LANDING);
		resp.authToken = authService.getAuthToken(trackingId);
		response = Response.status(Response.Status.OK).entity(resp).build();
		log.info("editParent() completed. trackingId:{}", trackingId);
		return response;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("sp/{parentId}/children")
	public Response findParentChildren(@HeaderParam(Constants.TRACKING_ID) String trackingId,
	                                   @HeaderParam(Constants.AUTH_TOKEN) String authToken,
	                                   @PathParam(Constants.PARENT_ID) String userId)
	throws ParentApiException, AuthException {
		log.info("findParentChildren() trackingId:{}, userId:{}", trackingId, userId);
		FindChildrenResponse resp;
		Response response;
		Integer uid = Integer.parseInt(userId);
		User parent = parentService.findParentAccount(uid, trackingId);
		List<ChildUser> children = parentService.findParentChildren(parent, trackingId);
		resp = new FindChildrenResponse();
		resp.parentChildren = new ParentChildren(children);
		resp.forwardTo = Constants.FIND_PARENT_CHILDREN;
		resp.authToken = authService.getAuthToken(trackingId);
		response = Response.status(Response.Status.OK).entity(resp).build();
		log.info("findParentChildren() completed. trackingId:{}", trackingId);
		return response;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("sp/{parentId}/child/{childId}")
	public Response getChild(@HeaderParam(Constants.TRACKING_ID) String trackingId, @HeaderParam(Constants.AUTH_TOKEN) String authToken,
	                         @PathParam(Constants.PARENT_ID) Integer parentId, @PathParam(Constants.CHILD_ID) Integer childId)
	throws ParentApiException {
		log.info("getChild() trackingId:{}, parentId:{}, childId:{}", trackingId, parentId, childId);
		EditChildResponse resp;
		Response response;
		ChildUser child = parentService.findParentChild(childId, trackingId);
		resp = new EditChildResponse(Constants.EDIT_CHILD_LANDING);
		resp.username = child.username;
		resp.childId = child.id;
		resp.authToken = authService.getAuthToken(trackingId);
		response = Response.status(Response.Status.OK).entity(resp).build();
		log.info("editChild() completed. trackingId:{}", trackingId);
		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("sp/{parentId}/editChild/{childId}")
	public Response editChild(EditChildRequest request, @PathParam(Constants.PARENT_ID) Integer parentId,
	                          @PathParam(Constants.CHILD_ID) Integer childId, @HeaderParam(Constants.AUTH_TOKEN) String authToken,
	                          @HeaderParam(Constants.TRACKING_ID) String trackingId)
	throws AuthException, ParentApiException {
		log.info("editChild() request:{}, trackingId:{}", request, trackingId);
		EditChildResponse resp;
		Response response;
		parentService.confirmPasswords(request.password, request.confirmPassword);
		ChildUser originalUser = parentService.findParentChild(childId, trackingId);
		User parent = parentService.findParentAccount(parentId, trackingId);
		parentService.editChild(originalUser, request.getChildUser(spellingService, authService, parent), trackingId);
		resp = new EditChildResponse(Constants.EDIT_CHILD_LANDING);
		resp.authToken = authService.getAuthToken(trackingId);
		response = Response.status(Response.Status.OK).entity(resp).build();
		log.info("editChild() completed. trackingId:{}", trackingId);
		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("sp/{parentId}/addWord/{childId}")
	public Response addWord(AddWordRequest request, @PathParam(Constants.PARENT_ID) Integer parentId,
	                        @PathParam(Constants.CHILD_ID) Integer childId, @HeaderParam(Constants.AUTH_TOKEN) String authToken,
	                        @HeaderParam(Constants.TRACKING_ID) String trackingId)
	throws AuthException, ParentApiException, ParentWordException {
		log.info("addWord() request:{}, trackingId:{}", request, trackingId);
		AddWordResponse resp;
		User parent = parentService.findParentAccount(parentId, trackingId);
		ChildUser child = parentService.findParentChild(childId, trackingId);
		Word word = parentService.createOrFindWord(parent, request.getWordEntity(dm), trackingId);
		DateTime startOfWeek = DateTime.parse(request.startOfWeek).withTimeAtStartOfDay();
		DateTime endOfWeek = DateTime.parse(request.endOfWeek).withTimeAtStartOfDay();
		Week week = parentService.createOrFindWeek(getWeekEntity(startOfWeek.toDate(), endOfWeek.toDate()), trackingId);
		WordLocation wordLocation = getWordLocationEntity(child, word, week);
		parentService.createOrFindWordLocation(wordLocation, trackingId);
		parentService.createOrFindWordSentence(getSentenceEntity(request.sentence, word), trackingId);
		resp = new AddWordResponse("addWord");
		resp.id = parentId;
		resp.childId = childId;
		resp.authToken = authService.getAuthToken(trackingId);
		resp.forwardTo = Constants.ADD_WORD;
		log.info("addWord() completed trackingId:{}", trackingId);
		return Response.status(Response.Status.CREATED).entity(resp).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("sp/{parentId}/editWord")
	public Response editWord(EditWordRequest request, @PathParam(Constants.PARENT_ID) Integer parentId,
	                         @HeaderParam(Constants.AUTH_TOKEN) String authToken,
	                         @HeaderParam(Constants.TRACKING_ID) String trackingId)
	throws AuthException, ParentApiException, ParentWordException {
		log.info("editWord() request:{}, parentId:{}, trackingId:{}", request, parentId, trackingId);
		EditWordResponse resp;
		User parent = parentService.findParentAccount(parentId, trackingId);
		Word originalWord = parentService.findWord(request.getWordEntity(dm), trackingId);
		parentService.editWord(parent, originalWord, request.getEditedWordEntity(dm), trackingId);
		resp = new EditWordResponse();
		resp.wordId = originalWord.id;
		resp.authToken = authService.getAuthToken(trackingId);
		resp.forwardTo = Constants.PARENT_LANDING;
		log.debug("editWord() completed trackingId:{}", trackingId);
		return Response.status(Response.Status.OK).entity(resp).build();
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
