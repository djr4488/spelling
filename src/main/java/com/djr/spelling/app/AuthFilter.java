package com.djr.spelling.app;

import com.djr.spelling.app.services.auth.AuthService;
import org.slf4j.Logger;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by IMac on 1/28/2015.
 */
@WebFilter(
		urlPatterns = "/api/parent/sp/*"
)
public class AuthFilter implements Filter {
	@Inject
	private AuthService authService;
	@Inject
	private Logger log;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		String trackingId = req.getHeader(Constants.TRACKING_ID);
		String authToken = req.getHeader(Constants.AUTH_TOKEN);
		log.debug("doFilter() trackingId:{}, authToken:{}", trackingId, authToken);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
}
