package osa.projekat.sf1528.emailClient.dto;

import java.io.Serializable;
import java.util.List;

public class MessageDataDTO implements Serializable {

	private static final long serialVersionUID = -2159863338969384284L;
	
	private MessageDTO message;
	
	private List<AttachmentDTO> attachments;
	
	public MessageDataDTO() {}

	public MessageDTO getMessage() {
		return message;
	}

	public void setMessage(MessageDTO message) {
		this.message = message;
	}

	public List<AttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentDTO> attachments) {
		this.attachments = attachments;
	}
}
