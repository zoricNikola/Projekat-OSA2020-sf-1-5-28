package osa.projekat.sf1528.emailClient.service;

import java.util.List;

import osa.projekat.sf1528.emailClient.model.Contact;
import osa.projekat.sf1528.emailClient.model.User;

public interface ContactServiceInterface {
	
	Contact findOne(Long contactId);
	
	List<Contact> findByUser(User user);
	
	Contact save(Contact contact);
	
	void remove(Long contactId);
	
}
