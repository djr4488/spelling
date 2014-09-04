package com.djr.spelling.app.parent.restapi.model;

import com.djr.spelling.*;
import javax.inject.Inject;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by IMac on 9/3/2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ChildUserCreateRequest implements Serializable {
	private static final long serialVersionUID = 1;
	@XmlElement
	public String username;
	@XmlElement
	public String password;
	@XmlElement(name = "state")
	public String stateName;
	@XmlElement(name = "city")
	public String cityName;
	@XmlElement
	public String schoolName;
	@XmlElement
	public String gradeName;

	@Inject
	private SpellingService spellingService;
	public ChildUser getChildUserEntity(User user) {
		School school = spellingService.createOrFindSchool(schoolName);
		State state = spellingService.createOrFindState(stateName);
		City city = spellingService.createOrFindCity(cityName);
		Grade grade = spellingService.createOrFindGrade(gradeName);
		Location location = spellingService.createOrFindLocation(school, state, city);
		return new ChildUser(username, password, location, grade, user);
	}
}
