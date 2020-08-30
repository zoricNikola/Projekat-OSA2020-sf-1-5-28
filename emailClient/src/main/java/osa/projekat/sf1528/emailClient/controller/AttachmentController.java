package osa.projekat.sf1528.emailClient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import osa.projekat.sf1528.emailClient.dto.AttachmentDTO;
import osa.projekat.sf1528.emailClient.model.Attachment;
import osa.projekat.sf1528.emailClient.model.Message;
import osa.projekat.sf1528.emailClient.model.User;
import osa.projekat.sf1528.emailClient.service.AttachmentService;
import osa.projekat.sf1528.emailClient.service.MessageService;
import osa.projekat.sf1528.emailClient.service.UserService;

@RestController
@RequestMapping(value = "api/attachments")
public class AttachmentController {

	@Autowired
	AttachmentService attachmentService;
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	UserService userService;
	
	private boolean userOwnsAttachment(User user, Attachment attachment) {
		return user.getId() == attachment.getMessage().getAccount().getUser().getId();
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<AttachmentDTO> getAttachment(@PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if(user == null) {
			return new ResponseEntity<AttachmentDTO>(HttpStatus.UNAUTHORIZED);
		}
		
		Attachment attachment = attachmentService.findOne(id);
		
		if (attachment == null) {
			return new ResponseEntity<AttachmentDTO>(HttpStatus.NOT_FOUND);
		}
		if(!userOwnsAttachment(user, attachment)) {
			return new ResponseEntity<AttachmentDTO>(HttpStatus.UNAUTHORIZED);
		}
		
		return new ResponseEntity<AttachmentDTO>(new AttachmentDTO(attachment), HttpStatus.OK);
	}
	
	@PostMapping(value = "/{messageId}", consumes = "application/json")
	public ResponseEntity<AttachmentDTO> saveAttachment(@RequestBody AttachmentDTO attachmentDTO, @PathVariable("messageId") Long messageId) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if(user == null) {
			return new ResponseEntity<AttachmentDTO>(HttpStatus.UNAUTHORIZED);
		}
		
		Message message = messageService.findOne(messageId);
		
		if (message == null) {
			return new ResponseEntity<AttachmentDTO>(HttpStatus.BAD_REQUEST);
		}
		if(user.getId() != message.getAccount().getUser().getId()) {
			return new ResponseEntity<AttachmentDTO>(HttpStatus.UNAUTHORIZED);
		}
		
		Attachment attachment = new Attachment();
		attachment.setData(attachmentDTO.getData());
		attachment.setMimeType(attachmentDTO.getMimeType());
		attachment.setName(attachmentDTO.getName());
		message.addAttachment(attachment);
		
		attachment = attachmentService.save(attachment);
		return new ResponseEntity<AttachmentDTO>(new AttachmentDTO(attachment), HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteAttachment(@PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if(user == null) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		
		Attachment attachment = attachmentService.findOne(id);
		
		if (attachment == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		if(!userOwnsAttachment(user, attachment)) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		
		attachment.getMessage().removeAttachment(attachment);
		attachmentService.remove(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
