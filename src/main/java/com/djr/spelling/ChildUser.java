package com.djr.spelling;

import javax.persistence.*;

/**
 * Created by danny.rucker on 9/2/14.
 */
@Entity
@Table(name = "child_users")
@NamedQueries({
	@NamedQuery(name="findExistingUserByUserNameAndPassword",
		query="select user from ChildUser user where user.userName = :username and user.password = :password")
})
public class ChildUser extends Identifiable {
	@Column(name = "username")
	public String userName;
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
}
