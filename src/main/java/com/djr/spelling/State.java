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
@Table(name = "states")
@NamedQueries({
	@NamedQuery(name="findState",
		query="select state from State state where state.stateAbbr = :stateAbbr")
})
public class State extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1;
	@Column(name = "state_name")
	public String stateName;
	@Column(name = "state_abbr")
	public String stateAbbr;

	public State() {

	}

	public State(String stateAbbr) {
		this.stateAbbr = stateAbbr;
	}

	public State(String stateName, String stateAbbr) {
		this.stateName = stateName;
		this.stateAbbr = stateAbbr;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
