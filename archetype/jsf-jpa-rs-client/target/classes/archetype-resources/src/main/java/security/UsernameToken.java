package ${package}.security;

import org.apache.shiro.authc.AuthenticationToken;

public class UsernameToken implements AuthenticationToken {
	private static final long serialVersionUID = 1L;
	
	private String username;

	public UsernameToken(String username) {
		this.username = username;
	}

	public String getPrincipal() {
		return username;
	}

	public String getCredentials() {
		return username;
	}
}