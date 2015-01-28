package com.djr.spelling.app;

import com.djr.spelling.app.exceptions.SpellingException;
import com.djr.spelling.app.parent.restapi.model.ParentLoginResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by IMac on 1/27/2015.
 */
@Provider
public class SpellingExceptionMapper implements ExceptionMapper<Exception> {
	@Override
	public Response toResponse(Exception e) {
		if (e instanceof SpellingException) {
			return handleParentAuthException();
		}
		return Response.serverError().build();
	}

	public Response handleParentAuthException() {
		ParentLoginResponse parentLoginResponse =
				new ParentLoginResponse("Well, wouldn't you know it, can't seem to log you in.", "Oops!");
		return getResponse(Response.Status.UNAUTHORIZED, parentLoginResponse);
	}

	public Response handleParentAuthGeneralException() {
		ParentLoginResponse parentLoginResponse =
				new ParentLoginResponse("", "");
		return getResponse(Response.Status.INTERNAL_SERVER_ERROR, parentLoginResponse);
	}

	public Response getResponse(Response.Status status, Object entity) {
		return Response.status(status).entity(entity).build();
	}
}
