package com.djr.spelling.app.parent.restapi.exceptionmappers;

import com.djr.spelling.app.parent.exceptions.ParentAuthException;
import com.djr.spelling.app.parent.restapi.ParentApiConstants;
import com.djr.spelling.app.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by IMac on 1/27/2015.
 */
@Provider
public class ParentAuthExceptionMapper implements ExceptionMapper<ParentAuthException> {
	private static final Logger log = LoggerFactory.getLogger(ParentAuthExceptionMapper.class);
	@Context
	private HttpServletRequest request;
	@Override
	public Response toResponse(ParentAuthException e) {
		log.error("toResponse() request:{}, exception:{}", request, e);
		switch(e.getMessage()) {
			case ParentApiConstants.USER_NOT_FOUND: {
				return handleParentAuthException();
			}
			case ParentApiConstants.GENERAL_AUTH: {
				return handleParentAuthGeneralException();
			}
			case ParentApiConstants.NOT_CONFIRMED: {
				return handleNotConfirmedPassword();
			}
			case ParentApiConstants.EMAIL_EXISTS: {
				return handleEmailExists();
			}
			case ParentApiConstants.GENERAL_CREATE: {
				return handleGeneralCreate();
			}
			case ParentApiConstants.CREATE_INVALID_TRACKING: {
				return handleCreateInvalidTracking();
			}
			case ParentApiConstants.FIND_PARENT_BY_ID: {
				return handleFindParentById();
			}
			case ParentApiConstants.FIND_PARENT_BY_ID_GENERAL_FAILURE: {
				return handleFindParentByIdGeneralFailure();
			}
			default: {
				return Response.serverError().build();
			}
		}
	}

	public Response handleParentAuthException() {
		return getParentErrorResponse("Well, wouldn't you know it, can't seem to log you in.", "Oops!",
				Response.Status.UNAUTHORIZED);
	}

	public Response handleParentAuthGeneralException() {
		return getParentErrorResponse("seems we cannot log anybody in right now.  Try again?",
				"Doh! It is bad when it ", Response.Status.INTERNAL_SERVER_ERROR);
	}

	public Response handleNotConfirmedPassword() {
		return getParentErrorResponse("your passwords not the same.", "It seems ", Response.Status.NOT_ACCEPTABLE);
	}

	public Response handleEmailExists() {
		return getParentErrorResponse("It appears the email address already exists.", "Oops!", Response.Status.CONFLICT);
	}

	public Response handleGeneralCreate() {
		return getParentErrorResponse("We seemed to have an issue creating your account.  Try again?", "Doh!",
				Response.Status.INTERNAL_SERVER_ERROR);
	}

	public Response handleCreateInvalidTracking() {
		return getParentErrorResponse("Something wasn't quite right with the request, can you try again?", "Oops!",
				Response.Status.BAD_REQUEST);
	}

	public Response handleFindParentById() {
		return getParentErrorResponse("We had a problem finding you!  Try again later?", "Oops!",
				Response.Status.INTERNAL_SERVER_ERROR);
	}

	public Response handleFindParentByIdGeneralFailure() {
		return getParentErrorResponse("It appears there was a problem changing your password.  Try again later?", "Doh!",
				Response.Status.INTERNAL_SERVER_ERROR);
	}

	public Response getParentErrorResponse(String msg, String bold, Response.Status status) {
		return getResponse(status, new ErrorResponse(msg, bold));
	}

	public Response getResponse(Response.Status status, Object entity) {
		return Response.status(status).entity(entity).build();
	}
}
