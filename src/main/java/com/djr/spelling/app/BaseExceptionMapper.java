package com.djr.spelling.app;

import javax.ws.rs.core.Response;

/**
 * Created by IMac on 1/31/2015.
 */
public abstract class BaseExceptionMapper {
	public Response getErrorResponse(String msg, String bold, Response.Status status) {
		return getResponse(status, new ErrorResponse(msg, bold));
	}

	public Response getResponse(Response.Status status, Object entity) {
		return Response.status(status).entity(entity).build();
	}
}
