package com.djr.spelling.app.child.service;


import org.slf4j.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by IMac on 9/1/2014.
 */
public class ChildServiceBean {
	@Inject
	private Logger log;
	@PersistenceContext(name = "jdbc/SpellingPersistence")
	private EntityManager em;
}
