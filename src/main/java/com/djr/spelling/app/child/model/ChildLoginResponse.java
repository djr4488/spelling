package com.djr.spelling.app.child.model;

import com.djr.spelling.app.BaseResponse;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by IMac on 9/9/2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ChildLoginResponse extends BaseResponse implements Serializable {
	private static final long serialVersionUID = 1;

	public ChildLoginResponse() {
		super();
	}

	public ChildLoginResponse(String errorMsg, String errorBold) {
		super(errorMsg, errorBold);
	}

	public ChildLoginResponse(String forwardTo) {
		super(forwardTo);
	}
}
