package com.djr.spelling.app.filters;

import com.djr.spelling.app.Constants;
import org.slf4j.Logger;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by danny.rucker on 9/4/14.
 */
@WebFilter(filterName="trackingFilter",
    urlPatterns={"/api/parent/createParentUser", "/api/parent/loginParent", "/api/child/loginChild"})
public class TrackingFilter implements Filter {
	@Inject
	private Logger log;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) servletRequest;
		HttpSession session = req.getSession(false);
		String trackingId = null;
		log.info("doFilter() start");
		if (session != null) {
			trackingId = (String)session.getAttribute(Constants.TRACKING_ID);
		} else {
			session = req.getSession();
		}
		if (trackingId == null || trackingId.trim().length() == 0) {
			trackingId = UUID.randomUUID().toString();
			session.setAttribute(Constants.TRACKING_ID, trackingId);
		}
	}

	@Override
	public void destroy() {

	}
}
