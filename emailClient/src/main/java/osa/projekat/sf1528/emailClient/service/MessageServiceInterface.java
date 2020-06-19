package osa.projekat.sf1528.emailClient.service;

import java.util.List;

import osa.projekat.sf1528.emailClient.model.Account;
import osa.projekat.sf1528.emailClient.model.Folder;
import osa.projekat.sf1528.emailClient.model.Message;

public interface MessageServiceInterface {
	
	Message findOne(Long messageId);
	
	List<Message> findByAccount(Account account);
	
	List<Message> findByFolder(Folder folder);
	
	Message save(Message message);
	
	void remove(Long messageId);

}
