package com.djr.spelling.app.parent.restapi.exceptionmappers;

import com.djr.spelling.app.parent.exceptions.ParentManageChildrenException;
import com.djr.spelling.app.parent.restapi.ParentApiConstants;
import com.djr.spelling.app.ErrorResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by IMac on 1/28/2015.
 */
public class ParentManageChildrenExceptionMapper implements ExceptionMapper<ParentManageChildrenException> {
	@Override
	public Response toResponse(ParentManageChildrenException e) {
		switch (e.getMessage()) {
			case ParentApiConstants.CHILD_NOT_CONFIRMED: {
				return handleChildNotConfirmed();
			}
			case ParentApiConstants.CHILD_EXISTS: {
				return handleChildExists();
			}
			case ParentApiConstants.CHILD_CREATE_GENERAL_FAIL: {
				return handleChildCreateGeneralFail();
			}
			default: {
				return Response.serverError().build();
			}
		}
	}

	public Response handleChildNotConfirmed() {
		return getParentErrorResponse("Passwords not the same.", "It seems ", Response.Status.NOT_ACCEPTABLE);
	}

	public Response handleChildExists() {
		return getParentErrorResponse("Apparently the child user name already exists.", "Oops!",
				Response.Status.CONFLICT);
	}

	public Response handleChildCreateGeneralFail() {
		return getParentErrorResponse("We seemed to have an issue creating the child's account.  Try again?", "Doh!", Response.Status.INTERNAL_SERVER_ERROR);
	}

	public Response getParentErrorResponse(String msg, String bold, Response.Status status) {
		return getResponse(status, new ErrorResponse(msg, bold));
	}

	public Response getResponse(Response.Status status, Object entity) {
		return Response.status(status).entity(entity).build();
	}
}
