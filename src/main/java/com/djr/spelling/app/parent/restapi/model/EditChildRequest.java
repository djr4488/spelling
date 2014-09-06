package com.djr.spelling.app.parent.restapi.model;

import com.djr.spelling.ChildUser;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by IMac on 9/5/2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class EditChildRequest implements Serializable {
	private static final long serialVersionUID = 1;
	@XmlElement
	@NotNull
	public String username;
	@XmlElement
	@NotNull
	public String originalPassword;
	@XmlElement
	@NotNull
	public String password;
	@XmlElement
	@NotNull
	public String confirmPassword;

	public ChildUser getUserEntity() {
		return new ChildUser(username, password);
	}
}
