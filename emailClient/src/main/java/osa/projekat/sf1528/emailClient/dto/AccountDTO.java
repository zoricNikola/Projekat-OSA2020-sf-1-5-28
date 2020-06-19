package osa.projekat.sf1528.emailClient.dto;

import java.io.Serializable;

import osa.projekat.sf1528.emailClient.model.Account;

public class AccountDTO implements Serializable {

	private static final long serialVersionUID = -4719862163497779925L;
	
	private Long id;
	
	private String smtpAddress;
	
	private Integer smtpPort;
	
	private Short inServerType;
	
	private String inServerAddress;
	
	private Integer inServerPort;
	
	private String username;
	
	private String password;
	
	private String displayName;
	
	public AccountDTO() {}
	
	public AccountDTO(Long id, String smtpAddress, Integer smtpPort, Short inServerType, String inServerAddress,
			Integer inServerPort, String username, String password, String displayName) {
		super();
		this.id = id;
		this.smtpAddress = smtpAddress;
		this.smtpPort = smtpPort;
		this.inServerType = inServerType;
		this.inServerAddress = inServerAddress;
		this.inServerPort = inServerPort;
		this.username = username;
		this.password = password;
		this.displayName = displayName;
	}


	public AccountDTO(Account account) {
		this(account.getId(), account.getSmtpAddress(), account.getSmtpPort(), account.getInServerType(), account.getInServerAddress(), 
				account.getInServerPort(), account.getUsername(), account.getPassword(), account.getDisplayName());
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSmtpAddress() {
		return smtpAddress;
	}

	public void setSmtpAddress(String smtpAddress) {
		this.smtpAddress = smtpAddress;
	}

	public Integer getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(Integer smtpPort) {
		this.smtpPort = smtpPort;
	}

	public Short getInServerType() {
		return inServerType;
	}

	public void setInServerType(Short inServerType) {
		this.inServerType = inServerType;
	}

	public String getInServerAddress() {
		return inServerAddress;
	}

	public void setInServerAddress(String inServerAddress) {
		this.inServerAddress = inServerAddress;
	}

	public Integer getInServerPort() {
		return inServerPort;
	}

	public void setInServerPort(Integer inServerPort) {
		this.inServerPort = inServerPort;
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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
