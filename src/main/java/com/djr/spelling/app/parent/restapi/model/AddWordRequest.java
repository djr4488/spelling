package com.djr.spelling.app.parent.restapi.model;


import com.djr.spelling.Word;
import com.djr.spelling.app.BaseRequest;
import org.apache.commons.codec.language.DoubleMetaphone;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by danny.rucker on 9/8/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class AddWordRequest extends BaseRequest implements Serializable {
	private static final long serialVersionUID = 1;

	@XmlElement
	@NotNull
	public String word;
	@XmlElement
	public String sentence;
	@XmlElement
	@NotNull
	public String startOfWeek;
	@XmlElement
	@NotNull
	public String endOfWeek;

	public Word getWordEntity(DoubleMetaphone dm) {
		Word word = new Word(this.word, dm.doubleMetaphone(this.word));
		return word;
	}
}
