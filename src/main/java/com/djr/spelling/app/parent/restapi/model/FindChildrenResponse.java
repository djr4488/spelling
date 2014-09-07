package com.djr.spelling.app.parent.restapi.model;

import com.djr.spelling.ChildUser;
import com.djr.spelling.app.BaseResponse;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IMac on 9/7/2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class FindChildrenResponse extends BaseResponse implements Serializable {
	private static final long serialVersionUID = 1;
	@XmlElement
	public List<Child> parentChildren;

	public void setParentChildren(List<ChildUser> childUsers) {
		parentChildren = new ArrayList<>();
		for (ChildUser childUser : childUsers) {
			parentChildren.add(new Child(childUser.username, childUser.id));
		}
	}

	public FindChildrenResponse() {
		super();
	}

	public FindChildrenResponse(String errorMsg, String errorBold) {
		super(errorMsg, errorBold);
	}

	public FindChildrenResponse(String forwardTo) {
		super(forwardTo);
	}
}
