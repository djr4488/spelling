package com.djr.spelling.app.parent.restapi.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by IMac on 9/3/2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ChildUserCreateResponse implements Serializable {
	private static final long serialVersionUID = 1;
	@XmlElement
	private String errorMsg;
	@XmlElement
	private String errorBold;
	@XmlElement
	private String forwardTo;

	public ChildUserCreateResponse() {}

	public ChildUserCreateResponse(String errorMsg, String errorBold) {
		this.errorBold = errorBold;
		this.errorMsg = errorMsg;
	}

	public ChildUserCreateResponse(String forwardTo) {
		this.forwardTo = forwardTo;
	}
}
