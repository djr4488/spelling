package com.djr.spelling.app.services.auth;

import com.djr.spelling.app.services.auth.model.AuthModel;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import javax.ejb.Schedule;
import javax.ejb.Schedules;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.*;

/**
 * Manages all the AuthModels currently connected
 */
@Singleton
public class AuthModelManager {
	private final Map<DateTime, HashSet<AuthModel>> timeAuthMap = new TreeMap<>();
	private final Map<String, AuthModel> trackingAuthMap = new HashMap<>();
	@Inject
	private Logger log;
	@Inject
	private Integer timeToLive;

	public AuthModel getAuthModelByTrackingId(String trackingId) {
		log.debug("getAuthModelByTrackingId() trackingId:{}", trackingId);
		return trackingAuthMap.get(trackingId);
	}

	public void addAuthModel(AuthModel authModel) {
		log.debug("addAuthModel() authModel:{}", authModel);
		trackingAuthMap.put(authModel.trackingId, authModel);
		if (!timeAuthMap.containsKey(authModel.exipiry)) {
			timeAuthMap.put(authModel.exipiry, new HashSet<AuthModel>());
		}
		timeAuthMap.get(authModel.exipiry).add(authModel);
	}

	public void removeAuthModel(AuthModel authModel) {
		log.debug("removeAuthModel() authModel:{}", authModel);
		trackingAuthMap.remove(authModel.trackingId);
		timeAuthMap.get(authModel.exipiry).remove(authModel);
		if (timeAuthMap.get(authModel.exipiry).size() == 0) {
			timeAuthMap.remove(authModel.exipiry);
		}
	}

	@Schedules({
			@Schedule(second = "0", minute = "*/5", hour="*")
	})
	public void removeExpired() {
		log.debug("removeExpired() checking...");
		if (timeAuthMap.keySet().iterator().hasNext() &&
				timeAuthMap.keySet().iterator().next().isBeforeNow()) {
			log.debug("removeExpired() cleaning up authTokens");
			Iterator<DateTime> timeAuthMapKeys = timeAuthMap.keySet().iterator();
			DateTime maxTimeInMap =
					((TreeMap<DateTime, HashSet<AuthModel>>) timeAuthMap).descendingKeySet().first();
			DateTime now = DateTime.now().minusMinutes(timeToLive);
			DateTime startedAt = DateTime.now();
			if (maxTimeInMap.isBefore(now)) {
				//remove everything
				timeAuthMap.clear();
				trackingAuthMap.clear();
				return;
			}
			while (timeAuthMapKeys.hasNext() && keepCleaning(startedAt)) {
				DateTime key = timeAuthMapKeys.next();
				for (AuthModel authModel : timeAuthMap.get(key)) {
					if (authModel.exipiry.isBefore(now)){
						trackingAuthMap.remove(authModel.trackingId);
					}
				}
				timeAuthMapKeys.remove();
			}
			log.debug("removeExpired() completed");
		}
	}

	public boolean keepCleaning(DateTime startedAt) {
		DateTime minus5Seconds = DateTime.now().minusSeconds(5);
		log.debug("keepCleaning() startedAt:{}, now plus 5 seconds:{}", startedAt.toString(), minus5Seconds.toString());
		return minus5Seconds.isBefore(startedAt);
	}
}
