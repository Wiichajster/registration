package util;

import java.security.Principal;

import javax.security.auth.login.LoginException;

import org.jboss.security.auth.spi.DatabaseServerLoginModule;

public class CustomDbLoginModule extends DatabaseServerLoginModule {

	private CustomPrincipal principal;

	@Override
	public boolean login() throws LoginException {
		boolean result = super.login();
		if (result) {
			principal = new CustomPrincipal(getUsername(), false);
			//result = true;
		}

		return result;
	}

	@Override
	protected void setValidateError(Throwable validateError) {
		super.setValidateError(validateError);
	}

	@Override
	protected Principal getIdentity() {
		return principal != null ? this.principal : super.getIdentity();
	}
}
