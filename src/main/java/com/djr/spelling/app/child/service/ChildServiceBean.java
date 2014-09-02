package com.djr.spelling.app.child.service;

import com.djr.spelling.*;
import com.djr.spelling.app.child.model.WordStructure;
import java.util.List;

/**
 * Created by IMac on 9/1/2014.
 */
public class ChildServiceBean {
	/**
	 * Method to get weekly quiz words, this will attempt each word in the weekly list one time and provide a result
	 *
	 * @param howManyWords int
	 * @return List<WordStructure>
	 */
	public List<WordStructure> getWeeklyQuizWords(int howManyWords) {
		return null;
	}

	/**
	 * Method to get quiz words by grade and location.  This will generate a quiz using all words known by grade and
	 * location.
	 *
	 * @param grade Grade
	 * @param location Location
	 * @param howManyWords int
	 * @return List<WordStructure>
	 */
	public List<WordStructure> getQuizWordsByGradeAndLocation(Grade grade, Location location, int howManyWords) {
		return null;
	}

	/**
	 * Method to generate quiz words by Grade, City, and State.
	 *
	 * @param grade Grade
	 * @param city City
	 * @param state State
	 * @param howManyWords int
	 * @return List<WordStructure>
	 */
	public List<WordStructure> getPracticeWordsByGradeAndCityState(Grade grade, City city, State state, int howManyWords) {
		return null;
	}

	/**
	 * Method to generate quiz words by Grade only.
	 *
	 * @param grade Grade
	 * @param howManyWords int
	 * @return List<WordStructure>
	 */
	public List<WordStructure> getPracticeWordsByGrade(Grade grade, int howManyWords) {
		return null;
	}

	/**
	 * Method to simple auth a child account
	 *
	 * @param username String
	 * @param password String
	 * @return User
	 */
	public User findChildAccount(String username, String password) {
		return null;
	}
}
