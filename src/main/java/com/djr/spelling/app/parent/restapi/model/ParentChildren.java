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
 * Created by IMac on 9/24/2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ParentChildren extends BaseResponse implements Serializable {
	private static final long serialVersionUID = 1;
	@XmlElement(name="parentChild")
	public List<Child> parentChildren;

	public ParentChildren() { }

	public ParentChildren(List<ChildUser> childUsers) {
		parentChildren = new ArrayList<>();
		for (ChildUser childUser : childUsers) {
			parentChildren.add(new Child(childUser));
		}
	}
}
