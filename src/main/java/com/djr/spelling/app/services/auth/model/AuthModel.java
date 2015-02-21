package com.djr.spelling.app.services.auth.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

/**
 * Created by IMac on 9/6/2014.
 */
public class AuthModel {
	public String trackingId;
	public DateTime exipiry;
	public DateTime timestamp;
	public Integer userId;

	public AuthModel() {}

	public AuthModel(String trackingId, Integer userId, Integer timeToLive) {
		this.trackingId = trackingId;
		this.exipiry = new DateTime().plusMinutes(timeToLive);
		this.timestamp = new DateTime();
		this.userId = userId;
	}

	@Override
	public final int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public final boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
