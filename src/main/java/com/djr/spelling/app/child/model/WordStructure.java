package com.djr.spelling.app.child.model;

import com.djr.spelling.Sentence;
import com.djr.spelling.Word;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.io.Serializable;

/**
 * Created by IMac on 9/1/2014.
 */
public class WordStructure implements Serializable {
	private static final long serialVersionUID = 1;
	public Word word;
	public Sentence sentence;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
