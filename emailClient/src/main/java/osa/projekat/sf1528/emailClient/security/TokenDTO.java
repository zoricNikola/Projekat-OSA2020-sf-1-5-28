package osa.projekat.sf1528.emailClient.security;

import osa.projekat.sf1528.emailClient.dto.UserDTO;

public class TokenDTO {
	
	private String jwt;
	
	private UserDTO user;
	
	public TokenDTO() {}
	
	public TokenDTO(String token, UserDTO user) {
		this.jwt = token;
		this.user = user;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}
	
	

}
