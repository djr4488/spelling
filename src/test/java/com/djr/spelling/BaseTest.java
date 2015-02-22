package com.djr.spelling;

import junit.framework.TestCase;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;

/**
 * Created by IMac on 2/21/2015.
 */
public abstract class BaseTest extends TestCase {
	public void  addIntegerField(Object object, String fieldName, Integer value)
	throws Exception {
		Field field = object.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(object, value);
	}

	public void addLogger(Object object, String fieldName)
	throws Exception {
		Field field = object.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(object, LoggerFactory.getLogger(object.getClass()));
	}
}
