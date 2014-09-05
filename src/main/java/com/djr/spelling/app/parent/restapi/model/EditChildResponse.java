package com.djr.spelling.app.parent.restapi.model;

import com.djr.spelling.app.BaseResponse;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by IMac on 9/5/2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class EditChildResponse extends BaseResponse implements Serializable {
	private static final long serialVersionUID = 1;

	public EditChildResponse() {
		super();
	}

	public EditChildResponse(String errorMsg, String errorBold) {
		super(errorMsg, errorBold);
	}

	public EditChildResponse(String forwardTo) {
		super(forwardTo);
	}
}
