package com.djr.spelling.app.parent.restapi.exceptionmappers;

import com.djr.spelling.app.parent.exceptions.ParentManageChildrenException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by IMac on 1/28/2015.
 */
public class ParentManageChildrenExceptionMapper implements ExceptionMapper<ParentManageChildrenException> {
	@Override
	public Response toResponse(ParentManageChildrenException e) {
		return Response.serverError().build();
	}
}
