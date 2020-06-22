package osa.projekat.sf1528.emailClient.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import osa.projekat.sf1528.emailClient.model.Tag;
import osa.projekat.sf1528.emailClient.model.User;
import osa.projekat.sf1528.emailClient.repository.TagRepository;

@Service
public class TagService implements TagServiceInterface {

	@Autowired
	TagRepository tagRepository;
	
	@Override
	public Tag findOne(Long tagId) {
		return tagRepository.getOne(tagId);
	}

	@Override
	public List<Tag> findByUser(User user) {
		return tagRepository.findByUser(user);
	}

	@Override
	public Tag save(Tag tag) {
		return tagRepository.save(tag);
	}

	@Override
	public void remove(Long tagId) {
		tagRepository.deleteById(tagId);
	}

}