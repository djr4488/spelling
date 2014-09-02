package com.djr.spelling;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IMac on 9/1/2014.
 */
@Entity
@Table(name = "weeks")
public class Week extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1;
	@Column(name = "week_start")
	public Date weekStart;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
