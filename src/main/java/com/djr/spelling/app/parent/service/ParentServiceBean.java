package com.djr.spelling.app.parent.service;

import com.djr.spelling.ChildUser;
import com.djr.spelling.Sentence;
import com.djr.spelling.User;
import com.djr.spelling.Week;
import com.djr.spelling.Word;
import com.djr.spelling.WordLocation;
import com.djr.spelling.app.exceptions.SpellingException;
import com.djr.spelling.app.parent.exceptions.ParentAuthException;
import com.djr.spelling.app.parent.exceptions.ParentManageChildrenException;
import com.djr.spelling.app.parent.exceptions.ParentWordException;
import com.djr.spelling.app.parent.restapi.ParentApiConstants;
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
import java.util.Date;
import java.util.List;

/**
 * Created by IMac on 9/1/2014.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ParentServiceBean {
	@PersistenceContext(name = "jdbc/SpellingPersistence")
	private EntityManager em;
	@Inject
	private Logger log;
	@Inject
	private DoubleMetaphone doubleMetaphone;

	public void createParentAccount(User user, String trackingId)
	throws ParentAuthException {
		log.debug("createParentAccount() user:{}, trackingId:{}", user, trackingId);
		try {
			TypedQuery<User> query = em.createNamedQuery("findExistingUserByEmailAddress", User.class);
			query.setParameter("emailAddress", user.emailAddress);
			query.getSingleResult();
			throw new ParentAuthException(ParentApiConstants.EMAIL_EXISTS);
		} catch (NoResultException nrEx) {
			log.debug("createParentAccount() no results found, so good to continue");
		} catch (Exception ex) {
			log.debug("createParentAccount() exception occurred", ex);
			throw new ParentAuthException(ParentApiConstants.GENERAL_CREATE);
		}
		em.persist(user);
	}

	public void createChildAccount(ChildUser user, String trackingId)
	throws ParentManageChildrenException {
		log.debug("createChildAccount() user:{}, trackingId:{}", user, trackingId);
		try {
			TypedQuery<ChildUser> query = em.createNamedQuery("findExistingChildUserByUsername", ChildUser.class);
			query.setParameter("username", user.username);
			query.getSingleResult();
			throw new ParentAuthException(ParentApiConstants.CHILD_EXISTS);
		} catch (NoResultException nrEx) {
			log.debug("createChildAccount() no results found, so continuing");
		} catch (Exception ex) {
			log.debug("createChildAccount() exception occurred", ex);
			throw new ParentManageChildrenException(ParentApiConstants.CHILD_CREATE_GENERAL_FAIL);
		}
		log.debug("createChildAccount() persisting childUser:{}", user);
		em.persist(user);
	}

	public User findParentAccount(User user, String trackingId)
	throws ParentAuthException {
		log.debug("findParentAccount() user:{}, trackingId:{}", user, trackingId);
		try {
			TypedQuery<User> query = em.createNamedQuery("findExistingUserByUserNameAndPassword", User.class);
			query.setParameter("emailAddress", user.emailAddress);
			query.setParameter("password", user.password);
			return query.getSingleResult();
		} catch (NoResultException nrEx) {
			log.debug("findParentAccount() no user account found trackingId:{}", trackingId);
			throw new ParentAuthException(ParentApiConstants.USER_NOT_FOUND);
		} catch (Exception ex) {
			log.debug("findParentAccount() general exception occurred", ex);
			throw new ParentAuthException(ParentApiConstants.GENERAL_AUTH);
		}
	}

	public User findParentAccount(Integer userId, String trackingId)
	throws ParentAuthException {
		log.debug("findParentAccount() userId:{}, trackingId:{}", userId, trackingId);
		try {
			return em.find(User.class, userId);
		} catch (Exception ex) {
			log.error("findParentAccount() trackingId:{}, userId:{}", trackingId, userId);
			throw new ParentAuthException(ParentApiConstants.FIND_PARENT_BY_ID);
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
	throws ParentManageChildrenException {
		log.debug("findParentChildren() user:{}, trackingId:{}", user, trackingId);
		try {
			TypedQuery<ChildUser> query = em.createNamedQuery("findChildrenUsersByParentUser", ChildUser.class);
			query.setParameter("parent", user);
			return query.getResultList();
		} catch (NoResultException nrEx) {
			log.debug("findParentChildren() no children found. trackingId:{}", trackingId);
			throw new ParentManageChildrenException(ParentApiConstants.NO_CHILDREN_FOUND);
		} catch (Exception ex) {
			throw new ParentManageChildrenException(ParentApiConstants.FIND_PARENT_CHILDREN_FAILED);
		}
	}

	public ChildUser findParentChild(Integer childId, String trackingId)
	throws ParentManageChildrenException {
		log.debug("findParentChild() childId:{}, trackingId:{}", childId, trackingId);
		try {
			return em.find(ChildUser.class, childId);
		} catch (NoResultException nrEx) {
			log.debug("findParentChild() no child found. trackingId:{}", trackingId);
			throw new ParentManageChildrenException(ParentApiConstants.NO_CHILD_BY_ID);
		} catch (Exception ex) {
			throw new ParentManageChildrenException(ParentApiConstants.FIND_PARENT_CHILD_FAILED);
		}
	}

	public void editChildPassword(ChildUser original, String password, String trackingId)
	throws ParentManageChildrenException {
		log.debug("findParentChildren() original:{}, password:{}, trackingId:{}", original, password, trackingId);
		original.password = password;
		try {
			if (!em.contains(original)) {
				em.merge(original);
			}
		} catch (Exception ex) {
			throw new ParentManageChildrenException(ParentApiConstants.EDIT_CHILD_PASSWORD_FAILED);
		}
	}

	public Word createOrFindWord(User user, Word word, String trackingId)
	throws ParentWordException {
		log.debug("createOrFindWord() user:{}, word:{}, trackingId:{}", user, word, trackingId);
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
				log.error("createOrFindWord() ex:{}", ex);
				throw new ParentWordException(ParentApiConstants.CREATE_OR_FIND_WORD_FAILED);
			}
		}
		return existingWord;
	}

	public Word findWord(Word word, String trackingId) {
		log.debug("findWord() trackingId:{}", trackingId);
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

	public void createOrFindWordLocation(WordLocation wordLocation, String trackingId)
	throws ParentWordException {
		log.debug("createOrFindWordLocation() wordLocation:{}, trackingId:{}", wordLocation, trackingId);
		WordLocation existingWordLocation = null;
		try {
			existingWordLocation = findWordLocation(wordLocation, trackingId);
			if (existingWordLocation == null) {
				existingWordLocation = wordLocation;
				em.persist(existingWordLocation);
			}
		} catch (Exception ex) {
			log.debug("createOrFindWord() word probably was created at same time. trackingId:{}", trackingId);
			existingWordLocation = findWordLocation(wordLocation, trackingId);
			if (existingWordLocation == null) {
				log.error("createOrFindWordLocation ex:{}", ex);
				throw new ParentWordException(ParentApiConstants.CREATE_OR_FIND_WORD_LOCATION_FAILED);
			}
		}
	}

	private WordLocation findWordLocation(WordLocation wordLocation, String trackingId) {
		log.debug("findWordLocation() trackingId:{}", trackingId);
		try {
			TypedQuery<WordLocation> query = em.createNamedQuery("findWordLocation", WordLocation.class);
			query.setParameter("location", wordLocation.location);
			query.setParameter("grade", wordLocation.grade);
			query.setParameter("word", wordLocation.word);
			WordLocation foundLocation = query.getSingleResult();
			log.debug("findWordLocation() foundLocation:{}, trackingId{}", foundLocation, trackingId);
			return foundLocation;
		} catch (NoResultException nrEx) {
			log.debug("findWordLocation() nothing found. trackingId:{}", trackingId);
			return null;
		}
	}

	public void createOrFindWordSentence(Sentence sentence, String trackingId)
	throws ParentWordException {
		log.debug("createOrFindWordSentence() sentence:{}, trackingId:{}", sentence, trackingId);
		Sentence existingSentence = null;
		try {
			existingSentence = findSentence(sentence, trackingId);
			if (existingSentence == null) {
				existingSentence = sentence;
				em.persist(existingSentence);
			}
		} catch (Exception ex) {
			log.debug("createOrFindWordSentence() sentence/word combo probably exists already. trackingId:{}", trackingId);
			existingSentence = findSentence(sentence, trackingId);
			if (existingSentence == null) {
				log.error("createOrFindWordSentence() ex:{}", ex);
				throw new ParentWordException(ParentApiConstants.CREATE_OR_FIND_WORD_SENTENCE_FAILED);
			}
		}
	}

	private Sentence findSentence(Sentence sentence, String trackingId) {
		log.debug("findSentence() trackingId:{}", trackingId);
		try {
			TypedQuery<Sentence> query = em.createNamedQuery("findSentence", Sentence.class);
			query.setParameter("sentence", sentence.sentence);
			query.setParameter("word", sentence.word);
			return query.getSingleResult();
		} catch (NoResultException nrEx) {
			log.debug("findSentence() no results found. trackingId:{}", trackingId);
			return null;
		}
	}

	public Week createOrFindWeek(Week week, String trackingId)
	throws ParentWordException {
		log.debug("createOrFindWeek() week:{}, trackingId:{}", week, trackingId);
		Week existingWeek = null;
		try {
			existingWeek = findWeek(week, trackingId);
			if (existingWeek == null) {
				existingWeek = week;
				em.persist(existingWeek);
			}
		} catch (Exception ex) {
			log.debug("createOrFindWeek() week likely exists already. trackingId:{}", trackingId);
			existingWeek = findWeek(week, trackingId);
			if (existingWeek == null) {
				log.error("createOrFindWeek() ex:{}",ex);
				throw new ParentWordException(ParentApiConstants.CREATE_OR_FIND_WEEK_FAILED);
			}
		}
		return existingWeek;
	}

	private Week findWeek(Week week, String trackingId) {
		log.debug("findWeek() trackingId:{}", trackingId);
		try {
			TypedQuery<Week> query = em.createNamedQuery("findWeek", Week.class);
			query.setParameter("weekStart", week.weekStart);
			query.setParameter("weekEnd", week.weekEnd);
			return query.getSingleResult();
		} catch (NoResultException nrEx) {
			log.debug("findWeek() no results found. trackingId:{}", trackingId);
			return null;
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

	public boolean confirmPasswords(String password, String confirmPassword)
	throws ParentAuthException {
		if (!password.equals(confirmPassword)) {
			throw new ParentAuthException(ParentApiConstants.NOT_CONFIRMED);
		}
		return true;
	}
}
