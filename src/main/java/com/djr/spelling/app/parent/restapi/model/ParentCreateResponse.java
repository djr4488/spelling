package com.djr.spelling.app.parent.restapi.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by danny.rucker on 9/2/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ParentCreateResponse implements Serializable {
	private static final long serialVersionUID = 1;
	@XmlElement
	private String errorMsg;
	@XmlElement
	private String errorBold;
	@XmlElement
	private String forwardTo;

	public ParentCreateResponse() {}

	public ParentCreateResponse(String errorMsg, String errorBold) {
		this.errorBold = errorBold;
		this.errorMsg = errorMsg;
	}

	public ParentCreateResponse(String forwardTo) {
		this.forwardTo = forwardTo;
	}
}
