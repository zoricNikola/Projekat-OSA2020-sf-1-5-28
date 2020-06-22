package osa.projekat.sf1528.emailClient.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "folders")
public class Folder implements Serializable {

	private static final long serialVersionUID = 5002075192275790756L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "folder_id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "name", unique = false, nullable = false)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "parent_id", referencedColumnName = "folder_id", nullable = true)
	private Folder parent;
	
	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "parent")
	private Set<Folder> childFolders = new HashSet<Folder>();
	
	@ManyToOne
	@JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
	private Account account;
	
	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "folder")
	private Set<Message> messages = new HashSet<Message>();
	
	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "folder")
	private Set<Rule> rules;
	
	public Folder() {}

	public void addChildFolder(Folder folder) {
		if (folder.getParent() != null)
			folder.getParent().removeChildFolder(folder);
		folder.setParent(this);
		getChildFolders().add(folder);
	}
	
	public void removeChildFolder(Folder folder) {
		folder.setParent(null);
		getChildFolders().remove(folder);
	}
	
	public void addMessage(Message message) {
		if (message.getFolder() != null)
			message.getFolder().removeMessage(message);
		message.setFolder(this);
		getMessages().add(message);
	}
	
	public void removeMessage(Message message) {
		message.setFolder(null);
		getMessages().remove(message);
	}
	
	public void addRule(Rule rule) {
		if (rule.getDestination() != null)
			rule.getDestination().removeRule(rule);
		rule.setDestination(this);
		getRules().add(rule);
	}
	
	public void removeRule(Rule rule) {
		rule.setDestination(null);
		getRules().remove(rule);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Folder getParent() {
		return parent;
	}

	public void setParent(Folder parrent) {
		this.parent = parrent;
	}

	public Set<Folder> getChildFolders() {
		return childFolders;
	}

	public void setChildFolders(Set<Folder> childFolders) {
		this.childFolders = childFolders;
	}


	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	public Set<Message> getMessages() {
		return messages;
	}

	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}
	
	public Set<Rule> getRules() {
		return rules;
	}

	public void setRules(Set<Rule> rules) {
		this.rules = rules;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
