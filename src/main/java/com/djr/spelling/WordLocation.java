package com.djr.spelling;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by IMac on 9/2/2014.
 */
@Entity
@Table(name = "word_locations")
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

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
