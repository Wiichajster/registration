package ejb;

import model.VerificationToken;

public interface ActivateRepository {
	public boolean activateUser(String hash);

	public VerificationToken getByUsername(String username);

	public VerificationToken getByHash(String hash);
}
