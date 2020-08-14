package osa.projekat.sf1528.emailClient.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import osa.projekat.sf1528.emailClient.dto.AttachmentDTO;
import osa.projekat.sf1528.emailClient.dto.MessageDTO;
import osa.projekat.sf1528.emailClient.dto.TagDTO;
import osa.projekat.sf1528.emailClient.dto.UserDTO;
import osa.projekat.sf1528.emailClient.model.Account;
import osa.projekat.sf1528.emailClient.model.Attachment;
import osa.projekat.sf1528.emailClient.model.Folder;
import osa.projekat.sf1528.emailClient.model.Message;
import osa.projekat.sf1528.emailClient.model.Rule;
import osa.projekat.sf1528.emailClient.model.Tag;
import osa.projekat.sf1528.emailClient.service.AccountService;
import osa.projekat.sf1528.emailClient.service.AttachmentService;
import osa.projekat.sf1528.emailClient.service.FolderService;
import osa.projekat.sf1528.emailClient.service.MessageService;
import osa.projekat.sf1528.emailClient.service.TagService;

@RestController
@RequestMapping(value = "api/messages")
public class MessageController {
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	FolderService folderService;
	
	@Autowired
	TagService tagService;
	
	@Autowired
	AttachmentService attachmentService;

	@GetMapping(value="/{id}")
	public ResponseEntity<MessageDTO> getMessage(@PathVariable("id") Long id) {
		Message message = messageService.findOne(id);
		if (message == null)
			return new ResponseEntity<MessageDTO>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<MessageDTO>(new MessageDTO(message), HttpStatus.OK);
	}
	
	@GetMapping(value="/{id}/tags")
	public ResponseEntity<List<TagDTO>> getMessageTags(@PathVariable("id") Long id) {
		Message message = messageService.findOne(id);
		if (message == null)
			return new ResponseEntity<List<TagDTO>>(HttpStatus.NOT_FOUND);
		
		List<Tag> tags = tagService.findByMessage(message);
		List<TagDTO> messageTags = new ArrayList<TagDTO>();
		for (Tag tag : tags) {
			messageTags.add(new TagDTO(tag));
		}
		
		return new ResponseEntity<List<TagDTO>>(messageTags, HttpStatus.OK);
	}
	
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
	
	@PostMapping(value = "/{accountId}", consumes = "application/json")
	public ResponseEntity<MessageDTO> saveMessage(@RequestBody MessageDTO messageDTO, @PathVariable("accountId") Long accountId) {
		Account account = accountService.findOne(accountId);
		if (account == null) {
			return new ResponseEntity<MessageDTO>(HttpStatus.BAD_REQUEST);
		}
		
		Message message = new Message();
		message.setFrom(messageDTO.getFrom());
		message.setTo(messageDTO.getTo());
		message.setCc(messageDTO.getCc());
		message.setBcc(messageDTO.getBcc());
		message.setDateTime(messageDTO.getDateTime());
		message.setSubject(messageDTO.getSubject());
		message.setContent(messageDTO.getContent());
		message.setUnread(messageDTO.isUnread());
		account.addMessage(message);
		
		for (TagDTO tagDTO : messageDTO.getTags()) {
			message.addTag(tagService.findOne(tagDTO.getId()));
		}
		
		for (Folder folder : account.getFolders()) {
			for (Rule rule : folder.getRules()) {
				Message m = rule.doRule(message);
				if (m != null && m != message)
					messageService.save(m);
			}
		}
		
		message = messageService.save(message);
		return new ResponseEntity<MessageDTO>(new MessageDTO(message), HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<MessageDTO> updateMessageTags(@RequestBody MessageDTO messageDTO, @PathVariable("id") Long id) {
		Message message = messageService.findOne(id);
		if (message == null)
			return new ResponseEntity<MessageDTO>(HttpStatus.NOT_FOUND);
		
		Set<Tag> currentTags = message.getTags();
		Set<Tag> updatedTags = new HashSet<Tag>();
		
		for (TagDTO tagDTO : messageDTO.getTags()) {
			updatedTags.add(tagService.findOne(tagDTO.getId()));
		}
		
		Set<Tag> toDeleteTags = new HashSet<Tag>();
		Set<Tag> toAddTags = new HashSet<Tag>();
		
		for (Tag cTag : currentTags) {
			boolean shouldDelete = true;
			for (Tag uTag : updatedTags) {
				if (cTag.getId() == uTag.getId())
					shouldDelete = false;;
			}
			
			if (shouldDelete)
				toDeleteTags.add(cTag);
		}
		
		for (Tag uTag : updatedTags) {
			boolean shouldAdd = true;
			for (Tag cTag : currentTags) {
				if (uTag.getId() == cTag.getId())
					shouldAdd = false;
			}
			
			if (shouldAdd)
				toAddTags.add(uTag);
		}
		
		for (Tag tag : toDeleteTags)
			message.removeTag(tag);
		
		for (Tag tag : toAddTags)
			message.addTag(tag);
		
		message = messageService.save(message);
		return new ResponseEntity<MessageDTO>(new MessageDTO(message), HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{id}/moveTo/{folderId}")
	public ResponseEntity<MessageDTO> moveMessage(@PathVariable("id") Long id, @PathVariable("folderId") Long folderId) {
		Message message = messageService.findOne(id);
		if (message == null)
			return new ResponseEntity<MessageDTO>(HttpStatus.NOT_FOUND);
		
		Folder folder = folderService.findOne(folderId);
		if (folder == null)
			return new ResponseEntity<MessageDTO>(HttpStatus.BAD_REQUEST);
		
		folder.addMessage(message);
		
		message = messageService.save(message);
		return new ResponseEntity<MessageDTO>(new MessageDTO(message), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteMessage(@PathVariable("id") Long id) {
		Message message = messageService.findOne(id);
		if (message == null)
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		
		message.getAccount().removeMessage(message);
		message.getFolder().removeMessage(message);
		
		for (Tag tag : message.getTags()) {
			message.removeTag(tag);
		}
		
		messageService.remove(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
