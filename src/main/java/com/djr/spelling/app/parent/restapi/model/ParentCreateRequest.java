package com.djr.spelling.app.parent.restapi.model;

import com.djr.spelling.*;
import com.djr.spelling.app.BaseRequest;
import com.djr.spelling.app.services.auth.AuthService;
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
public class ParentCreateRequest extends BaseRequest implements Serializable {
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

	public User getUserEntity(AuthService authService) {
		return new User(username, password, emailAddress, authService);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("parentCreateRequest[username=").append(username).append(", password=****");
		sb.append(", confirmPassword=****, emailAddress=").append(emailAddress).append("]");
		return sb.toString();
	}
}
