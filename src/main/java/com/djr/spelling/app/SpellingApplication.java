package com.djr.spelling.app;

import com.djr.spelling.app.child.restapi.ChildApi;
import com.djr.spelling.app.child.restapi.exceptionmappers.ChildApiExceptionMapper;
import com.djr.spelling.app.parent.restapi.ParentApi;
import com.djr.spelling.app.parent.restapi.exceptionmappers.ParentApiExceptionMapper;
import com.djr.spelling.app.parent.restapi.exceptionmappers.ParentWordExceptionMapper;
import com.djr.spelling.app.services.auth.AuthExceptionMapper;
import com.djr.spelling.app.services.auth.AuthFilter;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by danny.rucker on 9/2/14.
 */
@ApplicationPath("/api")
public class SpellingApplication extends Application {
	@Override
	public Set<Class<?>> getClasses() {
		return new HashSet<Class<?>>(Arrays.asList(ParentApi.class, ChildApi.class, SpellingExceptionMapper.class,
				AuthExceptionMapper.class, ParentApiExceptionMapper.class, AuthFilter.class,
				ParentWordExceptionMapper.class, ChildApiExceptionMapper.class ));
	}
}
