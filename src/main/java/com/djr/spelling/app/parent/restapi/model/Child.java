package com.djr.spelling.app.parent.restapi.model;

import com.djr.spelling.ChildUser;
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
	@XmlElement(name="stateAbbr")
	public String state;
	@XmlElement(name="cityName")
	public String city;
	@XmlElement(name="schoolName")
	public String school;
	@XmlElement(name="grade")
	public String grade;

	public Child() {}

	public Child(String username, Integer id) {
		this.username= username;
		this.id = id;
	}

	public Child(ChildUser childUser) {
		this.username = childUser.username;
		this.id = childUser.id;
		this.state = childUser.location.state.stateAbbr;
		this.city = childUser.location.city.cityName;
		this.school = childUser.location.school.schoolName;
		this.grade = childUser.grade.grade;
	}
}
