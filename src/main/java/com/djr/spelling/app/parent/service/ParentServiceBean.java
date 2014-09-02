package com.djr.spelling.app.parent.service;

import com.djr.spelling.Location;
import com.djr.spelling.User;
import com.djr.spelling.Word;
import java.util.List;

/**
 * Created by IMac on 9/1/2014.
 */
public class ParentServiceBean {
	public boolean createParentAccount(User user) {
		return false;
	}

	public boolean createChildAccount(User user) {
		return false;
	}

	public boolean createOrFindWord(Word word) {
		return false;
	}

	public boolean createOrFindLocation(Location location) {
		return false;
	}

	public User findParentAccount(String username, String password) {
		return null;
	}

	public List<User> findParentChildren(User user) {
		return null;
	}

	public boolean changeChildPassword(User user) {
		return false;
	}
}
