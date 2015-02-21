package com.djr.spelling.app.services.auth;

import com.djr.spelling.app.services.auth.model.AuthModel;
import com.djr.spelling.app.services.auth.util.HashingUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import javax.ejb.Schedule;
import javax.ejb.Schedules;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

/**
 * Created by IMac on 9/6/2014.
 */
@ApplicationScoped
public class AuthService {
	@Inject
	private AuthModelManager authModelManager;
	@Inject
	private Logger log;
	@Inject
	private HashingUtil hashingUtil;
	@Inject
	private Integer timeToLive;

	private boolean validateAuthToken(String trackingId, String authToken, AuthModel authModel) {
		log.debug("validateAuthToken() trackingId:{}, authToken:{}", trackingId, authToken);
		if (isProvidedAuthTokenNull(authToken)) {
			return false;
		}
		String authTokenCalc = "<timestamp>"+authModel.timestamp.toString()+"</timestamp><tracking>"+
				trackingId+"</tracking>";
		String hashed = hashingUtil.generateHmacHash(authTokenCalc);
		log.debug("validateAuthToken() trackingId:{}, generatedAuth:{}", trackingId, hashed);
		return authToken.trim().equalsIgnoreCase(hashed.trim());
	}

	public boolean validateTrackingId(String trackingId, String authToken, boolean validateTrackingIdOnly) {
		log.debug("validateTrackingId() trackingId:{}, authToken:{}, validateTrackingIdOnly:{}", trackingId, authToken,
				validateTrackingIdOnly);
		AuthModel authModel = authModelManager.getAuthModelByTrackingId(trackingId);
		return !isAuthModelNull(authModel) &&
				authModel.exipiry.isAfter(new DateTime().minusMinutes(timeToLive)) &&
				(validateTrackingIdOnly || validateAuthToken(trackingId, authToken, authModel));
	}

	public void addTrackingId(String trackingId, Integer userId) {
		log.debug("addTrackingId() trackingId:{}, userId:{}", trackingId, userId);
		authModelManager.addAuthModel(new AuthModel(trackingId, userId, timeToLive));
	}

	public String getAuthToken(String trackingId) {
		log.debug("getAuthToken() trackingId:{}", trackingId);
		String authTokenCalc = "<timestamp>"+authModelManager.getAuthModelByTrackingId(trackingId).timestamp.toString()+"</timestamp><tracking>"+
				trackingId+"</tracking>";
		return hashingUtil.generateHmacHash(authTokenCalc);
	}

	public Integer getUserId(String trackingId) {
		return authModelManager.getAuthModelByTrackingId(trackingId).userId;
	}

	public String getPasswordHash(String password) {
		log.debug("getPasswordHash()");
		String passwordMessage = "<password>"+password+"</password>";
		return hashingUtil.generateHmacHash(passwordMessage);
	}

	public boolean isProvidedAuthTokenNull(String authToken) {
		return authToken == null;
	}

	public boolean isAuthModelNull(AuthModel authModel) {
		return authModel == null;
	}
}
