package com.djr.spelling.app.parent.restapi.model;

import com.djr.spelling.Sentence;
import com.djr.spelling.Word;
import com.djr.spelling.app.BaseRequest;
import org.apache.commons.codec.language.DoubleMetaphone;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by danny.rucker on 9/8/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class EditWordRequest extends BaseRequest implements Serializable {
	private static final long serialVersionUID = 1;

	@XmlElement
	@NotNull
	public String word;

	@XmlElement
	@NotNull
	public String editedWord;

	@XmlElement
	public String sentence;

	public Word getWordEntity(DoubleMetaphone dm) {
		return new Word(word, dm.doubleMetaphone(word));
	}

	public Word getEditedWordEntity(DoubleMetaphone dm) {
		return new Word(editedWord, dm.doubleMetaphone(editedWord));
	}
}
