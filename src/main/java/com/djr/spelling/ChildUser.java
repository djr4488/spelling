package com.djr.spelling;

import com.djr.spelling.app.services.auth.AuthService;
import javax.persistence.*;
import java.util.Date;

/**
 * Created by danny.rucker on 9/2/14.
 */
@Entity
@Table(name = "child_users", uniqueConstraints = {@UniqueConstraint(columnNames = { "username" } ) })
@NamedQueries({
	@NamedQuery(name="findChildUser",
		query="select user from ChildUser user where user.username = :username and user.password = :password"),
	@NamedQuery(name = "findExistingChildUserByUsername",
		query="select user from ChildUser user where user.username = :username"),
	@NamedQuery(name = "findChildrenUsersByParentUser",
		query="select childrenUsers from ChildUser childrenUsers where childrenUsers.parent = :parent"),
	@NamedQuery(name = "findChildByParentAndChild",
		query="select child from ChildUser child where child.parent = :parent and child.username = :username")
})
public class ChildUser extends Identifiable {
	@Column(name = "username", unique = true, nullable = false, updatable = false)
	public String username;
	@Column(name = "password")
	public String password;
	@ManyToOne
	@JoinColumn(name = "parent_id")
	public User parent;
	@ManyToOne
	@JoinColumn(name = "location_id")
	public Location location;
	@ManyToOne
	@JoinColumn(name = "grade_id")
	public Grade grade;
	@Column(name="last_login")
	@Temporal(TemporalType.TIMESTAMP)
	public Date lastLogin;

	public ChildUser() {

	}

	public ChildUser(String username, String password, Location location, Grade grade, User user, AuthService authService) {
		this.username = username;
		this.password = authService.getPasswordHash(password);
		this.parent = user;
		this.location = location;
		this.grade = grade;
	}

	public ChildUser(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public ChildUser(String password) {
		this.password = password;
	}

	public void updateChildUser(ChildUser updated) {
		this.password = updated.password;
		this.location = updated.location;
		this.grade = updated.grade;
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("User[username=").append(username).append(", password=****").append(", email_address=");
		stringBuffer.append(parent.toString()).append(", location=").append(location.toString()).append("grade=")
			.append(grade.toString()).append("]");
		return stringBuffer.toString();
	}
}
