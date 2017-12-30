package ejb;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.VerificationToken;

@RequestScoped
public class ActivateRepositoryImpl implements ActivateRepository {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public boolean activateUser(String hash) {
		boolean result = false;

		TypedQuery<VerificationToken> query = em.createNamedQuery("VerificationToken.FindByHash",
				VerificationToken.class);
		query.setParameter("tokenHash", hash);
		VerificationToken token = query.getSingleResult();
		if (token != null && token.getExpirationDateTime().compareTo(token.getRegistrationDateTime()) >= 0) {
			token.getUser().setActive(true);
			result = true;
		}
		return result;
	}

	@Override
	public VerificationToken getByUsername(String username) {
		return em.find(VerificationToken.class, username);
	}

}
