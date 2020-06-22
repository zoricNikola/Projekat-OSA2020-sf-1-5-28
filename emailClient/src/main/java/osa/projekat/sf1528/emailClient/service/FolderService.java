package osa.projekat.sf1528.emailClient.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import osa.projekat.sf1528.emailClient.model.Account;
import osa.projekat.sf1528.emailClient.model.Folder;
import osa.projekat.sf1528.emailClient.repository.FolderRepository;

@Service
public class FolderService implements FolderServiceInterface {

	@Autowired
	FolderRepository folderRepository;
	
	@Override
	public Folder findOne(Long folderId) {
		return folderRepository.getOne(folderId);
	}

	@Override
	public List<Folder> findByParent(Folder parent) {
		return folderRepository.findByParent(parent);
	}

	@Override
	public List<Folder> findByAccount(Account account) {
		return folderRepository.findByAccount(account);
	}

	@Override
	public Folder save(Folder folder) {
		return folderRepository.save(folder);
	}

	@Override
	public void remove(Long folderId) {
		folderRepository.deleteById(folderId);
	}

}
