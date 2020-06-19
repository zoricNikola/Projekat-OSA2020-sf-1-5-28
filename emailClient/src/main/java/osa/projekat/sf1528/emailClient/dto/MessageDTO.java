package osa.projekat.sf1528.emailClient.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import osa.projekat.sf1528.emailClient.model.Message;

public class MessageDTO implements Serializable {

	private static final long serialVersionUID = -2640990194597541890L;
	
	private Long id;
	
	private String from;
	
	private String to;
	
	private String cc;
	
	private String bcc;
	
	private LocalDateTime dateTime;
	
	private String subject;
	
	private String content;
	
	private boolean unread;
	
	public MessageDTO() {}

	public MessageDTO(Long id, String from, String to, String cc, String bcc, LocalDateTime dateTime, String subject,
			String content, boolean unread) {
		super();
		this.id = id;
		this.from = from;
		this.to = to;
		this.cc = cc;
		this.bcc = bcc;
		this.dateTime = dateTime;
		this.subject = subject;
		this.content = content;
		this.unread = unread;
	}

	public MessageDTO(Message message) {
		this(message.getId(), message.getFrom(), message.getTo(), message.getCc(), message.getBcc(), message.getDateTime(), 
				message.getSubject(), message.getContent(), message.isUnread());
	}
	
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
