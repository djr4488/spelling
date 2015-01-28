package com.djr.spelling.app.parent.restapi;

import com.djr.spelling.app.parent.exceptions.ParentAuthException;
import com.djr.spelling.app.parent.restapi.model.ParentLoginResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by IMac on 1/27/2015.
 */
@Provider
public class ParentAuthExceptionMapper implements ExceptionMapper<ParentAuthException> {
	@Override
	public Response toResponse(ParentAuthException e) {
		if (e.getMessage().contains("NOT FOUND")) {
			return handleParentAuthException();
		} else if (e.getMessage().contains("GENERAL")) {
			return handleParentAuthGeneralException();
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
				new ParentLoginResponse("seems we cannot log anybody in right now.  Try again?", "Doh! It is bad when it ");
		return getResponse(Response.Status.INTERNAL_SERVER_ERROR, parentLoginResponse);
	}

	public Response getResponse(Response.Status status, Object entity) {
		return Response.status(status).entity(entity).build();
	}
}
