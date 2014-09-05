package com.djr.spelling;

import javax.persistence.*;

/**
 * Created by danny.rucker on 9/2/14.
 */
@Entity
@Table(name = "child_users", uniqueConstraints = {@UniqueConstraint(columnNames = { "username" } ) })
@NamedQueries({
	@NamedQuery(name="findExistingUserByUserNameAndPassword",
		query="select user from ChildUser user where user.username = :username and user.password = :password"),
	@NamedQuery(name = "findExistingChildUserByUsername",
		query="select user from ChildUser user where user.username = :username"),
	@NamedQuery(name = "findChildrenUsersByParentUser",
		query="select childrenUsers from ChildUser childrenUsers where childrenUsers.parent = :parent")
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

	public ChildUser() {

	}

	public ChildUser(String username, String password, Location location, Grade grade, User user) {
		this.username = username;
		this.password = password;
		this.parent = user;
		this.location = location;
		this.grade = grade;
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
