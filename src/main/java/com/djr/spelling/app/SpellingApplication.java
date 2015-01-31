package com.djr.spelling.app;

import com.djr.spelling.app.child.restapi.ChildApi;
import com.djr.spelling.app.parent.restapi.ParentApi;
import com.djr.spelling.app.parent.restapi.exceptionmappers.ParentAuthExceptionMapper;
import com.djr.spelling.app.parent.restapi.exceptionmappers.ParentManageChildrenExceptionMapper;
import com.djr.spelling.app.parent.restapi.exceptionmappers.ParentWordExceptionMapper;
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
				ParentAuthExceptionMapper.class, ParentManageChildrenExceptionMapper.class, AuthFilter.class,
				ParentWordExceptionMapper.class ));
	}
}
