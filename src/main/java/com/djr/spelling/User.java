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
		query="select user from User user where user.emailAddress = :emailAddress and user.password = :password")
})
public class User extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1;
	@Column(name = "username")
	public String username;
	@Column(name = "password")
	public String password;
	@Column(name = "email_address", unique=true, nullable=false, updatable=true)
	public String emailAddress;

	public User() {}

	public User(String username, String password, String emailAddress) {
		this.username = username;
		this.password = password;
		this.emailAddress = emailAddress;
	}

	public User(String emailAddress, String password) {
		this.emailAddress = emailAddress;
		this.password = password;
	}

	public User(String password) {
		this.password = password;
	}

	public User(User userEditted) {
		this.password = userEditted.password;
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("User[username=").append(username).append(", password=****").append(", email_address=");
		stringBuffer.append(emailAddress).append("]");
		return stringBuffer.toString();
	}
}
