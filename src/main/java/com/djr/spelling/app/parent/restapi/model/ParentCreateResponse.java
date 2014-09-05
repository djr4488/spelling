package com.djr.spelling.app.parent.restapi.model;

import com.djr.spelling.app.BaseResponse;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by danny.rucker on 9/2/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ParentCreateResponse extends BaseResponse implements Serializable {
	private static final long serialVersionUID = 1;

	public ParentCreateResponse() {
		super();
	}

	public ParentCreateResponse(String errorMsg, String errorBold) {
		super(errorMsg, errorBold);
	}

	public ParentCreateResponse(String forwardTo) {
		super(forwardTo);
	}
}
