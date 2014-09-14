package com.djr.spelling;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IMac on 9/14/2014.
 */
public class Quiz extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1;

	@Column(name = "created_at")
	@Temporal(TemporalType.DATE)
	public Date createdAt;

	public Quiz() {
		createdAt = new Date();
	}
}
