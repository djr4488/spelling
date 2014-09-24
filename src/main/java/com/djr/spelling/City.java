package com.djr.spelling;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IMac on 9/1/2014.
 */
@Entity
@Table(name = "cities", uniqueConstraints = {@UniqueConstraint(columnNames = { "city_name" })})
@NamedQueries({
	@NamedQuery(name="findCity",
		query="select city from City city where city.cityName = :cityName")
})
public class City extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1;
	@Column(name = "city_name", unique=true, nullable = false)
	public String cityName;

	public City() {}

	public City(String cityName) {
		this.cityName = cityName;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
