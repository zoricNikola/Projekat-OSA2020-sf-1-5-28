package osa.projekat.sf1528.emailClient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import osa.projekat.sf1528.emailClient.model.Attachment;
import osa.projekat.sf1528.emailClient.model.Message;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

	List<Attachment> findByMessage(Message message);
	
}
