package com.djr.spelling;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by IMac on 9/1/2014.
 */
@Entity
@Table(name = "locations")
@NamedQueries({
	@NamedQuery(name="findLocation",
		query="select location from Location location where location.state = :state and location.city = :city and" +
			" location.school = :school")
})
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

	public Location() {}

	public Location(State state, City city, School school) {
		this.state = state;
		this.city = city;
		this.school = school;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
