package com.djr.spelling;

/**
 * Created by IMac on 9/1/2014.
 */

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "words", uniqueConstraints = {@UniqueConstraint(columnNames = { "word" } ) })
@NamedQueries({
	@NamedQuery(name="findWord",
		query = "select w from Word w where w.word = :word and w.metaphone = :metaphone")
})
public class Word extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1;
	@Column(name = "word", unique=true, nullable=false, updatable=true)
	public String word;
	@Column(name = "metaphone")
	public String metaphone;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
