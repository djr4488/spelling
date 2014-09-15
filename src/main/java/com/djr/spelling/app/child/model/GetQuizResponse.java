package com.djr.spelling.app.child.model;

import com.djr.spelling.app.BaseResponse;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by IMac on 9/13/2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class GetQuizResponse extends BaseResponse implements Serializable {
	private static final long serialVersionUID = 1;

	@XmlElement
	public QuizWordWrapper quizWordWrapper;

	public GetQuizResponse() {
		super();
	}

	public GetQuizResponse(String errorMsg, String errorBold) {
		super(errorMsg, errorBold);
	}

	public GetQuizResponse(String forwardTo) {
		super(forwardTo);
	}
}
