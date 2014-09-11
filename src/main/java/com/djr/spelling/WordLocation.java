package com.djr.spelling;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IMac on 9/2/2014.
 */
@Entity
@Table(name = "word_locations")
@NamedQueries({
	@NamedQuery(name="findWordLocation",
		query="select wLoc from WordLocation wLoc where wLoc.location = :location and wLoc.grade = :grade and " +
				"wLoc.word = :word"),
	@NamedQuery(name="findWordsByWeekGradeAndLocation",
		query="select wLoc from WordLocation wLoc where wLoc.location = :location and wLoc.grade = :grade and " +
				"wLoc.week.weekStart >= :weekStart and wLoc.week.weekEnd <= :weekEnd"),
	@NamedQuery(name="findWordsByGradeAndLocation",
		query="select wLoc from WordLocation wLoc where wLoc.location = :location and wLoc.grade = :grade"),
	@NamedQuery(name="findWordsByGrade",
		query="select wLoc from WordLocation wLoc where wLoc.grade = :grade"),
	@NamedQuery(name="findWordsByLocation",
		query="select wLoc from WordLocation wLoc where wLoc.location = :location")
})
public class WordLocation extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1;
	@ManyToOne
	@JoinColumn(name = "word_id")
	public Word word;
	@ManyToOne
	@JoinColumn(name = "location_id")
	public Location location;
	@ManyToOne
	@JoinColumn(name = "grade_id")
	public Grade grade;
	@ManyToOne
	@JoinColumn(name = "week_id")
	public Week week;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
