package com.djr.spelling.app.parent.service;

import com.djr.spelling.ChildUser;
import com.djr.spelling.User;
import com.djr.spelling.Word;
import com.djr.spelling.WordLocation;
import com.djr.spelling.app.exceptions.SpellingException;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.slf4j.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by IMac on 9/1/2014.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ParentServiceBean {
	@PersistenceContext(name = "SpellingPersistence")
	private EntityManager em;
	@Inject
	private Logger log;
	@Inject
	private DoubleMetaphone doubleMetaphone;

	public void createParentAccount(User user, String trackingId)
	throws SpellingException {
		log.debug("createParentAccount() user:{}, trackingId:{}", user, trackingId);
		try {
			TypedQuery<User> query = em.createNamedQuery("findExistingUserByEmailAddress", User.class);
			query.setParameter("emailAddress", user.emailAddress);
			query.getSingleResult();
			throw new SpellingException("Account already exists");
		} catch (NoResultException nrEx) {
			log.debug("createParentAccount() no results found, so good to continue");
		}
		em.persist(user);
	}

	public void createChildAccount(ChildUser user, String trackingId)
	throws SpellingException {
		log.debug("createChildAccount() user:{}, trackingId:{}", user, trackingId);
		try {
			TypedQuery<ChildUser> query = em.createNamedQuery("findExistingChildUserByUsername", ChildUser.class);
			query.setParameter("username", user.username);
			query.getSingleResult();
			throw new SpellingException("Account already exists");
		} catch (NoResultException nrEx) {
			log.debug("createChildAccount() no results found, so continuing");
		}
		em.persist(user);
	}

	public User findParentAccount(User user, String trackingId)
	throws SpellingException {
		log.debug("findParentAccount() user:{}, trackingId:{}", user, trackingId);
		try {
			TypedQuery<User> query = em.createNamedQuery("findExistingUserByUserNameAndPassword", User.class);
			query.setParameter("emailAddress", user.emailAddress);
			query.setParameter("password", user.password);
			return query.getSingleResult();
		} catch (NoResultException nrEx) {
			log.debug("findParentAccount() no user account found trackingId:{}", trackingId);
			throw new SpellingException("No user account found or incorrect username / password");
		}
	}

	public User findParentAccount(Integer userId, String trackingId)
	throws SpellingException {
		log.debug("findParentAccount() userId:{}, trackingId:{}", userId, trackingId);
		try {
			return em.find(User.class, userId);
		} catch (Exception ex) {
			log.error("findParentAccount() trackingId:{}", trackingId);
			throw new SpellingException("Something went wrong when attempting to find parent account " + userId);
		}
	}

	public void editParentPassword(User original, User edited, String trackingId) {
		log.debug("editParentPassword() original:{}, edited:{}, trackingId:{}", original, edited, trackingId);
		original.password = edited.password;
		if (!em.contains(original)) {
			em.merge(original);
		}
	}

	public List<ChildUser> findParentChildren(User user, String trackingId)
	throws SpellingException {
		log.debug("findParentChildren() user:{}, trackingId:{}", user, trackingId);
		try {
			TypedQuery<ChildUser> query = em.createNamedQuery("findChildrenUsersByParentUser", ChildUser.class);
			query.setParameter("parent", user);
			return query.getResultList();
		} catch (NoResultException nrEx) {
			log.debug("findParentChildren() no children found. trackingId:{}", trackingId);
			throw new SpellingException("No children found for parent");
		}
	}

	public ChildUser findParentChild(Integer childId, String trackingId)
	throws SpellingException {
		log.debug("findParentChild() childId:{}, trackingId:{}", childId, trackingId);
		try {
			return em.find(ChildUser.class, childId);
		} catch (NoResultException nrEx) {
			log.debug("findParentChild() no child found. trackingId:{}", trackingId);
			throw new SpellingException("No child found for parent");
		}
	}

	public void editChildPassword(ChildUser original, ChildUser edited, String trackingId) {
		log.debug("findParentChildren() original:{}, edited:{}, trackingId:{}", original, edited, trackingId);
		original.password = edited.password;
		if (!em.contains(original)) {
			em.merge(original);
		}
	}

	public void createOrFindWord(User user, WordLocation wordLocation, Word word, String trackingId)
	throws SpellingException {
		log.debug("createOrFindWord() user:{}, wordLocation:{}, word:{}, trackingId:{}", user, wordLocation, word, trackingId);
		Word existingWord = null;
		try {
			existingWord = findWord(word, trackingId);
			if (existingWord == null) {
				existingWord = word;
				em.persist(existingWord);
			}
		} catch (Exception ex) {
			log.debug("createOrFindWord() word probably was created at same time. trackingId:{}", trackingId);
			existingWord = findWord(word, trackingId);
			if (existingWord == null) {
				throw new SpellingException("Something has gone horribly wrong with this word.");
			}
		}
		createOrFindWordLocation(wordLocation, existingWord, trackingId);
	}

	private Word findWord(Word word, String trackingId) {
		log.debug("findWord() word:{}, trackingId:{}", word, trackingId);
		try {
			TypedQuery<Word> query = em.createNamedQuery("findWord", Word.class);
			query.setParameter("word", word.word);
			query.setParameter("metaphone", word.metaphone);
			Word foundWord = query.getSingleResult();
			log.debug("findWord() foundWord:{}, trackingId:{}", foundWord, trackingId);
			return foundWord;
		} catch (NoResultException nrEx) {
			log.debug("findWord() nothing found. trackingId{}", trackingId);
			return null;
		}
	}

	private WordLocation findWordLocation(WordLocation wordLocation, Word word, String trackingId) {
		log.debug("findWordLocation() wordLocation:{}, word:{}, trackingId:{}", wordLocation, trackingId);
		try {
			TypedQuery<WordLocation> query = em.createNamedQuery("findWordLocation", WordLocation.class);
			query.setParameter("location", wordLocation.location);
			query.setParameter("grade", wordLocation.grade);
			query.setParameter("word", word);
			WordLocation foundLocation = query.getSingleResult();
			log.debug("findWordLocation() foundLocation:{}, trackingId{}", foundLocation, trackingId);
			return foundLocation;
		} catch (NoResultException nrEx) {
			log.debug("findWordLocation() nothing found. trackingId:{}", trackingId);
			return null;
		}
	}

	private void createOrFindWordLocation(WordLocation wordLocation, Word word, String trackingId)
	throws SpellingException {
		WordLocation existingWordLocation = null;
		try {
			existingWordLocation = findWordLocation(wordLocation, word, trackingId);
			if (existingWordLocation == null) {
				existingWordLocation = wordLocation;
				em.persist(existingWordLocation);
			}
		} catch (Exception ex) {
			log.debug("createOrFindWord() word probably was created at same time. trackingId:{}", trackingId);
			existingWordLocation = findWordLocation(wordLocation, word, trackingId);
			if (existingWordLocation == null) {
				throw new SpellingException("Something has gone horribly wrong with this word location!");
			}
		}
	}

	public void editWord(User user, Word originalWord, Word editedWord, String trackingId) {
		log.debug("editWord() user:{}, word:{}, trackingId:{}");
		originalWord.word = editedWord.word;
		originalWord.metaphone = editedWord.metaphone;
		if (!em.contains(originalWord)) {
			em.merge(originalWord);
		}
	}
}
