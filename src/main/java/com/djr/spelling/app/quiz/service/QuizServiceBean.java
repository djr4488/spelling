package com.djr.spelling.app.quiz.service;

import javax.ejb.Schedule;
import javax.ejb.Stateless;

/**
 * Created by IMac on 2/22/2015.
 */
@Stateless
public class QuizServiceBean {
	@Schedule(second = "0", minute = "0", hour = "0", dayOfWeek = "Sat")
	public void generateWeeklyNationalQuizzes() {
		/**
		 * TODO -
		 * 1. should create a 20 to 30 word quiz based
		 * 2. should be one quiz generated for each grade level
		 */
	}
}
