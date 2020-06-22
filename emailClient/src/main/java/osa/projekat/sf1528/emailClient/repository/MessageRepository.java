package osa.projekat.sf1528.emailClient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import osa.projekat.sf1528.emailClient.model.Account;
import osa.projekat.sf1528.emailClient.model.Folder;
import osa.projekat.sf1528.emailClient.model.Message;
import osa.projekat.sf1528.emailClient.model.Tag;

public interface MessageRepository extends JpaRepository<Message, Long> {
	
	List<Message> findByAccount(Account account);
	
	List<Message> findByFolder(Folder folder);
	
	List<Message> findByAccountAndTagsContaining(Account account, Tag tag);

}
