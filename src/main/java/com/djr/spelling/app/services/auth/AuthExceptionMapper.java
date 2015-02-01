package com.djr.spelling.app.services.auth;

import com.djr.spelling.app.BaseExceptionMapper;
import com.djr.spelling.app.exceptions.AuthException;
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
public class AuthExceptionMapper extends BaseExceptionMapper implements ExceptionMapper<AuthException> {
	private static final Logger log = LoggerFactory.getLogger(AuthExceptionMapper.class);
	@Context
	private HttpServletRequest request;
	@Override
	public Response toResponse(AuthException e) {
		log.error("toResponse() request:{}, exception:{}", request, e);
		switch(e.getMessage()) {
			case AuthConstants.USER_NOT_FOUND: {
				return handleAuthException();
			}
			case AuthConstants.GENERAL_AUTH: {
				return handleAuthGeneralException();
			}
			case AuthConstants.NOT_CONFIRMED: {
				return handleNotConfirmedPassword();
			}
			case AuthConstants.EMAIL_EXISTS: {
				return handleEmailExists();
			}
			case AuthConstants.GENERAL_CREATE: {
				return handleGeneralCreate();
			}
			case AuthConstants.CREATE_INVALID_TRACKING: {
				return handleCreateInvalidTracking();
			}
			default: {
				return Response.serverError().build();
			}
		}
	}

	public Response handleAuthException() {
		return getErrorResponse("Well, wouldn't you know it, can't seem to log you in.", "Oops!",
				Response.Status.UNAUTHORIZED);
	}

	public Response handleAuthGeneralException() {
		return getErrorResponse("seems we cannot log anybody in right now.  Try again?",
				"Doh! It is bad when it ", Response.Status.INTERNAL_SERVER_ERROR);
	}

	public Response handleNotConfirmedPassword() {
		return getErrorResponse("your passwords not the same.", "It seems ", Response.Status.NOT_ACCEPTABLE);
	}

	public Response handleEmailExists() {
		return getErrorResponse("It appears the email address already exists.", "Oops!", Response.Status.CONFLICT);
	}

	public Response handleGeneralCreate() {
		return getErrorResponse("We seemed to have an issue creating your account.  Try again?", "Doh!",
				Response.Status.INTERNAL_SERVER_ERROR);
	}

	public Response handleCreateInvalidTracking() {
		return getErrorResponse("Something wasn't quite right with the request, can you try again?", "Oops!",
				Response.Status.BAD_REQUEST);
	}
}
