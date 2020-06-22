package osa.projekat.sf1528.emailClient.service;

import java.util.List;

import osa.projekat.sf1528.emailClient.model.Message;
import osa.projekat.sf1528.emailClient.model.Tag;
import osa.projekat.sf1528.emailClient.model.User;

public interface TagServiceInterface {
	
	Tag findOne(Long tagId);
	
	List<Tag> findByUser(User user);

	List<Tag> findByMessage(Message message);
	
	Tag save(Tag tag);
	
	void remove(Long tagId);

}