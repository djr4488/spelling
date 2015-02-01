package com.djr.spelling.app.parent.restapi.exceptionmappers;

import com.djr.spelling.app.BaseExceptionMapper;
import com.djr.spelling.app.parent.exceptions.ParentWordException;
import com.djr.spelling.app.parent.ParentApiConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by IMac on 1/31/2015.
 */
public class ParentWordExceptionMapper extends BaseExceptionMapper implements ExceptionMapper<ParentWordException> {
	private static final Logger log = LoggerFactory.getLogger(ParentWordExceptionMapper.class);
	@Context
	private HttpServletRequest request;
	@Override
	public Response toResponse(ParentWordException e) {
		log.error("toResponse() request:{}, ex:{}", request, e);
		switch (e.getMessage()) {
			case ParentApiConstants.CREATE_OR_FIND_WORD_FAILED:
			case ParentApiConstants.CREATE_OR_FIND_WEEK_FAILED:
			case ParentApiConstants.CREATE_OR_FIND_WORD_LOCATION_FAILED:
			case ParentApiConstants.CREATE_OR_FIND_WORD_SENTENCE_FAILED: {
				return handleCreateOrFindWordFailed();
			}
			default: {
				return Response.serverError().build();
			}
		}
	}

	public Response handleCreateOrFindWordFailed() {
		return getErrorResponse("Something prevented me from adding this word.", "Can you try again?",
				Response.Status.INTERNAL_SERVER_ERROR);
	}
}
