package osa.projekat.sf1528.emailClient.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "attachments")
public class Attachment implements Serializable {

	private static final long serialVersionUID = -6184772261364724856L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "attachment_id", unique = true, nullable = false)
	private Long id;
	
	@Lob
	@Column(name = "data", unique = false, nullable = false)
	private String data;
	
	@Column(name = "mime_type", unique = false, nullable = false)
	private String mimeType;
	
	@Column(name = "name", unique = false, nullable = false)
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

	public String getData() {
		return data;
	}

	public void setData(String data) {
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
