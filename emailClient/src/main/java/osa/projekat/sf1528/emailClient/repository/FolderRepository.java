package osa.projekat.sf1528.emailClient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import osa.projekat.sf1528.emailClient.model.Account;
import osa.projekat.sf1528.emailClient.model.Folder;

public interface FolderRepository extends JpaRepository<Folder, Long> {

	List<Folder> findByParent(Folder parent);
	
	List<Folder> findByAccount(Account account);
	
}
