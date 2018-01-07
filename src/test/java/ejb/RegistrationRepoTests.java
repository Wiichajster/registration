package ejb;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

	@Test(expected=UserAlreadyExistException.class)
	public void inAddIfUserExistThrowException() throws ConstraintViolationException, MessagingException, UserAlreadyExistException {
		
		User user = new User("test", "test", "test@test.com");
		EntityManager em = repo.getEm();
		
		when(em.find(User.class, user.getUsername())).thenReturn(user);
	
		repo.add(user);
	}
	
	@Test(expected=UserAlreadyExistException.class)
	public void ifAddThenPersistUser() throws ConstraintViolationException, MessagingException, UserAlreadyExistException {
		
		User user = new User("test", "test", "test@test.com");
		EntityManager em = repo.getEm();
		
		//doAnswer()
		//TODO
	
		repo.add(user);
	}
}
