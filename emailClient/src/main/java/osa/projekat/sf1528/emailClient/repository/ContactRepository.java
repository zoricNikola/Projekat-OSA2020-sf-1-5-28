package osa.projekat.sf1528.emailClient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import osa.projekat.sf1528.emailClient.model.Contact;
import osa.projekat.sf1528.emailClient.model.User;

public interface ContactRepository extends JpaRepository<Contact, Long>  {

	List<Contact> findByUser(User user);
}
