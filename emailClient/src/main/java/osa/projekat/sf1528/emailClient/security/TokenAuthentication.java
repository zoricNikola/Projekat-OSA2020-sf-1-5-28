package osa.projekat.sf1528.emailClient.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class TokenAuthentication extends AbstractAuthenticationToken {
	
	private static final long serialVersionUID = 3061964054314017667L;
	
	private String token;
	private final UserDetails principle;
	
	public TokenAuthentication(UserDetails principle) {
		super(principle.getAuthorities());
		this.principle = principle;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public Object getPrincipal() {
		return principle;
	}

}
