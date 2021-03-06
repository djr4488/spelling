package com.djr.spelling.app;

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
public class ErrorResponse extends BaseResponse implements Serializable {
	private static final long serialVersionUID = 1;

	public ErrorResponse() {
		super();
	}

	public ErrorResponse(String errorMsg, String errorBold) {
		super(errorMsg, errorBold);
	}

	public ErrorResponse(String forwardTo) {
		super(forwardTo);
	}
}
