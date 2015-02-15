package com.djr.spelling.app.child.restapi;

import com.djr.spelling.ChildUser;
import com.djr.spelling.SpellingService;
import com.djr.spelling.app.BaseApi;
import com.djr.spelling.app.Constants;
import com.djr.spelling.app.child.exceptions.ChildApiException;
import com.djr.spelling.app.child.model.ChildLoginRequest;
import com.djr.spelling.app.child.model.ChildLoginResponse;
import com.djr.spelling.app.child.model.GetQuizResponse;
import com.djr.spelling.app.child.service.ChildServiceBean;
import com.djr.spelling.app.exceptions.AuthException;
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
	@Path("login")
	public ChildLoginResponse login(@HeaderParam("trackingId") String trackingId, ChildLoginRequest request)
	throws AuthException {
		log.info("login() trackingId:{}, request:{}", trackingId, request);
		ChildLoginResponse resp;
		ChildUser childUser = childServiceBean.findChildUser(request.username, authService.getPasswordHash(request.password),
				trackingId);
		resp = new ChildLoginResponse(Constants.CHILD_QUIZ_LANDING);
		resp.authToken = authService.getAuthToken(trackingId);
		resp.id = childUser.id;
		log.info("login() completed");
		return resp;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("createQuiz/{timeType}/{locationType}/{childId}")
	public GetQuizResponse createQuiz(@HeaderParam("trackingId") String trackingId, @HeaderParam("auth-token") String authToken,
	                        @PathParam("timeType") String timeType, @PathParam("locationType") String locationType,
	                        @PathParam("childId") Integer childId)
	throws ChildApiException {
		log.info("createQuiz() trackingId{}, timeType:{}, locationType:{}, childId:{}", trackingId, timeType, locationType,
				childId);
		GetQuizResponse resp;
		resp = new GetQuizResponse(Constants.TAKE_QUIZ);
		resp.authToken = authService.getAuthToken(trackingId);
		resp.quizWordWrapper = childServiceBean.createQuiz(timeType, locationType, childId, trackingId);
		return resp;
	}
}
