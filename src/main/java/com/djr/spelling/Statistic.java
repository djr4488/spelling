package com.djr.spelling;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IMac on 9/1/2014.
 */
@Entity
@Table(name = "word_stats")
public class Statistic extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1;
	@ManyToOne
	@JoinColumn(name = "user_id")
	public User user;
	@Column(name = "missed")
	public Integer missed;
	@Column(name = "attempts")
	public Integer attempts;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
