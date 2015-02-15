package com.djr.spelling.app.parent.restapi.model;

import com.djr.spelling.app.BaseResponse;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by danny.rucker on 9/8/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class EditWordResponse extends BaseResponse implements Serializable {
	private static final long serialVersionUID = 1;

	@XmlElement
	public Integer wordId;

	public EditWordResponse() {
		super();
	}

	public EditWordResponse(String errorMsg, String errorBold) {
		super(errorMsg, errorBold);
	}

	public EditWordResponse(String forwardTo) {
		super(forwardTo);
	}

	public EditWordResponse(String forwardTo, String successMsg, String successBold) {
		super(forwardTo, successMsg, successBold);
	}
}
