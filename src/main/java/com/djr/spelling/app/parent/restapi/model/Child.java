package com.djr.spelling.app.parent.restapi.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IMac on 9/7/2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Child {
	@XmlElement
	public String username;
	@XmlElement
	public Integer id;

	public Child() {}

	public Child(String username, Integer id) {
		this.username= username;
		this.id = id;
	}
}
