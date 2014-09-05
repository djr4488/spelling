package com.djr.spelling.app.parent.restapi.model;

import com.djr.spelling.app.BaseResponse;
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
public class ChildUserCreateResponse extends BaseResponse implements Serializable {
	private static final long serialVersionUID = 1;

	public ChildUserCreateResponse() {
		super();
	}

	public ChildUserCreateResponse(String errorMsg, String errorBold) {
		super(errorMsg, errorBold);
	}

	public ChildUserCreateResponse(String forwardTo) {
		super(forwardTo);
	}
}
