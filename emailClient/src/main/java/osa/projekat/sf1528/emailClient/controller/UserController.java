package osa.projekat.sf1528.emailClient.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import osa.projekat.sf1528.emailClient.dto.AccountDTO;
import osa.projekat.sf1528.emailClient.dto.ContactDTO;
import osa.projekat.sf1528.emailClient.dto.TagDTO;
import osa.projekat.sf1528.emailClient.dto.UserDTO;
import osa.projekat.sf1528.emailClient.model.Account;
import osa.projekat.sf1528.emailClient.model.Contact;
import osa.projekat.sf1528.emailClient.model.Tag;
import osa.projekat.sf1528.emailClient.model.User;
import osa.projekat.sf1528.emailClient.service.AccountService;
import osa.projekat.sf1528.emailClient.service.ContactService;
import osa.projekat.sf1528.emailClient.service.TagService;
import osa.projekat.sf1528.emailClient.service.UserService;

@RestController
@RequestMapping(value = "api/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	TagService tagService;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	ContactService contactService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<UserDTO> getUser(@PathVariable("id") Long id){
		User user = userService.findOne(id);		
		if(user == null) {
			return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
		}
		user.setPassword(null);
		
		return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}/accounts")
	public ResponseEntity<List<AccountDTO>> getUserAccounts(@PathVariable("id") Long id){
		User user = userService.findOne(id);
		if(user == null) {
			return new ResponseEntity<List<AccountDTO>>(HttpStatus.NOT_FOUND);
		}
		
		List<Account> accounts = accountService.findByUser(user);
		List<AccountDTO> userAccounts = new ArrayList<AccountDTO>();
		for(Account account : accounts) {
			userAccounts.add(new AccountDTO(account));
		}
		
		return new ResponseEntity<List<AccountDTO>>(userAccounts, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}/contacts")
	public ResponseEntity<List<ContactDTO>> getUserContacts(@PathVariable("id") Long id){
		User user = userService.findOne(id);
		if(user == null) {
			return new ResponseEntity<List<ContactDTO>>(HttpStatus.NOT_FOUND);
		}
		
		List<Contact> contacts = contactService.findByUser(user);
		List<ContactDTO> userContacts = new ArrayList<ContactDTO>();
		for(Contact contact : contacts) {
			userContacts.add(new ContactDTO(contact));
		}
		
		return new ResponseEntity<List<ContactDTO>>(userContacts, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}/tags")
	public ResponseEntity<List<TagDTO>> getUserTags(@PathVariable("id") Long id){
		User user = userService.findOne(id);
		if(user == null) {
			return new ResponseEntity<List<TagDTO>>(HttpStatus.NOT_FOUND);
		}
		
		List<Tag> tags = tagService.findByUser(user);
		List<TagDTO> userTags = new ArrayList<TagDTO>();
		for(Tag tag : tags) {
			userTags.add(new TagDTO(tag));
		}
		
		return new ResponseEntity<List<TagDTO>>(userTags, HttpStatus.OK);
	}
	
//	@PostMapping(consumes = "application/json")
//	public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO){
//		User user = new User();
//		user.setUsername(userDTO.getUsername());
//		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
//		user.setFirstName(userDTO.getFirstName());
//		user.setLastName(userDTO.getLastName());
//		
//		user = userService.save(user);
//		return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.CREATED);
//	}
	
	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable("id") Long id){
		User user = userService.findOne(id);
		if(user == null) {
			return new ResponseEntity<UserDTO>(HttpStatus.BAD_REQUEST);
		}
		
		user.setUsername(userDTO.getUsername());
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		
		user = userService.save(user);
		user.setPassword(null);
		return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteuser(@PathVariable("id") Long id){
		User user = userService.findOne(id);
		if(user == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		
		userService.remove(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
