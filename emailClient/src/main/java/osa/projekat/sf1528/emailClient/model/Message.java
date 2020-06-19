package osa.projekat.sf1528.emailClient.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "messages")
public class Message implements Serializable {

	private static final long serialVersionUID = 2L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "from", unique = false, nullable = false)
	private String from;
	
	@Column(name = "to", unique = false, nullable = false)
	private String to;
	
	@Column(name = "cc", unique = false, nullable = false)
	private String cc;
	
	@Column(name = "bcc", unique = false, nullable = false)
	private String bcc;
	
	@Column(name = "date_time", unique = false, nullable = false)
	private LocalDateTime dateTime;
	
	@Column(name = "subject", unique = false, nullable = false)
	private String subject;
	
	@Column(name = "content", unique = false, nullable = false)
	private String content;
	
	@Column(name = "unread", unique = false, nullable = false)
	private boolean unread;
	
	@ManyToOne
	@JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
	private Account account;
	
//	@ManyToOne
//	@JoinColumn(name = "folder_id", referencedColumnName = "folder_id", nullable = false)
//	private Folder folder;
	
//	@ManyToMany
//	----------------- 
//	private Set<Tag> tags = new HashSet<Tag>();
	
	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "message")
	private Set<Attachment> attachments = new HashSet<Attachment>();
	
	public Message() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isUnread() {
		return unread;
	}

	public void setUnread(boolean unread) {
		this.unread = unread;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}