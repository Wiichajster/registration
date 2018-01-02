package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.User;
import model.VerificationToken;

@Stateless
public class ActivateRepositoryImpl implements ActivateRepository {

	@PersistenceContext
	private EntityManager em;

	@Override
	public boolean activateUser(String hash) {
		boolean result = false;

		VerificationToken token = getByHash(hash);
		
		if (token != null && token.getExpirationDateTime().compareTo(token.getRegistrationDateTime()) >= 0) {
			User user = token.getUser();
			user.setActive(true);
			em.merge(user);
			result = true;
		}
		return result;
	}

	@Override
	public VerificationToken getByUsername(String username) {
		return em.find(VerificationToken.class, username);
	}

	@Override
	public VerificationToken getByHash(String hash) {
		TypedQuery<VerificationToken> query = em.createNamedQuery("VerificationToken.FindByHash",
				VerificationToken.class);
		query.setParameter("tokenHash", hash);
		VerificationToken token = query.getSingleResult();

		return token;

	}

}
