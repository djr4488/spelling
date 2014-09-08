package com.djr.spelling;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IMac on 9/1/2014.
 */
@Entity
@Table(name = "schools")
@NamedQueries({
	@NamedQuery(name="findSchool",
		query="select school from School school where school.schoolName = :school and school.isPrivate = :isPrivate and" +
			" school.isHome = :isHome")
})
public class School extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1;

	@Column(name = "school_name")
	public String schoolName;
	@Column(name = "is_private")
	public boolean isPrivate;
	@Column(name = "is_home")
	public boolean isHome;

	public School() {

	}

	public School(String schoolName, boolean isPrivate, boolean isHome) {
		this.schoolName = schoolName;
		this.isPrivate = isPrivate;
		this.isHome = isHome;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
