package model;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import util.HashingHelper;

/*
 * Token weryfikacyjny. Klasa służąca do aktywacji użytkownika. Po jego rejestracji pole isActive użytkownika
 * jest ustawione na false. To uniemożliwa korzystanie z serwisu. Aby aktywować konto trzeba kliknąć na link
 * werfikacyjny przesłany w mailu. Dopiero po tym konto jest aktywowane i można w pełni z niego korzystać.
 */

@Entity
@Table(name = "verification_token")
@NamedQueries({
		@NamedQuery(name = "VerificationToken.FindByUsername", 
					query = "SELECT v FROM VerificationToken v WHERE v.user = :user"),
		@NamedQuery(name = "VerificationToken.FindByHash", 
					query = "SELECT v FROM VerificationToken v WHERE v.tokenHash = :tokenHash")
})
public class VerificationToken implements Serializable {

	private static final long serialVersionUID = -894192812860490703L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	private User user;
	private LocalDateTime registrationDateTime;
	private LocalDateTime expirationDateTime;
	private String tokenHash;

	public VerificationToken() throws NoSuchAlgorithmException {
		registrationDateTime = LocalDateTime.now();
		expirationDateTime = registrationDateTime.plusDays(1);
		// setTokenHash();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getRegistrationDateTime() {
		return registrationDateTime;
	}

	public void setRegistrationDateTime(LocalDateTime registrationDateTime) {
		this.registrationDateTime = registrationDateTime;
	}

	public LocalDateTime getExpirationDateTime() {
		return expirationDateTime;
	}

	public void setExpirationDateTime(LocalDateTime expirationDateTime) {
		this.expirationDateTime = expirationDateTime;
	}

	public String getTokenHash() {
		return tokenHash;
	}

	public void setTokenHash() throws NoSuchAlgorithmException {
		this.tokenHash = HashingHelper.hashString(user.getUsername());
	}
}
