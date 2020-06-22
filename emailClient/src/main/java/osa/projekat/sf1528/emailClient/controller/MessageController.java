package osa.projekat.sf1528.emailClient.controller;

import java.util.ArrayList;
import java.util.List;

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
import osa.projekat.sf1528.emailClient.dto.MessageDTO;
import osa.projekat.sf1528.emailClient.dto.TagDTO;
import osa.projekat.sf1528.emailClient.model.Attachment;
import osa.projekat.sf1528.emailClient.model.Message;
import osa.projekat.sf1528.emailClient.model.Tag;
import osa.projekat.sf1528.emailClient.service.AccountServiceInterface;
import osa.projekat.sf1528.emailClient.service.AttachmentServiceInterface;
import osa.projekat.sf1528.emailClient.service.FolderServiceInterface;
import osa.projekat.sf1528.emailClient.service.MessageServiceInterface;
import osa.projekat.sf1528.emailClient.service.TagServiceInterface;

@RestController
@RequestMapping(value = "api/messages")
public class MessageController {
	
	@Autowired
	MessageServiceInterface messageService;
	
	@Autowired
	AccountServiceInterface accountService;
	
	@Autowired
	FolderServiceInterface folderService;
	
	@Autowired
	TagServiceInterface tagService;
	
	@Autowired
	AttachmentServiceInterface attachmentService;

	@GetMapping(value="/{id}")
	public ResponseEntity<MessageDTO> getMessage(@PathVariable("id") Long id) {
		Message message = messageService.findOne(id);
		if (message == null)
			return new ResponseEntity<MessageDTO>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<MessageDTO>(new MessageDTO(message), HttpStatus.OK);
	}
	
	//@GetMapping(value="/{id}/tags")
	//public ResponseEntity<List<TagDTO>> getMessageTags(@PathVariable("id") Long id) {
		//Message message = messageService.findOne(id);
		//if (message == null)
		// new ResponseEntity<List<TagDTO>>(HttpStatus.NOT_FOUND);
		
		//List<Tag> tags = tagService.findByMessage(message);
		//List<TagDTO> messageTags = new ArrayList<TagDTO>();
		//for (Tag tag : tags) {
		//	messageTags.add(new TagDTO(tag));
		//}
		
		//return new ResponseEntity<List<TagDTO>>(messageTags, HttpStatus.OK);
	//}
	
	@GetMapping(value="/{id}/attachments")
	public ResponseEntity<List<AttachmentDTO>> getMessageAttachments(@PathVariable("id") Long id) {
		Message message = messageService.findOne(id);
		if (message == null)
			return new ResponseEntity<List<AttachmentDTO>>(HttpStatus.NOT_FOUND);
		
		List<Attachment> attachments = attachmentService.findByMessage(message);
		List<AttachmentDTO> messageAttachments = new ArrayList<AttachmentDTO>();
		for (Attachment attachment : attachments) {
			messageAttachments.add(new AttachmentDTO(attachment));
		}
		
		return new ResponseEntity<List<AttachmentDTO>>(messageAttachments, HttpStatus.OK);
	}
	
	@PostMapping(consumes = "appliaction/json")
	public ResponseEntity<MessageDTO> saveMessage(@RequestBody MessageDTO messageDTO) {
		Message message = new Message();
		message.setFrom(messageDTO.getFrom());
		message.setTo(messageDTO.getTo());
		message.setCc(messageDTO.getCc());
		message.setBcc(messageDTO.getBcc());
		message.setDateTime(messageDTO.getDateTime());
		message.setSubject(messageDTO.getSubject());
		message.setContent(messageDTO.getContent());
		message.setUnread(messageDTO.isUnread());
		message.setAccount(accountService.findOne(messageDTO.getAccount().getId()));
		message.setFolder(folderService.findOne(messageDTO.getFolder().getId()));
		
		for (TagDTO tagDTO : messageDTO.getTags()) {
			message.getTags().add(tagService.findOne(tagDTO.getId()));
		}
		
		message = messageService.save(message);
		return new ResponseEntity<MessageDTO>(new MessageDTO(message), HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteMessage(@PathVariable("id") Long id) {
		Message message = messageService.findOne(id);
		if (message == null)
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		
		messageService.remove(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
