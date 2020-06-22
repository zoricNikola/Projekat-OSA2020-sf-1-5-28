package osa.projekat.sf1528.emailClient.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import osa.projekat.sf1528.emailClient.model.Account;
import osa.projekat.sf1528.emailClient.model.Folder;
import osa.projekat.sf1528.emailClient.model.Message;
import osa.projekat.sf1528.emailClient.model.Tag;
import osa.projekat.sf1528.emailClient.repository.MessageRepository;

@Service
public class MessageService implements MessageServiceInterface {

	@Autowired
	MessageRepository messageRepository;
	
	@Override
	public Message findOne(Long messageId) {
		return messageRepository.getOne(messageId);
	}

	@Override
	public List<Message> findByAccount(Account account) {
		return messageRepository.findByAccount(account);
	}

	@Override
	public List<Message> findByFolder(Folder folder) {
		return messageRepository.findByFolder(folder);
	}

	@Override
	public List<Message> findByAccountAndTag(Account account, Tag tag) {
		return messageRepository.findByAccountAndTagsContaining(account, tag);
	}

	@Override
	public Message save(Message message) {
		return messageRepository.save(message);
	}

	@Override
	public void remove(Long messageId) {
		messageRepository.deleteById(messageId);
	}

}
