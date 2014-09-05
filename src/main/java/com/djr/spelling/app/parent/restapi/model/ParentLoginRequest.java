package com.djr.spelling.app.parent.restapi.model;

import com.djr.spelling.User;
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
public class ParentLoginRequest implements Serializable {
	private static final long serialVersionUID = 1;
	@XmlElement
	@NotNull
	public String emailAddress;
	@XmlElement
	@NotNull
	public String password;

	public User getUserEntity() {
		return new User(emailAddress, password);
	}
}
