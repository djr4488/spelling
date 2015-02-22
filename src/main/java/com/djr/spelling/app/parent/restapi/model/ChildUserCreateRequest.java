package com.djr.spelling.app.parent.restapi.model;

import com.djr.spelling.*;
import com.djr.spelling.app.BaseRequest;
import com.djr.spelling.app.exceptions.SpellingException;
import com.djr.spelling.app.services.auth.AuthService;
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
public class ChildUserCreateRequest extends BaseRequest implements Serializable {
	private static final long serialVersionUID = 1;
	@XmlElement
	public String username;
	@XmlElement
	public String password;
	@XmlElement
	public String confirmPassword;
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

	public ChildUser getChildUserEntity(SpellingService spellingService, User user, AuthService authService)
	throws SpellingException {
		School school = spellingService.createOrFindSchool(getSchoolEntity(schoolName, isPrivate, isHome));
		State state = spellingService.createOrFindState(getStateEntity(stateName));
		City city = spellingService.createOrFindCity(getCityEntity(cityName));
		Grade grade = spellingService.createOrFindGrade(getGradeEntity(gradeName));
		Location location = spellingService.createOrFindLocation(getLocationEntity(state, city, school));
		return new ChildUser(username, password, location, grade, user, authService);
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

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("childUserCreateRequest[");
		sb.append("username=").append(username).append(", ");
		sb.append("password=").append("****").append(", ");
		sb.append("confirmPassword=").append("****").append(", ");
		sb.append("stateName=").append(stateName).append(", ");
		sb.append("cityName=").append(cityName).append(", ");
		sb.append("schoolName=").append(schoolName).append(", ");
		sb.append("isPrivate=").append(isPrivate).append(", ");
		sb.append("isHome=").append(isHome).append(", ");
		sb.append("gradeName=").append(gradeName).append(", ");
		sb.append("]");
		return sb.toString();
	}
}
