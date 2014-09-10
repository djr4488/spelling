package com.djr.spelling.app;

import com.djr.spelling.app.services.auth.AuthService;
import org.slf4j.Logger;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * Created by IMac on 9/9/2014.
 */
public class BaseApi {
	@Inject
	private AuthService authService;
	@Inject
	private Logger log;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getTrackingId")
	public Response getTrackingId(@Context HttpServletRequest request) {
		log.info("getTrackingId()");
		TrackingIdResponse tir = new TrackingIdResponse();
		tir.trackingId = UUID.randomUUID().toString();
		tir.forwardTo = Constants.HOME;
		authService.addTrackingId(tir.trackingId, null);
		log.debug("getTrackingId() trackingId:{}, ipAddress:{}, requestedResource:{}", tir.trackingId, request.getRemoteAddr(),
				request.getRequestURI());
		return Response.ok().entity(tir).build();
	}
}
