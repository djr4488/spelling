package com.djr.spelling.app.parent.restapi.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by danny.rucker on 9/2/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class UserCreateResponse {
	@XmlElement
	private String errorMsg;
	@XmlElement
	private String errorBold;
	@XmlElement
	private String forwardTo;

	public UserCreateResponse() {}

	public UserCreateResponse(String errorMsg, String errorBold) {
		this.errorBold = errorBold;
		this.errorMsg = errorMsg;
	}

	public UserCreateResponse(String forwardTo) {
		this.forwardTo = forwardTo;
	}
}
