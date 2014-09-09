package com.djr.spelling;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IMac on 9/1/2014.
 */
@Entity
@Table(name = "weeks")
@NamedQueries({
	@NamedQuery(name="findWeek",
		query="select week from Week week where week.weekStart = :weekStart and week.weekEnd = :weekEnd")
})
public class Week extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1;
	@Column(name = "week_start")
	@Temporal(TemporalType.DATE)
	public Date weekStart;
	@Column(name = "week_end")
	@Temporal(TemporalType.DATE)
	public Date weekEnd;

	public Week() {}

	public Week(Date weekStart, Date weekEnd) {
		this.weekStart = weekStart;
		this.weekEnd = weekEnd;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
