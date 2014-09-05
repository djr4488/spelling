package com.djr.spelling.app.parent.restapi.model;

import com.djr.spelling.app.BaseResponse;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by danny.rucker on 9/4/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class EditParentResponse extends BaseResponse implements Serializable {
	private static final long serialVersionUID = 1;

	public EditParentResponse() {
		super();
	}

	public EditParentResponse(String errorMsg, String errorBold) {
		super(errorMsg, errorBold);
	}

	public EditParentResponse(String forwardTo) {
		super(forwardTo);
	}
}
