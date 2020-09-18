package osa.projekat.sf1528.emailClient.controller;

import java.util.ArrayList;
import java.util.List;

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

import osa.projekat.sf1528.emailClient.dto.MessageDTO;
import osa.projekat.sf1528.emailClient.dto.TagDTO;
import osa.projekat.sf1528.emailClient.model.Account;
import osa.projekat.sf1528.emailClient.model.Message;
import osa.projekat.sf1528.emailClient.model.Tag;
import osa.projekat.sf1528.emailClient.model.User;
import osa.projekat.sf1528.emailClient.service.AccountService;
import osa.projekat.sf1528.emailClient.service.ContactService;
import osa.projekat.sf1528.emailClient.service.MessageService;
import osa.projekat.sf1528.emailClient.service.TagService;
import osa.projekat.sf1528.emailClient.service.UserService;

@RestController
@RequestMapping(value = "api/tags")
public class TagController {
	
	@Autowired
	TagService tagService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	ContactService contactService;
	
	private boolean userOwnsTag(User user, Tag tag) {
		return user.getId() == tag.getUser().getId();
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<TagDTO> getTag(@PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<TagDTO>(HttpStatus.UNAUTHORIZED);
		
		Tag tag = tagService.findOne(id);
		
		if (tag == null)
			return new ResponseEntity<TagDTO>(HttpStatus.NOT_FOUND);
		
		if (!userOwnsTag(user, tag))
			return new ResponseEntity<TagDTO>(HttpStatus.UNAUTHORIZED);
		
		return new ResponseEntity<TagDTO>(new TagDTO(tag), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}/{accountId}/messages", consumes = "application/json")
	public ResponseEntity<List<MessageDTO>> getTagMessagesByAccount(@PathVariable("accountId") Long accountId, @PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<List<MessageDTO>>(HttpStatus.UNAUTHORIZED);
		
		Tag tag = tagService.findOne(id);
		
		if (tag == null)
			return new ResponseEntity<List<MessageDTO>>(HttpStatus.NOT_FOUND);
		
		if (!userOwnsTag(user, tag))
			return new ResponseEntity<List<MessageDTO>>(HttpStatus.UNAUTHORIZED);
		
		Account account = accountService.findOne(accountId);
		
		if (account == null)
			return new ResponseEntity<List<MessageDTO>>(HttpStatus.NOT_FOUND);
		
		if (account.getUser().getId() != user.getId())
			return new ResponseEntity<List<MessageDTO>>(HttpStatus.UNAUTHORIZED);
		
		List<Message> messages = messageService.findByAccountAndTag(account, tag);
		List<MessageDTO> tagMessages = new ArrayList<MessageDTO>();
		
		for (Message message : messages) {
			tagMessages.add(new MessageDTO(message, contactService));
		}
		
		return new ResponseEntity<List<MessageDTO>>(tagMessages, HttpStatus.OK);
	}
	
	@PostMapping(value = "/{userId}", consumes = "application/json")
	public ResponseEntity<TagDTO> saveTag(@RequestBody TagDTO tagDTO, @PathVariable("userId") Long userId) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<TagDTO>(HttpStatus.UNAUTHORIZED);
		
		if (user.getId() != userId)
			return new ResponseEntity<TagDTO>(HttpStatus.UNAUTHORIZED);
		
		Tag tag = new Tag();
		tag.setName(tagDTO.getName());
		tag.setColor(tagDTO.getColor());
		user.addTag(tag);

		tag = tagService.save(tag);
		return new ResponseEntity<TagDTO>(new TagDTO(tag), HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteTag(@PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		
		Tag tag = tagService.findOne(id);
		
		if (tag == null)
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		
		if (!userOwnsTag(user, tag))
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		
		tag.getUser().removeTag(tag);
		
		for (Message message : tag.getMessages()) {
			message.removeTag(tag);
		}
		
		tagService.remove(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
