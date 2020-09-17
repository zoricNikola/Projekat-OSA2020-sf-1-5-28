package osa.projekat.sf1528.emailClient.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.tomcat.util.http.fileupload.FileUtils;
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

import osa.projekat.sf1528.emailClient.dto.ContactDTO;
import osa.projekat.sf1528.emailClient.model.Contact;
import osa.projekat.sf1528.emailClient.model.User;
import osa.projekat.sf1528.emailClient.service.ContactService;
import osa.projekat.sf1528.emailClient.service.UserService;
import osa.projekat.sf1528.emailClient.util.Base64;
import osa.projekat.sf1528.emailClient.util.FilesUtil;

@RestController
@RequestMapping(value = "api/contacts")
public class ContactController {
	
	@Autowired
	ContactService contactService;
	
	@Autowired
	UserService userService;
	
	private boolean userOwnsContact(User user, Contact contact) {
		return user.getId() == contact.getUser().getId();
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<ContactDTO> getContact(@PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<ContactDTO>(HttpStatus.UNAUTHORIZED);
		
		Contact contact = contactService.findOne(id);
		
		if (contact == null)
			return new ResponseEntity<ContactDTO>(HttpStatus.NOT_FOUND);
		
		if (!userOwnsContact(user, contact))
			return new ResponseEntity<ContactDTO>(HttpStatus.UNAUTHORIZED);
		
		return new ResponseEntity<ContactDTO>(new ContactDTO(contact), HttpStatus.OK);
	}
	
	@PostMapping(value = "/{userId}", consumes = "application/json")
	public ResponseEntity<ContactDTO> saveContact(@RequestBody ContactDTO contactDTO, @PathVariable("userId") Long userId) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<ContactDTO>(HttpStatus.UNAUTHORIZED);
		
		if (user.getId() != userId)
			return new ResponseEntity<ContactDTO>(HttpStatus.UNAUTHORIZED);
		
		Contact contact = new Contact();
		contact.setFirstName(contactDTO.getFirstName());
		contact.setLastName(contactDTO.getLastName());
		contact.setDisplayName(contactDTO.getDisplayName());
		contact.setEmail(contactDTO.getEmail());
		contact.setNote(contactDTO.getNote());
		user.addContact(contact);
		
		contact = contactService.save(contact);
		
		if (contactDTO.getEncodedPhotoData() != null && !contactDTO.getEncodedPhotoData().isEmpty()) {
			byte[] photoData = Base64.decode(contactDTO.getEncodedPhotoData());
			String path = String.format("./data/contactPhotos/%d", contact.getId());
			
			if (FilesUtil.saveBytes(photoData, path)) {
				contact.setPhotoPath(path);
				contact = contactService.save(contact);
			}
		}
		return new ResponseEntity<ContactDTO>(new ContactDTO(contact), HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<ContactDTO> updateContact(@RequestBody ContactDTO contactDTO, @PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<ContactDTO>(HttpStatus.UNAUTHORIZED);
		
		Contact contact = contactService.findOne(id);
		
		if (contact == null)
			return new ResponseEntity<ContactDTO>(HttpStatus.BAD_REQUEST);
		
		if (!userOwnsContact(user, contact))
			return new ResponseEntity<ContactDTO>(HttpStatus.UNAUTHORIZED);
		
		contact.setFirstName(contactDTO.getFirstName());
		contact.setLastName(contactDTO.getLastName());
		contact.setDisplayName(contactDTO.getDisplayName());
		contact.setEmail(contactDTO.getEmail());
		contact.setNote(contactDTO.getNote());
		
		if (contactDTO.getEncodedPhotoData() != null && !contactDTO.getEncodedPhotoData().isEmpty()) {
			byte[] photoData = Base64.decode(contactDTO.getEncodedPhotoData());
			String path = String.format("./data/contactPhotos/%d", id);
			
			if (FilesUtil.saveBytes(photoData, path))
				contact.setPhotoPath(path);
		}
		
		contact = contactService.save(contact);
		return new ResponseEntity<ContactDTO>(new ContactDTO(contact), HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteContact(@PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		
		Contact contact = contactService.findOne(id);
		
		if (contact == null)
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		
		if (!userOwnsContact(user, contact))
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		
		contact.getUser().removeContact(contact);
		contactService.remove(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	

}
