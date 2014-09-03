package com.djr.spelling;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IMac on 9/1/2014.
 */
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = { "email_address" } ) })
@NamedQueries({
	@NamedQuery(name="findExistingUserByEmailAddress",
		query="select user from User user where user.emailAddress = :emailAddress"),
	@NamedQuery(name="findExistingUserByUserNameAndPassword",
		query="select user from User user where user.username = :username and user.password = :password")
})
public class User extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1;
	@Column(name = "username")
	public String username;
	@Column(name = "password")
	public String password;
	@Column(name = "email_address", unique=true, nullable=false, updatable=true)
	public String emailAddress;
	@ManyToOne
	@JoinColumn(name = "location_id")
	public Location location;

	public User() {}

	public User(String username, String password, String emailAddress, Location location) {
		this.username = username;
		this.password = password;
		this.emailAddress = emailAddress;
		this.location = location;
	}
}
