package osa.projekat.sf1528.emailClient.service;

import java.util.List;

import osa.projekat.sf1528.emailClient.model.Account;
import osa.projekat.sf1528.emailClient.model.Folder;

public interface FolderServiceInterface {
	
	Folder findOne(Long folderId);
	
	List<Folder> findByParent(Folder parent);
	
	List<Folder> findByAccount(Account account);
	
	Folder save(Folder folder);
	
	void remove(Long folderId);

}
