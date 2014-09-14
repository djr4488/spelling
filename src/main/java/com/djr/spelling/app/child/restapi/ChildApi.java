package com.djr.spelling.app.child.restapi;

import com.djr.spelling.ChildUser;
import com.djr.spelling.SpellingService;
import com.djr.spelling.app.BaseApi;
import com.djr.spelling.app.Constants;
import com.djr.spelling.app.child.model.ChildLoginRequest;
import com.djr.spelling.app.child.model.ChildLoginResponse;
import com.djr.spelling.app.child.model.GetQuizResponse;
import com.djr.spelling.app.child.service.ChildServiceBean;
import com.djr.spelling.app.exceptions.SpellingException;
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
	public Response login(@HeaderParam("trackingId") String trackingId, ChildLoginRequest request) {
		log.info("login() trackingId:{}, request:{}", trackingId, request);
		Response response = null;
		ChildLoginResponse resp;
		if (request != null && authService.validateTrackingId(trackingId, null, true)) {
			try {
				ChildUser childUser = childServiceBean.findChildUser(request.username, request.password, trackingId);
				resp = new ChildLoginResponse(Constants.CHILD_QUIZ_LANDING);
				resp.authToken = authService.getAuthToken(trackingId);
				resp.id = childUser.id;
				response = Response.status(Response.Status.OK).entity(resp).build();
			} catch (SpellingException spEx) {
				resp = new ChildLoginResponse("Couldn't match the username and password", "Try again?");
				response = Response.status(Response.Status.UNAUTHORIZED).entity(resp).build();
			} catch (Exception ex) {
				resp = new ChildLoginResponse("Had a problem handling your request.", "Try again?");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
			}
		} else {
			resp = new ChildLoginResponse("Something wasn't right with the request.", "Oops!");
			resp.forwardTo = Constants.CHILD_HOME;
			response = Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
		return response;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("createQuiz/{timeType}/{locationType}/{childId}")
	public Response createQuiz(@HeaderParam("trackingId") String trackingId, @HeaderParam("auth-token") String authToken,
	                        @PathParam("timeType") String timeType, @PathParam("locationType") String locationType,
	                        @PathParam("childId") Integer childId) {
		log.info("createQuiz() trackingId{}, timeType:{}, locationType:{}, childId:{}", trackingId, timeType, locationType,
				childId);
		Response response = null;
		GetQuizResponse resp;
		if (authService.validateTrackingId(trackingId, authToken, false)) {
			try {
				resp = new GetQuizResponse(Constants.TAKE_QUIZ);
				resp.authToken = authService.getAuthToken(trackingId);
				resp.quizWordWrapper = childServiceBean.createQuiz(timeType, locationType, childId, trackingId);
				response = Response.status(Response.Status.OK).entity(resp).build();
			} catch (SpellingException spEx) {
				resp = new GetQuizResponse("Had a problem generating your quiz.", "Try again?");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
			} catch (Exception ex) {
				resp = new GetQuizResponse("Had a problem handling your request.", "Try again?");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).build();
			}
		} else {
			resp = new GetQuizResponse("Something wasn't right with the request", "Oops!");
			response = Response.status(Response.Status.UNAUTHORIZED).entity(resp).build();
		}
		return response;
	}
}
