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

import osa.projekat.sf1528.emailClient.dto.AccountDTO;
import osa.projekat.sf1528.emailClient.dto.MessageDTO;
import osa.projekat.sf1528.emailClient.dto.TagDTO;
import osa.projekat.sf1528.emailClient.model.Account;
import osa.projekat.sf1528.emailClient.model.Message;
import osa.projekat.sf1528.emailClient.model.Tag;
import osa.projekat.sf1528.emailClient.model.User;
import osa.projekat.sf1528.emailClient.service.AccountService;
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
	
	@GetMapping(value="/{id}")
	public ResponseEntity<TagDTO> getTag(@PathVariable("id") Long id) {
		Tag tag = tagService.findOne(id);
		if (tag == null)
			return new ResponseEntity<TagDTO>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<TagDTO>(new TagDTO(tag), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}/messages", consumes = "application/json")
	public ResponseEntity<List<MessageDTO>> getTagMessagesByAccount(@RequestBody AccountDTO accountDTO, @PathVariable("id") Long id) {
		Tag tag = tagService.findOne(id);
		if (tag == null)
			return new ResponseEntity<List<MessageDTO>>(HttpStatus.NOT_FOUND);
		
		Account account = accountService.findOne(accountDTO.getId());
		if (account == null)
			return new ResponseEntity<List<MessageDTO>>(HttpStatus.BAD_REQUEST);
		
		List<Message> messages = messageService.findByAccountAndTag(account, tag);
		List<MessageDTO> tagMessages = new ArrayList<MessageDTO>();
		
		for (Message message : messages) {
			tagMessages.add(new MessageDTO(message));
		}
		
		return new ResponseEntity<List<MessageDTO>>(tagMessages, HttpStatus.OK);
	}
	
	@PostMapping(value = "/{userId}", consumes = "application/json")
	public ResponseEntity<TagDTO> saveTag(@RequestBody TagDTO tagDTO, @PathVariable("userId") Long userId) {
		User user = userService.findOne(userId);
		if(user == null) {
			return new ResponseEntity<TagDTO>(HttpStatus.BAD_REQUEST);
		}
		
		Tag tag = new Tag();
		tag.setName(tagDTO.getName());
		user.addTag(tag);

		tag = tagService.save(tag);
		return new ResponseEntity<TagDTO>(new TagDTO(tag), HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteTag(@PathVariable("id") Long id) {
		Tag tag = tagService.findOne(id);
		if (tag == null)
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		
		tag.getUser().removeTag(tag);
		
		for (Message message : tag.getMessages()) {
			message.removeTag(tag);
		}
		
		tagService.remove(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
