package osa.projekat.sf1528.emailClient.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import osa.projekat.sf1528.emailClient.model.Contact;
import osa.projekat.sf1528.emailClient.model.Message;
import osa.projekat.sf1528.emailClient.model.Tag;
import osa.projekat.sf1528.emailClient.service.ContactService;
import osa.projekat.sf1528.emailClient.util.Base64;
import osa.projekat.sf1528.emailClient.util.FilesUtil;

public class MessageDTO implements Serializable {

	private static final long serialVersionUID = -2640990194597541890L;
	
	private Long id;
	
	private String from;
	
	private String to;
	
	private String cc;
	
	private String bcc;
	
	private String dateTime;
	
	private String subject;
	
	private String content;
	
	private boolean unread;
	
	private Set<TagDTO> tags = new HashSet<TagDTO>();
	
	private String contactDisplayName;
	
	private String encodedContactPhoto;
	
	public MessageDTO() {}

	public MessageDTO(Long id, String from, String to, String cc, String bcc, String dateTime, String subject,
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

	public MessageDTO(Message message, ContactService contactService) {
		this(message.getId(), message.getFrom(), message.getTo(), message.getCc(), message.getBcc(), message.getDateTime().toString(), 
				message.getSubject(), message.getContent(), message.isUnread());
		
		for (Tag tag : message.getTags()) {
			this.tags.add(new TagDTO(tag));
		}
		
		List<Contact> contacts = contactService.findByUser(message.getAccount().getUser());
		for (Contact contact : contacts) {
			if (message.getFrom().contains(contact.getEmail()) || message.getTo().contains(contact.getEmail())) {
				this.contactDisplayName = contact.getDisplayName();
				if (contact.getPhotoPath() != null && !contact.getPhotoPath().isEmpty()) {
					byte[] photoData = FilesUtil.readBytes(contact.getPhotoPath());
					if (photoData != null)
						this.encodedContactPhoto = Base64.encodeToString(photoData);
				}
				break;
			}
		}
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

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
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

	public Set<TagDTO> getTags() {
		return tags;
	}

	public void setTags(Set<TagDTO> tags) {
		this.tags = tags;
	}

	public String getContactDisplayName() {
		return contactDisplayName;
	}

	public void setContactDisplayName(String contactDisplayName) {
		this.contactDisplayName = contactDisplayName;
	}

	public String getEncodedContactPhoto() {
		return encodedContactPhoto;
	}

	public void setEncodedContactPhoto(String encodedContactPhoto) {
		this.encodedContactPhoto = encodedContactPhoto;
	}
	

}
