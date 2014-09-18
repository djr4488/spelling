package com.djr.spelling;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IMac on 9/10/2014.
 */
@Entity
@Table(name = "quiz_results")
@NamedQueries({
	@NamedQuery(name="findQuizzesByChild",
		query="select quiz from QuizResult quiz where quiz.child = :child")
})
public class QuizResult extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1;

	@ManyToOne
	@JoinColumn(name="child_id")
	public ChildUser child;
	@Column(name="start_date")
	@Temporal(TemporalType.DATE)
	public Date startDate;
	@Column(name="number_correct")
	public Integer numberCorrect;
	@Column(name="number_of_words")
	public Integer numberOfWords;

	public QuizResult() {}

	public QuizResult(ChildUser child, Date startDate) {
		this.child = child;
		this.startDate = startDate;
	}
}
