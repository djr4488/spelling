package com.djr.spelling;

import com.djr.spelling.app.Constants;
import com.djr.spelling.app.exceptions.SpellingException;
import org.slf4j.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Created by IMac on 9/2/2014.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SpellingService {
	@PersistenceContext(name = "SpellingPersistence")
	private EntityManager em;
	@Inject
	private Logger log;

	public Location createOrFindLocation(Location location)
	throws SpellingException {
		try {
			TypedQuery<Location> query = em.createNamedQuery("findLocation", Location.class);
			query.setParameter("school", location.school);
			query.setParameter("city", location.city);
			query.setParameter("state", location.state);
			return query.getSingleResult();
		} catch (NoResultException nrEx) {
			em.persist(location);
			return location;
		} catch (Exception ex) {
			throw new SpellingException(Constants.CREATE_OR_FIND_LOCATION_FAILED);
		}
	}

	public City createOrFindCity(City city)
	throws SpellingException {
		try {
			TypedQuery<City> query = em.createNamedQuery("findCity", City.class);
			query.setParameter("cityName", city.cityName);
			return query.getSingleResult();
		} catch (NoResultException nrEx) {
			em.persist(city);
			return city;
		} catch (Exception ex) {
			throw new SpellingException(Constants.CREATE_OR_FIND_CITY_FAILED);
		}
	}

	public State createOrFindState(State state)
	throws SpellingException {
		try {
			TypedQuery<State> query = em.createNamedQuery("findState", State.class);
			query.setParameter("stateAbbr", state.stateAbbr);
			return query.getSingleResult();
		} catch (NoResultException nrEx) {
			em.persist(state);
			return state;
		} catch (Exception ex) {
			throw new SpellingException(Constants.CREATE_OR_FIND_STATE_FAILED);
		}
	}

	public School createOrFindSchool(School school)
	throws SpellingException {
		try {
			TypedQuery<School> query = em.createNamedQuery("findSchool", School.class);
			query.setParameter("school", school.schoolName);
			query.setParameter("isPrivate", school.isPrivate);
			query.setParameter("isHome", school.isHome);
			return query.getSingleResult();
		} catch (NoResultException nrEx) {
			em.persist(school);
			return school;
		} catch (Exception ex) {
			throw new SpellingException(Constants.CREATE_OR_FIND_SCHOOL_FAILED);
		}
	}

	public Grade createOrFindGrade(Grade grade)
	throws SpellingException {
		try {
			TypedQuery<Grade> query = em.createNamedQuery("findGrade", Grade.class);
			query.setParameter("grade", grade.grade);
			return query.getSingleResult();
		} catch (NoResultException nrEx) {
			em.persist(grade);
			return grade;
		} catch (Exception ex) {
			throw new SpellingException(Constants.CREATE_OR_FIND_GRADE_FAILED);
		}
	}
}
