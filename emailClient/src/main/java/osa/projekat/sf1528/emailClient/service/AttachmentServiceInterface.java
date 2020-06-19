package osa.projekat.sf1528.emailClient.service;

import java.util.List;

import osa.projekat.sf1528.emailClient.model.Attachment;
import osa.projekat.sf1528.emailClient.model.Message;

public interface AttachmentServiceInterface {
	
	Attachment findOne(Long attachmentId);
	
	List<Attachment> findByMessage(Message message);
	
	Attachment save(Attachment attachment);
	
	void remove(Long attachmentId);

}
