package osa.projekat.sf1528.emailClient.dto;

import java.io.Serializable;
import java.util.Base64;

import osa.projekat.sf1528.emailClient.model.Attachment;

public class AttachmentDTO implements Serializable {

	private static final long serialVersionUID = 5112510341538929038L;
	
	private Long id;
	
	private Base64 data;
	
	private String mimeType;
	
	private String name;
	
	private MessageDTO message;
	
	public AttachmentDTO() {}

	public AttachmentDTO(Long id, Base64 data, String mimeType, String name, MessageDTO message) {
		super();
		this.id = id;
		this.data = data;
		this.mimeType = mimeType;
		this.name = name;
		this.message = message;
	}
	
	public AttachmentDTO(Attachment attachment) {
		this(attachment.getId(), attachment.getData() ,attachment.getMimeType(), attachment.getName(), new MessageDTO(attachment.getMessage()));
	}

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public MessageDTO getMessage() {
		return message;
	}

	public void setMessage(MessageDTO message) {
		this.message = message;
	}
	
	
}
