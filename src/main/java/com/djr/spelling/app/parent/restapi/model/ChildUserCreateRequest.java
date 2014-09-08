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
	public boolean isPrivate;
	@XmlElement
	public boolean isHome;
	@XmlElement
	public String gradeName;

	public ChildUser getChildUserEntity(SpellingService spellingService, User user) {
		School school = spellingService.createOrFindSchool(getSchoolEntity(schoolName, isPrivate, isHome));
		State state = spellingService.createOrFindState(getStateEntity(stateName));
		City city = spellingService.createOrFindCity(getCityEntity(cityName));
		Grade grade = spellingService.createOrFindGrade(getGradeEntity(gradeName));
		Location location = spellingService.createOrFindLocation(getLocationEntity(state, city, school));
		return new ChildUser(username, password, location, grade, user);
	}

	private Grade getGradeEntity(String gradeName) {
		return new Grade(gradeName);
	}

	private City getCityEntity(String cityName) {
		return new City(cityName);
	}

	private State getStateEntity(String stateAbbreviation) {
		return new State(stateAbbreviation);
	}

	private School getSchoolEntity(String schoolName, boolean isPrivate, boolean isHome) {
		return new School(schoolName, isPrivate, isHome);
	}

	private Location getLocationEntity(State state, City city, School school) {
		return new Location(state, city, school);
	}
}
