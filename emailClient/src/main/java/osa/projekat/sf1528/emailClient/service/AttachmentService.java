package osa.projekat.sf1528.emailClient.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import osa.projekat.sf1528.emailClient.model.Attachment;
import osa.projekat.sf1528.emailClient.model.Message;
import osa.projekat.sf1528.emailClient.repository.AttachmentRepository;

@Service
public class AttachmentService implements AttachmentServiceInterface {

	@Autowired
	AttachmentRepository attachmentRepository;
	
	@Override
	public Attachment findOne(Long attachmentId) {
		return attachmentRepository.getOne(attachmentId);
	}

	@Override
	public List<Attachment> findByMessage(Message message) {
		return attachmentRepository.findByMessage(message);
	}

	@Override
	public Attachment save(Attachment attachment) {
		return attachmentRepository.save(attachment);
	}

	@Override
	public void remove(Long attachmentId) {
		attachmentRepository.deleteById(attachmentId);
	}

}
