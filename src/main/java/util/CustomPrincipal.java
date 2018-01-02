package util;

import org.jboss.security.SimplePrincipal;

public class CustomPrincipal extends SimplePrincipal {

	private static final long serialVersionUID = -8502489535127330438L;

	private boolean isActivated;

	public CustomPrincipal(String name, boolean isActivated) {
		super(name);
		this.isActivated = isActivated;
	}

	public boolean isActivated() {
		return isActivated;
	}

	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}

}
