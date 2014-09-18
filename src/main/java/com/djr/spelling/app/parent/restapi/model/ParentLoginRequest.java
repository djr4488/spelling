package com.djr.spelling.app.parent.restapi.model;

import com.djr.spelling.User;
import com.djr.spelling.app.BaseRequest;
import com.djr.spelling.app.services.auth.AuthService;
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
public class ParentLoginRequest extends BaseRequest implements Serializable {
	private static final long serialVersionUID = 1;
	@XmlElement
	@NotNull
	public String emailAddress;
	@XmlElement
	@NotNull
	public String password;

	public User getUserEntity(AuthService authService) {
		return new User(emailAddress, authService.getPasswordHash(password));
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("parentLoginRequest[emailAddress=").append(emailAddress).append(", password=****").append("]");
		return sb.toString();
	}
}
