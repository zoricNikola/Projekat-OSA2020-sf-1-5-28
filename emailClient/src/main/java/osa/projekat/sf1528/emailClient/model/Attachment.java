package osa.projekat.sf1528.emailClient.model;

import java.io.Serializable;
import java.util.Base64;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "attachments")
public class Attachment implements Serializable {

	private static final long serialVersionUID = 3L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "attachment_id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "data", unique = true, nullable = false)
	private Base64 data;
	
	@Column(name = "mime_type", unique = true, nullable = false)
	private String mimeType;
	
	@Column(name = "name", unique = true, nullable = false)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "message_id", referencedColumnName = "message_id", nullable = false)
	private Message message;
	
	public Attachment() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Base64 getData() {
		return data;
	}

	public void setData(Base64 data) {
		this.data = data;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}