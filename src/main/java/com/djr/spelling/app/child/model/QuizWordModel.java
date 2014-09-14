package com.djr.spelling.app.child.model;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * Created by IMac on 9/13/2014.
 */
public class QuizWordModel implements Serializable {
	private static final long serialVersionUID = 1;

	@XmlElement
	public Integer id;
	@XmlElement
	public String word;
	@XmlElement
	public String sentence;

	public QuizWordModel() {}

	public QuizWordModel(Integer id, String word, String sentence) {
		this.id = id;
		this.word = word;
		this.sentence = sentence;
	}
}
