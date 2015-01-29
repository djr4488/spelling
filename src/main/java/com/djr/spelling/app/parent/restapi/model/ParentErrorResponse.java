package com.djr.spelling.app.parent.restapi.model;

import com.djr.spelling.app.BaseResponse;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by IMac on 1/28/2015.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ParentErrorResponse extends BaseResponse implements Serializable {
	private static final long serialVersionUID = 1;

	public ParentErrorResponse() {
		super();
	}

	public ParentErrorResponse(String errorMsg, String errorBold) {
		super(errorMsg, errorBold);
	}

	public ParentErrorResponse(String forwardTo) {
		super(forwardTo);
	}
}
