package osa.projekat.sf1528.emailClient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import osa.projekat.sf1528.emailClient.dto.AttachmentDTO;
import osa.projekat.sf1528.emailClient.model.Attachment;
import osa.projekat.sf1528.emailClient.service.AttachmentServiceInterface;
import osa.projekat.sf1528.emailClient.service.MessageServiceInterface;

@RestController
@RequestMapping(value = "api/attachments")
public class AttachmentController {

	@Autowired
	AttachmentServiceInterface attachmentService;
	
	@Autowired
	MessageServiceInterface messageService;
	
	@GetMapping(value="/{id}")
	public ResponseEntity<AttachmentDTO> getAttachment(@PathVariable("id") Long id) {
		Attachment attachment = attachmentService.findOne(id);
		if (attachment == null)
			return new ResponseEntity<AttachmentDTO>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<AttachmentDTO>(new AttachmentDTO(attachment), HttpStatus.OK);
	}
	
	@PostMapping(consumes = "application/json")
	public ResponseEntity<AttachmentDTO> saveAttachment(@RequestBody AttachmentDTO attachmentDTO) {
		Attachment attachment = new Attachment();
		attachment.setData(attachmentDTO.getData());
		attachment.setMimeType(attachmentDTO.getMimeType());
		attachment.setName(attachmentDTO.getName());
		messageService.findOne(attachmentDTO.getMessage().getId()).addAttachment(attachment);
		
		attachment = attachmentService.save(attachment);
		return new ResponseEntity<AttachmentDTO>(new AttachmentDTO(attachment), HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteAttachment(@PathVariable("id") Long id) {
		Attachment attachment = attachmentService.findOne(id);
		if (attachment == null)
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		
		attachment.getMessage().removeAttachment(attachment);
		attachmentService.remove(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
