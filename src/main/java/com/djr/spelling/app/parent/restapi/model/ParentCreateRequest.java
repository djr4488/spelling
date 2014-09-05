package com.djr.spelling.app.parent.restapi.model;

import com.djr.spelling.*;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by danny.rucker on 9/2/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ParentCreateRequest implements Serializable {
	private static final long serialVersionUID = 1;
	@XmlElement
	@NotNull
	public String username;
	@XmlElement
	@NotNull
	public String password;
	@XmlElement
	@NotNull
	public String confirmPassword;
	@XmlElement
	@NotNull
	public String emailAddress;

	public User getUserEntity() {
		return new User(username, password, emailAddress);
	}
}
