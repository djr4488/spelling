package com.djr.spelling;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IMac on 9/1/2014.
 */
@Entity
@Table(name = "sentences")
@NamedQueries({
	@NamedQuery(name="findSentence",
		query="select sentence from Sentence sentence where sentence.sentence = :sentence and sentence.word = :word")
})
public class Sentence extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1;
	@ManyToOne
	@JoinColumn(name = "word_id")
	public Word word;
	@Column(name = "sentence")
	public String sentence;

	public Sentence() {

	}

	public Sentence(String sentence) {
		this.sentence = sentence;
	}

	public Sentence(String sentence, Word word) {
		this.sentence = sentence;
		this.word = word;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
