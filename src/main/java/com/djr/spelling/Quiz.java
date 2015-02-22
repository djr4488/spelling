package com.djr.spelling;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IMac on 9/14/2014.
 */
@Entity
@Table(name="quizzes")
public class Quiz extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1;

	@Column(name = "created_at")
	@Temporal(TemporalType.DATE)
	public Date createdAt;
	@Column(name = "quiz_type")
	public String quizType;
	@JoinColumn(name = "word_location_id")
	@ManyToOne
	public WordLocation wordLocation;

	public Quiz() {
		createdAt = new Date();
	}

	public Quiz(String quizType, WordLocation wordLocation) {
		this.quizType = quizType;
		this.wordLocation = wordLocation;
	}
}
