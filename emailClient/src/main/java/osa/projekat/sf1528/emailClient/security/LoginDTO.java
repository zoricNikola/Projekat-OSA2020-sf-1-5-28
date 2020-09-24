package osa.projekat.sf1528.emailClient.security;

import java.io.Serializable;

public class LoginDTO implements Serializable {
	
	private static final long serialVersionUID = -408442848890908044L;
	
	private String username;
	private String password;
	
	public LoginDTO() {
		super();
	}

	public LoginDTO(String username, String password) {
		super();
		this.username = username;
		this.password = password;
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
	
	

}
