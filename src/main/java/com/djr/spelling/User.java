package com.djr.spelling;

import javax.persistence.*;

/**
 * Created by IMac on 9/1/2014.
 */
@Entity
@Table(name = "users")
@NamedQueries({
	@NamedQuery(name="findExistingUserByEmailAddress",
		query="select user from User user where user.emailAddress = :emailAddress"),
	@NamedQuery(name="findExistingUserByUserNameAndPassword",
		query="select user from User user where user.userName = :username and user.password = :password")
})
public class User extends Identifiable {
	@Column(name = "parent_user")
	public Boolean isParent;
	@Column(name = "username")
	public String userName;
	@Column(name = "password")
	public String password;
	@Column(name = "email_address", unique=true, nullable=false, updatable=true)
	public String emailAddress;
	@ManyToOne
	@JoinColumn(name = "parent_id")
	public User parent;
	@ManyToOne
	@JoinColumn(name = "location_id")
	public Location location;
}
