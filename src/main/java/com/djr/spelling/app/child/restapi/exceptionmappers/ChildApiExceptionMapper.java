package com.djr.spelling.app.child.restapi.exceptionmappers;

import com.djr.spelling.app.BaseExceptionMapper;
import com.djr.spelling.app.child.ChildConstants;
import com.djr.spelling.app.child.exceptions.ChildApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by IMac on 1/31/2015.
 */
public class ChildApiExceptionMapper extends BaseExceptionMapper implements ExceptionMapper<ChildApiException> {
	private static final Logger log = LoggerFactory.getLogger(ChildApiException.class);
	@Context
	private HttpServletRequest request;
	@Override
	public Response toResponse(ChildApiException e) {
		log.error("toResponse() request:{}, ex:{}", request, e);
		switch (e.getMessage()) {
			case ChildConstants.CREATE_QUIZ_NO_RESULT:
			case ChildConstants.CREATE_QUIZ_FAILED: {
				return handleCreateQuizFailed();
			}
			default: {
				return Response.serverError().build();
			}
		}
	}

	public Response handleCreateQuizFailed() {
		return getErrorResponse("Had a problem generating your quiz.", "Try again?", Response.Status.INTERNAL_SERVER_ERROR);
	}
}
