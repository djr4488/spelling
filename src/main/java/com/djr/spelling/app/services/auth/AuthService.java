package com.djr.spelling.app.services.auth;

import com.djr.spelling.app.services.auth.model.AuthModel;
import com.djr.spelling.app.services.auth.util.HashingUtil;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import javax.ejb.Schedule;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

/**
 * Created by IMac on 9/6/2014.
 */
@ApplicationScoped
public class AuthService {
	private static final Object lock = new Object();
	private static boolean isCleaning = false;
	private final Map<DateTime, HashSet<AuthModel>> timeAuthMap = new TreeMap<>();
	private final Map<String, AuthModel> trackingAuthMap = new HashMap<>();
	@Inject
	private Logger log;
	@Inject
	private HashingUtil hashingUtil;

	private boolean validateAuthToken(String trackingId, String authToken) {
		log.debug("validateAuthToken() trackingId:{}, authToken:{}", trackingId, authToken);
		String authTokenCalc = "<timestamp>"+trackingAuthMap.get(trackingId).timestamp.toString()+"</timestamp><tracking>"+
				trackingId+"</tracking>";
		String hashed = hashingUtil.generateHmacHash(authTokenCalc);
		return authToken.trim().equalsIgnoreCase(hashed.trim());
	}

	public boolean validateTrackingId(String trackingId, String authToken, boolean validateTrackingIdOnly) {
		log.debug("validateTrackingId() trackingId:{}, authToken:{}", trackingId, authToken);
		return trackingAuthMap.containsKey(trackingId) &&
				trackingAuthMap.get(trackingId).exipiry.isAfter(new DateTime().minusMinutes(120)) &&
				(validateTrackingIdOnly || validateAuthToken(trackingId, authToken));
	}

	public void addTrackingId(String trackingId, Integer userId) {
		log.debug("addTrackingId() trackingId:{}", trackingId);
		trackingAuthMap.put(trackingId, new AuthModel(trackingId, userId));
		synchronized(lock) {
			AuthModel authModel = trackingAuthMap.get(trackingId);
			if (!timeAuthMap.containsKey(authModel.exipiry)) {
				timeAuthMap.put(authModel.exipiry, new HashSet<AuthModel>());
			}
			timeAuthMap.get(authModel.exipiry).add(authModel);
		}
	}

	public String getAuthToken(String trackingId) {
		String authTokenCalc = "<timestamp>"+trackingAuthMap.get(trackingId).timestamp.toString()+"</timestamp><tracking>"+
				trackingId+"</tracking>";
		return hashingUtil.generateHmacHash(authTokenCalc);
	}

	public Integer getUserId(String trackingId) {
		return trackingAuthMap.get(trackingId).userId;
	}

	@Schedule(minute = "*/5")
	public void removeExpired() {
		if (timeAuthMap.keySet().iterator().hasNext() &&
				timeAuthMap.keySet().iterator().next().isBeforeNow() &&
				!isCleaning) {
			synchronized(lock) {
				Iterator<DateTime> timeAuthMapKeys = timeAuthMap.keySet().iterator();
				DateTime maxTimeInMap =
						((TreeMap<DateTime, HashSet<AuthModel>>) timeAuthMap).descendingKeySet().first();
				DateTime now = new DateTime().minusMinutes(120);
				if (maxTimeInMap.isBefore(now)) {
					//remove everything
					timeAuthMap.clear();
					trackingAuthMap.clear();
					return;
				}
				while (timeAuthMapKeys.hasNext()) {
					DateTime key = timeAuthMapKeys.next();
					for (AuthModel authModel : timeAuthMap.get(key)) {
						if (authModel.exipiry.isBefore(now)){
							trackingAuthMap.remove(authModel.trackingId);
						}
					}
					timeAuthMapKeys.remove();
				}
			}
		}
	}
}
