package com.djr.spelling.app;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by IMac on 9/5/2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class BaseResponse implements Serializable {
	private static final long serialVersionUID = 1;
	@XmlElement
	public String errorMsg;
	@XmlElement
	public String errorBold;
	@XmlElement
	public String successMsg;
	@XmlElement
	public String successBold;
	@XmlElement
	public String forwardTo;
	@XmlElement
	public String authToken;
	@XmlElement
	public Integer id;

	public BaseResponse() {}

	public BaseResponse(String errorMsg, String errorBold) {
		this.errorBold = errorBold;
		this.errorMsg = errorMsg;
	}

	public BaseResponse(String forwardTo) {
		this.forwardTo = forwardTo;
	}

	public BaseResponse(String forwardTo, String successMsg, String successBold) {
		this(forwardTo);
		this.successMsg = successMsg;
		this.successBold = successBold;
	}
}
