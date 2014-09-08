package com.djr.spelling;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by IMac on 9/1/2014.
 */
@Entity
@Table(name = "grades")
@NamedQueries({
	@NamedQuery(name="findGrade",
		query="select grade from Grade grade where grade.grade = :grade")
})
public class Grade extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1;
	@Column(name = "grade")
	public String grade;

	public Grade() {}

	public Grade(String grade) {
		this.grade = grade;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
