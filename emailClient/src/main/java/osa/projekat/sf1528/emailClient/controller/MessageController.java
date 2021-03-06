package osa.projekat.sf1528.emailClient.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import osa.projekat.sf1528.emailClient.dto.AttachmentDTO;
import osa.projekat.sf1528.emailClient.dto.FilterDTO;
import osa.projekat.sf1528.emailClient.dto.MessageDTO;
import osa.projekat.sf1528.emailClient.dto.MessageDataDTO;
import osa.projekat.sf1528.emailClient.dto.TagDTO;
import osa.projekat.sf1528.emailClient.mail.MailUtil;
import osa.projekat.sf1528.emailClient.model.Account;
import osa.projekat.sf1528.emailClient.model.Attachment;
import osa.projekat.sf1528.emailClient.model.Folder;
import osa.projekat.sf1528.emailClient.model.Message;
import osa.projekat.sf1528.emailClient.model.Rule;
import osa.projekat.sf1528.emailClient.model.Tag;
import osa.projekat.sf1528.emailClient.model.User;
import osa.projekat.sf1528.emailClient.service.AccountService;
import osa.projekat.sf1528.emailClient.service.AttachmentService;
import osa.projekat.sf1528.emailClient.service.ContactService;
import osa.projekat.sf1528.emailClient.service.FolderService;
import osa.projekat.sf1528.emailClient.service.MessageService;
import osa.projekat.sf1528.emailClient.service.TagService;
import osa.projekat.sf1528.emailClient.service.UserService;
import osa.projekat.sf1528.emailClient.util.Base64;
import osa.projekat.sf1528.emailClient.util.FilesUtil;

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
	
	@Autowired
	UserService userService;
	
	@Autowired
	ContactService contactService;
	
	private boolean userOwnsMessage(User user, Message message) {
		return user.getId() == message.getAccount().getUser().getId();
	}
	private boolean userOwnsAccount(User user, Account account) {
		return user.getId() == account.getUser().getId();
	}

	@GetMapping(value="/{id}")
	public ResponseEntity<MessageDTO> getMessage(@PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if(user == null) {
			return new ResponseEntity<MessageDTO>(HttpStatus.UNAUTHORIZED);
		}
		
		Message message = messageService.findOne(id);
		
		if (message == null) {
			return new ResponseEntity<MessageDTO>(HttpStatus.NOT_FOUND);
		}
		if(!userOwnsMessage(user, message)) {
			return new ResponseEntity<MessageDTO>(HttpStatus.UNAUTHORIZED);
		}
		
		return new ResponseEntity<MessageDTO>(new MessageDTO(message, contactService), HttpStatus.OK);
	}
	
	@GetMapping(value="/{id}/tags")
	public ResponseEntity<List<TagDTO>> getMessageTags(@PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if(user == null) {
			return new ResponseEntity<List<TagDTO>>(HttpStatus.UNAUTHORIZED);
		}
		
		Message message = messageService.findOne(id);
		
		if (message == null) {
			return new ResponseEntity<List<TagDTO>>(HttpStatus.NOT_FOUND);
		}
		if(!userOwnsMessage(user, message)) {
			return new ResponseEntity<List<TagDTO>>(HttpStatus.UNAUTHORIZED);
		}
		
		List<Tag> tags = tagService.findByMessage(message);
		List<TagDTO> messageTags = new ArrayList<TagDTO>();
		for (Tag tag : tags) {
			messageTags.add(new TagDTO(tag));
		}
		
		return new ResponseEntity<List<TagDTO>>(messageTags, HttpStatus.OK);
	}
	
	@GetMapping(value="/{id}/attachments")
	public ResponseEntity<List<AttachmentDTO>> getMessageAttachments(@PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if(user == null) {
			return new ResponseEntity<List<AttachmentDTO>>(HttpStatus.UNAUTHORIZED);
		}
		
		Message message = messageService.findOne(id);
		
		if (message == null) {
			return new ResponseEntity<List<AttachmentDTO>>(HttpStatus.NOT_FOUND);
		}
		if(!userOwnsMessage(user, message)) {
			return new ResponseEntity<List<AttachmentDTO>>(HttpStatus.UNAUTHORIZED);
		}
		List<Attachment> attachments = attachmentService.findByMessage(message);
		List<AttachmentDTO> messageAttachments = new ArrayList<AttachmentDTO>();
		for (Attachment attachment : attachments) {
			AttachmentDTO attachmentDTO = new AttachmentDTO(attachment);
			attachmentDTO.setData(null);
			messageAttachments.add(attachmentDTO);
		}
		
		return new ResponseEntity<List<AttachmentDTO>>(messageAttachments, HttpStatus.OK);
	}
	
	@PostMapping(value = "/{accountId}", consumes = "application/json")
	public ResponseEntity<MessageDTO> saveMessage(@RequestBody MessageDataDTO messageData, @PathVariable("accountId") Long accountId) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if(user == null) {
			return new ResponseEntity<MessageDTO>(HttpStatus.UNAUTHORIZED);
		}
		
		Account account = accountService.findOne(accountId);
		
		if (account == null) {
			return new ResponseEntity<MessageDTO>(HttpStatus.BAD_REQUEST);
		}
		if(user.getId() != account.getUser().getId()) {
			return new ResponseEntity<MessageDTO>(HttpStatus.UNAUTHORIZED);
		}		
		
		Message message = new Message();
		message.setFrom(messageData.getMessage().getFrom());
		message.setTo(messageData.getMessage().getTo());
		message.setCc(messageData.getMessage().getCc());
		message.setBcc(messageData.getMessage().getBcc());
		message.setDateTime(LocalDateTime.now());
		message.setSubject(messageData.getMessage().getSubject());
		message.setContent(messageData.getMessage().getContent());
		message.setUnread(false);
		account.addMessage(message);
		storeMessageInAccountsSentFolder(message, account);
		account = accountService.save(account);
		
		for (AttachmentDTO attachmentDTO : messageData.getAttachments()) {
			Attachment attachment = new Attachment();
			attachment.setName(attachmentDTO.getName());
			attachment.setMimeType(attachmentDTO.getMimeType());
			attachment.setDataPath("");
			message.addAttachment(attachment);
			
			if (attachmentDTO.getData() != null && !attachmentDTO.getData().isEmpty()) {
				byte[] attachmentData = Base64.decode(attachmentDTO.getData());
				String path = String.format("./data/attachments/%d", new Date().hashCode());
				
				if (FilesUtil.saveBytes(attachmentData, path)) {
					attachment.setDataPath(path);
				}
			}
			
		}
		
		
