package osa.projekat.sf1528.emailClient.controller;

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

import osa.projekat.sf1528.emailClient.dto.ContactDTO;
import osa.projekat.sf1528.emailClient.model.Contact;
import osa.projekat.sf1528.emailClient.model.User;
import osa.projekat.sf1528.emailClient.service.ContactService;
import osa.projekat.sf1528.emailClient.service.UserService;

@RestController
@RequestMapping(value = "api/contacts")
public class ContactController {
	
	@Autowired
	ContactService contactService;
	
	@Autowired
	UserService userService;
	
	@GetMapping(value="/{id}")
	public ResponseEntity<ContactDTO> getContact(@PathVariable("id") Long id) {
		Contact contact = contactService.findOne(id);
		if (contact == null)
			return new ResponseEntity<ContactDTO>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<ContactDTO>(new ContactDTO(contact), HttpStatus.OK);
	}
	
	@PostMapping(value = "/{userId}", consumes = "application/json")
	public ResponseEntity<ContactDTO> saveContact(@RequestBody ContactDTO contactDTO, @PathVariable("userId") Long userId) {
		User user = userService.findOne(userId);
		if(user == null) {
			return new ResponseEntity<ContactDTO>(HttpStatus.BAD_REQUEST);
		}
		
		Contact contact = new Contact();
		contact.setFirstName(contactDTO.getFirstName());
		contact.setLastName(contactDTO.getLastName());
		contact.setDisplayName(contactDTO.getDisplayName());
		contact.setEmail(contactDTO.getEmail());
		contact.setNote(contactDTO.getNote());
		contact.setPhotoPath(contactDTO.getPhotoPath());
		user.addContact(contact);
		
		contact = contactService.save(contact);
		return new ResponseEntity<ContactDTO>(new ContactDTO(contact), HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<ContactDTO> updateContact(@RequestBody ContactDTO contactDTO, @PathVariable("id") Long id) {
		Contact contact = contactService.findOne(id);
		if (contact == null)
			return new ResponseEntity<ContactDTO>(HttpStatus.BAD_REQUEST);
		
		contact.setFirstName(contactDTO.getFirstName());
		contact.setLastName(contactDTO.getLastName());
		contact.setDisplayName(contactDTO.getDisplayName());
		contact.setEmail(contactDTO.getEmail());
		contact.setNote(contactDTO.getNote());
		contact.setPhotoPath(contactDTO.getPhotoPath());
		
		contact = contactService.save(contact);
		return new ResponseEntity<ContactDTO>(new ContactDTO(contact), HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteContact(@PathVariable("id") Long id) {
		Contact contact = contactService.findOne(id);
		if (contact == null)
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		
		contact.getUser().removeContact(contact);
		contactService.remove(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	

}
