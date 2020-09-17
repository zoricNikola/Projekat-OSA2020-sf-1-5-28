package osa.projekat.sf1528.emailClient.dto;

import java.io.Serializable;

import osa.projekat.sf1528.emailClient.model.User;
import osa.projekat.sf1528.emailClient.util.Base64;
import osa.projekat.sf1528.emailClient.util.FilesUtil;

public class UserDTO implements Serializable{

	private static final long serialVersionUID = 1898793168537242133L;
	
	private Long id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String encodedAvatarData;
	
	public UserDTO() {}
	
	public UserDTO(Long id, String username, String password, String firstName, String lastName, String encodedAvatarData) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.encodedAvatarData = encodedAvatarData;
	}
	
	public UserDTO(User user) {
		this(user.getId(), user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getAvatarPath());
		
		if (user.getAvatarPath() != null && !user.getAvatarPath().isEmpty())
			this.encodedAvatarData = Base64.encodeToString(FilesUtil.readBytes(user.getAvatarPath()));
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEncodedAvatarData() {
		return encodedAvatarData;
	}

	public void setEncodedAvatarData(String encodedAvatarData) {
		this.encodedAvatarData = encodedAvatarData;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
