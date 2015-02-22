package com.djr.spelling.app.parent.restapi.model;

import com.djr.spelling.*;
import com.djr.spelling.app.BaseRequest;
import com.djr.spelling.app.exceptions.SpellingException;
import com.djr.spelling.app.services.auth.AuthService;
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
public class EditChildRequest extends BaseRequest implements Serializable {
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
	@XmlElement
	@NotNull
	public String stateAbbr;
	@XmlElement
	@NotNull
	public String cityName;
	@XmlElement
	@NotNull
	public String schoolName;
	@XmlElement
	@NotNull
	public String grade;

	public ChildUser getUserEntity() {
		return new ChildUser(username, password);
	}

	public ChildUser getChildUser(SpellingService spellingService, AuthService authService, User parent)
	throws SpellingException {
		State state = spellingService.createOrFindState(new State(null, stateAbbr));
		City city = spellingService.createOrFindCity(new City(cityName));
		School school = spellingService.createOrFindSchool(new School(schoolName, false, false));
		Grade gradeEntity = spellingService.createOrFindGrade(new Grade(grade));
		Location location = spellingService.createOrFindLocation(new Location(state, city, school));
		ChildUser childUser = new ChildUser(username, password, location, gradeEntity, parent, authService);
		return childUser;
	}

	@Override
	public String toString() {
		return "editChildRequest[originalPassword=****, password=****, confirmPassword=****]";
	}
}
