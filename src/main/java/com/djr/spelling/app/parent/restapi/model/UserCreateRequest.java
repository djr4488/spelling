package com.djr.spelling.app.parent.restapi.model;

import com.djr.spelling.*;
import javax.inject.Inject;
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
public class UserCreateRequest implements Serializable {
	private static final long serialVersionUID = 1;
	@XmlElement
	public String username;
	@XmlElement
	public String password;
	@XmlElement
	public String emailAddress;
	@XmlElement(name = "state")
	public String stateName;
	@XmlElement(name = "city")
	public String cityName;
	@XmlElement
	public String schoolName;

	@Inject
	private SpellingService spellingService;
	public User getUserEntity() {
		School school = spellingService.createOrFindSchool(schoolName);
		State state = spellingService.createOrFindState(stateName);
		City city = spellingService.createOrFindCity(cityName);
		Location location = spellingService.createOrFindLocation(school, state, city);
		return new User(username, password, emailAddress, location);
	}
}
