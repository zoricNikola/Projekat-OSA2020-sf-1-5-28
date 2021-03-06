package osa.projekat.sf1528.emailClient.dto;

import java.io.Serializable;

import osa.projekat.sf1528.emailClient.model.Attachment;
import osa.projekat.sf1528.emailClient.util.Base64;
import osa.projekat.sf1528.emailClient.util.FilesUtil;

public class AttachmentDTO implements Serializable {

	private static final long serialVersionUID = 5112510341538929038L;
	
	private Long id;
	
	private String data;
	
	private String mimeType;
	
	private String name;
	
	public AttachmentDTO() {}

	public AttachmentDTO(Long id, String data, String mimeType, String name) {
		super();
		this.id = id;
		this.data = data;
		this.mimeType = mimeType;
		this.name = name;
	}
	
	public AttachmentDTO(Attachment attachment) {
		this(attachment.getId(), attachment.getDataPath() ,attachment.getMimeType(), attachment.getName());
		
		if (attachment.getDataPath() != null && !attachment.getDataPath().isEmpty()) {
			byte[] attachmentData = FilesUtil.readBytes(attachment.getDataPath());
			if (attachmentData != null)
				this.data = Base64.encodeToString(attachmentData);
		}
	}

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
