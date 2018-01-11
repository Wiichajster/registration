package validators;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.validation.ConstraintValidatorContext;

import org.junit.Before;
import org.junit.Test;

import ejb.RegistrationRepository;

public class UserExistValidatorTests {

	private UserExistValidator ueval;

	@Before
	public void init() {
		ueval = new UserExistValidator();
		ueval.setRepo(mock(RegistrationRepository.class));
	}

	@Test
	public void ifUserDontExistThenFalse() {
		RegistrationRepository repo = ueval.getRepo();
		when(repo.checkLoginUsage("Wiichajster")).thenReturn(false);

		ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

		boolean result = ueval.isValid("Wiichajster", context);
		assertFalse(result);
	}

	@Test
	public void ifNullPassedThenTrue() {
		RegistrationRepository repo = ueval.getRepo();
		when(repo.checkLoginUsage(null)).thenThrow(IllegalArgumentException.class);
		
		ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

		boolean result = ueval.isValid(null, context);

		assertTrue(result);
	}

	@Test
	public void ifEmptyStringPassedThenFalse() {
		RegistrationRepository repo = ueval.getRepo();
		when(repo.checkLoginUsage("")).thenThrow(IllegalArgumentException.class);

		ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

		boolean result = ueval.isValid("", context);

		assertFalse(result);
	}

	@Test
	public void ifUserExistThenTrue() {
		RegistrationRepository repo = ueval.getRepo();
		when(repo.checkLoginUsage("Wiichajster")).thenReturn(true);

		ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

		boolean result = ueval.isValid("Wiichajster", context);
		assertTrue(result);
	}

}
