package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import validators.UserExist;

@Entity
@Table(name = "users")
@NamedQueries({ @NamedQuery(name = "User.FindByMail", query = "SELECT u FROM User u WHERE u.email = :email") })
public class User implements Serializable {

	private static final long serialVersionUID = -7351729135012380019L;

	@Id
	@NotNull
	@Size(min = 4, max = 20)
	@UserExist
	private String username;
	@NotNull
	@Size(min = 5, max = 60)
	private String password;
	@Email
	private String email;
	private boolean isActive;

	@ElementCollection
	@CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"))
	@Column(name = "role")
	private Set<String> roles = new HashSet<>();

	public User() {

	}

	public User(String username, String password, String email) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.isActive = false;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
}
