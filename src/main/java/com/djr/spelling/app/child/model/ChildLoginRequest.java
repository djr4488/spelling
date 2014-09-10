package com.djr.spelling.app.child.model;

import com.djr.spelling.ChildUser;
import com.djr.spelling.app.BaseRequest;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by IMac on 9/9/2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ChildLoginRequest extends BaseRequest implements Serializable {
	private static final long serialVersionUID = 1;

	@XmlElement
	@NotNull
	public String username;
	@XmlElement
	@NotNull
	public String password;

	public ChildUser getChildUserEntity() {
		return new ChildUser(username, password);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("childLoginRequest[username=").append(username).append(", password=****").append("]");
		return sb.toString();
	}
}
