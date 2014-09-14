package com.djr.spelling;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by IMac on 9/10/2014.
 */
@Entity
@Table(name = "quiz_words")
public class QuizWord extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1;

	@ManyToOne
	@JoinColumn(name="quiz_id")
	public Quiz quiz;
	@ManyToOne
	@JoinColumn(name="word_location_id")
	public WordLocation wordLocation;

	public QuizWord() {}

	public QuizWord(Quiz quiz, WordLocation wordLocation) {
		this.quiz = quiz;
		this.wordLocation = wordLocation;
	}
}
