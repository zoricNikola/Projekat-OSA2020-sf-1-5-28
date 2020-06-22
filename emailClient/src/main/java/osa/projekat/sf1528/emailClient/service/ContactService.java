package osa.projekat.sf1528.emailClient.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import osa.projekat.sf1528.emailClient.model.Contact;
import osa.projekat.sf1528.emailClient.model.User;
import osa.projekat.sf1528.emailClient.repository.ContactRepository;


@Service
public class ContactService implements ContactServiceInterface {

	@Autowired
	ContactRepository contactRepository;
	
	@Override
	public Contact findOne(Long contactId) {
		return contactRepository.getOne(contactId);
	}

	@Override
	public List<Contact> findByUser(User user) {
		return contactRepository.findByUser(user);
	}

	@Override
	public Contact save(Contact contact) {
		return contactRepository.save(contact);
	}

	@Override
	public void remove(Long contactId) {
		contactRepository.deleteById(contactId);
	}

}
