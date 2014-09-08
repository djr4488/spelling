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
@Table(name = "cities")
@NamedQueries({
	@NamedQuery(name="findCity",
		query="select city from City city where city.cityName = :cityName")
})
public class City extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1;
	@Column(name = "city_name")
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
