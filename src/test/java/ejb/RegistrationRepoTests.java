package ejb;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;

import exceptions.UserAlreadyExistException;
import model.User;

public class RegistrationRepoTests {

	private RegistrationRepositoryImpl repo;

	@Before
	public void init() {
		repo = new RegistrationRepositoryImpl();
		repo.setEm(mock(EntityManager.class));
	}

	@Test
	public void inAddIfUserNullThenFalse()
			throws ConstraintViolationException, MessagingException, UserAlreadyExistException {
		boolean result = repo.add(null);

		assertFalse(result);
	}
	
	@Test
	public void ifUserExistThenTrue() {
		EntityManager em = repo.getEm();
		User user = new User("test","test","test@test.pl");
		
		when(em.find(User.class, "test")).thenReturn(user);
		
		boolean result = repo.checkLoginUsage("test");
		
		assertTrue(result);
	}
	
	@Test
	public void ifUserDontExistThenTrue() {
		EntityManager em = repo.getEm();
		
		when(em.find(User.class, "test")).thenReturn(null);
		
		boolean result = repo.checkLoginUsage("test");
		
		assertFalse(result);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ifUserIsNullThenException() {
		repo.checkLoginUsage(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ifUserIsEmptyStringThenException() {
		repo.checkLoginUsage(null);
	}
}
