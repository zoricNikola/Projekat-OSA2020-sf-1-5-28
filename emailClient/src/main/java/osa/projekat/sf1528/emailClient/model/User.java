package osa.projekat.sf1528.emailClient.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User implements Serializable{
	
	private static final long serialVersionUID = 1779429948963437142L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "username", unique = true, nullable = false)
	private String username;
	
	@Column(name = "password", unique = false, nullable = false)
	private String password;
	
	@Column(name = "first_name", unique = false, nullable = false)
	private String firstName;
	
	@Column(name = "last_name", unique = false, nullable = false)
	private String lastName;
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "user")
	private Set<Account> accounts = new HashSet<Account>();
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "user")
	private Set<Tag> tags = new HashSet<Tag>();
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "user")
	private Set<Contact> contacts = new HashSet<Contact>();

	public User() {}

	public void addAccount(Account account) {
		if (account.getUser() != null)
			account.getUser().removeAccount(account);
		account.setUser(this);
		getAccounts().add(account);
	}
	
	public void removeAccount(Account account) {
		account.setUser(null);
		getAccounts().remove(account);
	}
	
	public void addTag(Tag tag) {
		if (tag.getUser() != null)
			tag.getUser().removeTag(tag);
		tag.setUser(this);
		getTags().add(tag);
			
	}
	
	public void removeTag(Tag tag) {
		tag.setUser(null);
		getTags().remove(tag);
	}
	
	public void addContact(Contact contact) {
		if (contact.getUser() != null)
			contact.getUser().removeContact(contact);
		contact.setUser(this);
		getContacts().add(contact);
	}
	
	public void removeContact(Contact contact) {
		contact.setUser(null);
		getContacts().remove(contact);
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

	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public Set<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}

	public static long getSerilversionuid() {
		return serialVersionUID;
	}

}