//		for (TagDTO tagDTO : messageData.getMessage().getTags()) {
//			message.addTag(tagService.findOne(tagDTO.getId()));
//		}
		
//		for (Folder folder : account.getFolders()) {
//			for (Rule rule : folder.getRules()) {
//				Message m = rule.doRule(message);
//				if (m != null && m != message)
//					messageService.save(m);
//			}
//		}
		
		message = messageService.save(message);
		boolean succesful = MailUtil.sendMessage(message);
		
		if (succesful) {
			message = messageService.save(message);
			return new ResponseEntity<MessageDTO>(new MessageDTO(message, contactService), HttpStatus.CREATED);
		}
		return new ResponseEntity<MessageDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private void storeMessageInAccountsSentFolder(Message message, Account account) {
		for (Folder folder : account.getFolders()) {
			if (folder.getName().equalsIgnoreCase("sent")) {
				folder.addMessage(message);
				break;
			}
		}
	}
	
//	@PostMapping(value = "/{id}", consumes = "application/json")
//	public ResponseEntity<Boolean> sendMessage(@PathVariable("id") Long id) {
//		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
//		
//		if(user == null) {
//			return new ResponseEntity<Boolean>(false, HttpStatus.UNAUTHORIZED);
//		}
//		
//		Message message = messageService.findOne(id);
//		
//		if (message == null) {
//			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
//		}
//		if(!userOwnsMessage(user, message)) {
//			return new ResponseEntity<Boolean>(false, HttpStatus.UNAUTHORIZED);
//		}
//		
//		boolean succesful = MailUtil.sendMessage(message);
//		
//		if (succesful)
//			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
//		else
//			return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
//		
//	}
	
	@PostMapping(value="/filter/{accountId}")
	public ResponseEntity<List<MessageDTO>> filterAccountMessages(@PathVariable("accountId") Long accountId, @RequestBody FilterDTO filter) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<List<MessageDTO>>(HttpStatus.UNAUTHORIZED);
		
		Account account = accountService.findOne(accountId);
		
		if (account == null)
			return new ResponseEntity<List<MessageDTO>>(HttpStatus.NOT_FOUND);

		if (!userOwnsAccount(user, account))
			return new ResponseEntity<List<MessageDTO>>(HttpStatus.UNAUTHORIZED);
		
		List<Message> messages = messageService.findByAccount(account);
		
		Set<Message> filteredSet = new HashSet<Message>();
		
		if (filter.getFilteringTags().size() > 0) {
			for (Message message : messages) {
				tagLoop : for (Tag tag : message.getTags()) {
					for (TagDTO tagDTO : filter.getFilteringTags()) {
						if (tag.getId() == tagDTO.getId()) {
							filteredSet.add(message);
							break tagLoop;
						}
					}
				}
			}
		}
		
		if (!filter.getSearchText().isEmpty()) {
			for (Message message : messages) {
				if (message.getFrom().toLowerCase().contains(filter.getSearchText().toLowerCase())) {
					filteredSet.add(message);
					continue;
				}
				if (message.getTo().toLowerCase().contains(filter.getSearchText().toLowerCase())) {
					filteredSet.add(message);
					continue;
				}
				if (message.getCc() != null && message.getCc().toLowerCase().contains(filter.getSearchText().toLowerCase())) {
					filteredSet.add(message);
					continue;
				}
				if (message.getBcc() != null && message.getBcc().toLowerCase().contains(filter.getSearchText().toLowerCase())) {
					filteredSet.add(message);
					continue;
				}
				if (message.getSubject().toLowerCase().contains(filter.getSearchText().toLowerCase())) {
					filteredSet.add(message);
					continue;
				}
				if (message.getContent().toLowerCase().contains(filter.getSearchText().toLowerCase())) {
					filteredSet.add(message);
					continue;
				}
			}
		}
		
		List<Message> filteredMessages = new ArrayList<Message>();
		filteredMessages.addAll(filteredSet);
		
		Collections.sort(filteredMessages, new Comparator<Message>() {
			public int compare(Message m1, Message m2) {
				return m1.getDateTime().isAfter(m2.getDateTime()) ? -1 : 1;
			}
		});
		List<MessageDTO> accountMessages = new ArrayList<MessageDTO>();
		for (int i = 0; i < 50; i ++) {
			if ( i == filteredMessages.size())
				break;
			accountMessages.add(new MessageDTO(filteredMessages.get(i), contactService));
		}
		
		return new ResponseEntity<List<MessageDTO>>(accountMessages, HttpStatus.OK);
	}
	
	@PutMapping(value = "/{id}/markAsRead")
	public ResponseEntity<Boolean> markMessageAsRead(@PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if(user == null) {
			return new ResponseEntity<Boolean>(false, HttpStatus.UNAUTHORIZED);
		}
		
		Message message = messageService.findOne(id);
		
		if (message == null) {
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		}
		if(!userOwnsMessage(user, message)) {
			return new ResponseEntity<Boolean>(false, HttpStatus.UNAUTHORIZED);
		}
		
		message.setUnread(false);
		
		message = messageService.save(message);
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<MessageDTO> updateMessageTags(@RequestBody MessageDTO messageDTO, @PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if(user == null) {
			return new ResponseEntity<MessageDTO>(HttpStatus.UNAUTHORIZED);
		}
		
		Message message = messageService.findOne(id);
		
		if (message == null) {
			return new ResponseEntity<MessageDTO>(HttpStatus.BAD_REQUEST);
		}
		if(!userOwnsMessage(user, message)) {
			return new ResponseEntity<MessageDTO>(HttpStatus.UNAUTHORIZED);
		}
		
		Set<Tag> currentTags = message.getTags();
		Set<Tag> updatedTags = new HashSet<Tag>();
		
		for (TagDTO tagDTO : messageDTO.getTags()) {
			Tag tag;
			if (tagDTO.getId() == null) {
				tag = new Tag();
				tag.setName(tagDTO.getName());
				tag.setColor(tagDTO.getColor());
				user.addTag(tag);
				tag = tagService.save(tag);
			}
			else {
				tag = tagService.findOne(tagDTO.getId());
//				tag.setColor(tagDTO.getColor());
//				tag = tagService.save(tag);
			}
			if (tag != null)
				updatedTags.add(tag);
		}
		
		Set<Tag> toDeleteTags = new HashSet<Tag>();
		Set<Tag> toAddTags = new HashSet<Tag>();
		
		for (Tag cTag : currentTags) {
			boolean shouldDelete = true;
			for (Tag uTag : updatedTags) {
				if (cTag.getId() == uTag.getId()) {
					shouldDelete = false;
					break;
				}
			}
			
			if (shouldDelete)
				toDeleteTags.add(cTag);
		}
		
		for (Tag uTag : updatedTags) {
			boolean shouldAdd = true;
			for (Tag cTag : currentTags) {
				if (uTag.getId() == cTag.getId()) {
					shouldAdd = false;
					break;
				}
			}
			
			if (shouldAdd)
				toAddTags.add(uTag);
		}
		
		for (Tag tag : toDeleteTags)
			message.removeTag(tag);
		
		for (Tag tag : toAddTags)
			message.addTag(tag);
		
		message = messageService.save(message);
		return new ResponseEntity<MessageDTO>(new MessageDTO(message, contactService), HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{id}/moveTo/{folderId}")
	public ResponseEntity<MessageDTO> moveMessage(@PathVariable("id") Long id, @PathVariable("folderId") Long folderId) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if(user == null) {
			return new ResponseEntity<MessageDTO>(HttpStatus.UNAUTHORIZED);
		}
		
		Message message = messageService.findOne(id);
		
		if (message == null) {
			return new ResponseEntity<MessageDTO>(HttpStatus.BAD_REQUEST);
		}
		if(!userOwnsMessage(user, message)) {
			return new ResponseEntity<MessageDTO>(HttpStatus.UNAUTHORIZED);
		}
		
		Folder folder = folderService.findOne(folderId);
		
		if (folder == null) {
			return new ResponseEntity<MessageDTO>(HttpStatus.BAD_REQUEST);
		}
		if(user.getId() != folder.getAccount().getUser().getId()) {
			return new ResponseEntity<MessageDTO>(HttpStatus.UNAUTHORIZED);
		}
		
		folder.addMessage(message);
		
		message = messageService.save(message);
		return new ResponseEntity<MessageDTO>(new MessageDTO(message, contactService), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteMessage(@PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if(user == null) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		
		Message message = messageService.findOne(id);
		
		if (message == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		if(!userOwnsMessage(user, message)) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		
		message.getAccount().removeMessage(message);
		message.getFolder().removeMessage(message);
		
		for (Tag tag : message.getTags()) {
			message.removeTag(tag);
		}
		
		messageService.remove(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
