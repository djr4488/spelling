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
public class School extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1;

	@Column(name = "school_name")
	public String schoolName;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
