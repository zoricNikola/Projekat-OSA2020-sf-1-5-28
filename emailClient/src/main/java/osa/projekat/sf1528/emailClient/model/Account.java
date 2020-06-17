package osa.projekat.sf1528.emailClient.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "accounts")
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "smtp_address", unique = false, nullable = false)
	private String smtpAddress;
	
	@Column(name = "smtp_port", unique = false, nullable = false)
	private Integer smtpPort;
	
	@Column(name = "in_server_type", unique = false, nullable = false)
	private Short inServerType;
	
	@Column(name = "in_server_address", unique = false, nullable = false)
	private String inServerAddress;
	
	@Column(name = "in_server_port", unique = false, nullable = false)
	private Integer inServerPort;
	
	@Column(name = "account_username", unique = false, nullable = false)
	private String username;

	@Column(name = "account_password", unique = false, nullable = false)
	private String password;
	
	@Column(name = "account_display_name", unique = false, nullable = false)
	private String displayName;
	
//	@ManyToOne
//	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
//	private User user;
	
//	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "account")
//	private Set<Folder> folders = new HashSet<Folder>();
	
	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "account")
	private Set<Message> messages = new HashSet<Message>();
	
	public Account() {}

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
	
	public Set<Message> getMessages() {
		return messages;
	}

	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
