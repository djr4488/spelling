package com.djr.spelling.app.parent.restapi.model;

import com.djr.spelling.User;
import com.djr.spelling.app.BaseRequest;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by danny.rucker on 9/4/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class EditParentRequest extends BaseRequest implements Serializable {
	private static final long serialVersionUID = 1;
	@XmlElement
	@NotNull
	public String originalPassword;
	@XmlElement
	@NotNull
	public String password;
	@XmlElement
	@NotNull
	public String confirmPassword;

	public User getUserEntity() {
		return new User(password);
	}

	@Override
	public String toString() {
		return "editParentRequest[originalPassword=****, password=****, confirmPassword=****]";
	}
}
