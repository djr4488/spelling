package com.djr.spelling.app.child.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by IMac on 9/13/2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class QuizWordWrapper implements Serializable {
	private static final long serialVersionUID = 1;

	@XmlElement
	public List<QuizWordModel> quizWords;
	@XmlElement
	public Integer quizId;

	public QuizWordWrapper() {}

	public QuizWordWrapper(List<QuizWordModel> quizWords, Integer quizId) {
		this.quizWords = quizWords;
		this.quizId = quizId;
	}
}
