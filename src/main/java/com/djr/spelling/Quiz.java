package com.djr.spelling;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IMac on 9/10/2014.
 */
@Entity
@Table(name = "quizzes")
@NamedQueries({
	@NamedQuery(name="findQuizzesByChild",
		query="select quiz from Quiz quiz where quiz.child = :child")
})
public class Quiz extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1;

	@ManyToOne
	@JoinColumn(name="child_id")
	public ChildUser child;
	@Column(name="start_date")
	@Temporal(TemporalType.DATE)
	public Date startDate;
	@Column(name="finished_date")
	@Temporal(TemporalType.DATE)
	public Date finishedDate;
	@Column(name="number_correct")
	public Integer numberCorrect;
	@Column(name="number_of_words")
	public Integer numberOfWords;

	public Quiz() {}

	public Quiz(ChildUser child, Date startDate, Date finishedDate) {
		this.child = child;
		this.startDate = startDate;
		this.finishedDate = finishedDate;
	}
}
