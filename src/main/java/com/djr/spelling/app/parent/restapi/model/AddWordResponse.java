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
public class AddWordResponse extends BaseResponse implements Serializable {
	private static final long serialVersionUID = 1;

	@XmlElement
	public Integer childId;

	public AddWordResponse() {
		super();
	}

	public AddWordResponse(String errorMsg, String errorBold) {
		super(errorMsg, errorBold);
	}

	public AddWordResponse(String forwardTo) {
		super(forwardTo);
	}
}
