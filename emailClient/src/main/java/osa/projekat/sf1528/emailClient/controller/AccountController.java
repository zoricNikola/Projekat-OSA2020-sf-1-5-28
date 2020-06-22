package osa.projekat.sf1528.emailClient.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import osa.projekat.sf1528.emailClient.dto.AccountDTO;
import osa.projekat.sf1528.emailClient.dto.FolderDTO;
import osa.projekat.sf1528.emailClient.dto.MessageDTO;
import osa.projekat.sf1528.emailClient.model.Account;
import osa.projekat.sf1528.emailClient.model.Folder;
import osa.projekat.sf1528.emailClient.model.Message;
import osa.projekat.sf1528.emailClient.service.AccountServiceInterface;
import osa.projekat.sf1528.emailClient.service.FolderServiceInterface;
import osa.projekat.sf1528.emailClient.service.MessageServiceInterface;
import osa.projekat.sf1528.emailClient.service.UserServiceInterface;

@RestController
@RequestMapping(value = "api/accounts")
public class AccountController {
	
	@Autowired
	AccountServiceInterface accountService;
	
	@Autowired
	UserServiceInterface userService;
	
	@Autowired
	MessageServiceInterface messageService;
	
	@Autowired
	FolderServiceInterface folderService;
	
	@GetMapping(value="/{id}")
	public ResponseEntity<AccountDTO> getAccount(@PathVariable("id") Long id) {
		Account account = accountService.findOne(id);
		if (account == null)
			return new ResponseEntity<AccountDTO>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<AccountDTO>(new AccountDTO(account), HttpStatus.OK);
	}
	
	@GetMapping(value="/{id}/messages")
	public ResponseEntity<List<MessageDTO>> getAccountMessages(@PathVariable("id") Long id) {
		Account account = accountService.findOne(id);
		if (account == null)
			return new ResponseEntity<List<MessageDTO>>(HttpStatus.NOT_FOUND);
		
		List<Message> messages = messageService.findByAccount(account);
		List<MessageDTO> accountMessages = new ArrayList<MessageDTO>();
		for (Message message : messages) {
			accountMessages.add(new MessageDTO(message));
		}
		
		return new ResponseEntity<List<MessageDTO>>(accountMessages, HttpStatus.OK);
	}
	
	@GetMapping(value="/{id}/folders")
	public ResponseEntity<List<FolderDTO>> getAccountFolders(@PathVariable("id") Long id) {
		Account account = accountService.findOne(id);
		if (account == null)
			return new ResponseEntity<List<FolderDTO>>(HttpStatus.NOT_FOUND);
		
		List<Folder> folders = folderService.findByAccount(account);
		List<FolderDTO> accountFolders = new ArrayList<FolderDTO>();
		for (Folder folder : folders) {
			accountFolders.add(new FolderDTO(folder));
		}
		
		return new ResponseEntity<List<FolderDTO>>(accountFolders, HttpStatus.OK);
	}
	
	@PostMapping(consumes = "application/json")
	public ResponseEntity<AccountDTO> saveAccount(@RequestBody AccountDTO accountDTO) {
		Account account = new Account();
		account.setSmtpAddress(accountDTO.getSmtpAddress());
		account.setSmtpPort(accountDTO.getSmtpPort());
		account.setInServerType(accountDTO.getInServerType());
		account.setInServerAddress(accountDTO.getInServerAddress());
		account.setInServerPort(accountDTO.getInServerPort());
		account.setUsername(accountDTO.getUsername());
		account.setPassword(accountDTO.getPassword());
		account.setDisplayName(accountDTO.getDisplayName());
		account.setUser(userService.findOne(accountDTO.getUser().getId()));
		
		account = accountService.save(account);
		return new ResponseEntity<AccountDTO>(new AccountDTO(account), HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<AccountDTO> updateAccount(@RequestBody AccountDTO accountDTO, @PathVariable("id") Long id) {
		Account account = accountService.findOne(id);
		if (account == null)
			return new ResponseEntity<AccountDTO>(HttpStatus.BAD_REQUEST);
		account.setSmtpAddress(accountDTO.getSmtpAddress());
		account.setSmtpPort(accountDTO.getSmtpPort());
		account.setInServerType(accountDTO.getInServerType());
		account.setInServerAddress(accountDTO.getInServerAddress());
		account.setInServerPort(accountDTO.getInServerPort());
		account.setUsername(accountDTO.getUsername());
		account.setPassword(accountDTO.getPassword());
		account.setDisplayName(accountDTO.getDisplayName());
		
		account = accountService.save(account);
		return new ResponseEntity<AccountDTO>(new AccountDTO(account), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteAccount(@PathVariable("id") Long id) {
		Account account = accountService.findOne(id);
		if (account == null)
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		
		accountService.remove(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
