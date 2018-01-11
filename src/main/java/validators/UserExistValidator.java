package validators;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ejb.RegistrationRepository;

public class UserExistValidator implements ConstraintValidator<UserExist, String> {

	@Inject
	private RegistrationRepository repo;

	@Override
	public void initialize(UserExist constraintAnnotation) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		boolean result = true;

		if (value == null) {
			return result;
		}

		boolean isExist = false;

		try {
			isExist = repo.checkLoginUsage(value);
		} catch (IllegalArgumentException ex) {

		}
		if (!isExist) {
			result = false;
		}

		return result;
	}

	public RegistrationRepository getRepo() {
		return repo;
	}

	public void setRepo(RegistrationRepository repo) {
		this.repo = repo;
	}

}
