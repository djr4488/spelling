package com.djr.spelling.app.parent.restapi.exceptionmappers;

import com.djr.spelling.app.parent.exceptions.ParentManageChildrenException;
import com.djr.spelling.app.parent.restapi.ParentApiConstants;
import com.djr.spelling.app.ErrorResponse;
import com.djr.spelling.app.parent.restapi.model.FindChildrenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by IMac on 1/28/2015.
 */
public class ParentManageChildrenExceptionMapper implements ExceptionMapper<ParentManageChildrenException> {
	private static final Logger log = LoggerFactory.getLogger(ParentManageChildrenExceptionMapper.class);
	@Context
	private HttpServletRequest request;
	@Override
	public Response toResponse(ParentManageChildrenException e) {
		log.error("toResponse() request:{}, ex:{}", request, e);
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
			case ParentApiConstants.NO_CHILDREN_FOUND: {
				return handleNoChildrenFound();
			}
			case ParentApiConstants.FIND_PARENT_CHILDREN_FAILED: {
				return handleFindParentChildrenFailed();
			}
			case ParentApiConstants.NO_CHILD_BY_ID: {
				return handleNoChildById();
			}
			case ParentApiConstants.FIND_PARENT_CHILD_FAILED: {
				return handleFindParentChildFailed();
			}
			case ParentApiConstants.EDIT_CHILD_PASSWORD_FAILED: {
				return handleEditChildPasswordFailed();
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

	public Response handleNoChildrenFound() {
		return getFindChildrenResponse("We did find that you had any children.  Do you need to create them?", "Oops!", Response.Status.NOT_FOUND);
	}

	public Response handleFindParentChildrenFailed() {
		return getParentErrorResponse("We had a pretty big oops moment.  Try again later?", "Doh!", Response.Status.INTERNAL_SERVER_ERROR);
	}

	public Response handleNoChildById() {
		return getParentErrorResponse("There was a problem finding the child by this name.  Try again later?", "Oops!", Response.Status.NOT_FOUND);
	}

	public Response handleFindParentChildFailed() {
		return getParentErrorResponse("Big oops moment, sorry.  Try again later?", "Doh!", Response.Status.INTERNAL_SERVER_ERROR);
	}

	public Response handleEditChildPasswordFailed() {
		return getParentErrorResponse("It appears there was a problem changing your password.  Try again later?", "Oops!", Response.Status.INTERNAL_SERVER_ERROR);
	}

	public Response getFindChildrenResponse(String msg, String bold, Response.Status status) {
		return getResponse(status, new FindChildrenResponse(msg, bold));
	}

	public Response getParentErrorResponse(String msg, String bold, Response.Status status) {
		return getResponse(status, new ErrorResponse(msg, bold));
	}

	public Response getResponse(Response.Status status, Object entity) {
		return Response.status(status).entity(entity).build();
	}
}
