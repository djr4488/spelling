package com.djr.spelling;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by IMac on 9/1/2014.
 */
@Entity
@Table(name = "locations")
public class Location extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1;
	@ManyToOne
	@JoinColumn(name = "state_id")
	public State state;
	@ManyToOne
	@JoinColumn(name = "city_id")
	public City city;
	@ManyToOne
	@JoinColumn(name = "school_id")
	public School school;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
