package osa.projekat.sf1528.emailClient.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
import osa.projekat.sf1528.emailClient.mail.MailUtil;
import osa.projekat.sf1528.emailClient.model.Account;
import osa.projekat.sf1528.emailClient.model.Folder;
import osa.projekat.sf1528.emailClient.model.Message;
import osa.projekat.sf1528.emailClient.model.Rule;
import osa.projekat.sf1528.emailClient.model.User;
import osa.projekat.sf1528.emailClient.model.Rule.Condition;
import osa.projekat.sf1528.emailClient.model.Rule.Operation;
import osa.projekat.sf1528.emailClient.service.AccountService;
import osa.projekat.sf1528.emailClient.service.FolderService;
import osa.projekat.sf1528.emailClient.service.MessageService;
import osa.projekat.sf1528.emailClient.service.UserService;

@RestController
@RequestMapping(value = "api/accounts")
public class AccountController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	FolderService folderService;
	
	private boolean userOwnsAccount(User user, Account account) {
		return user.getId() == account.getUser().getId();
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<AccountDTO> getAccount(@PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<AccountDTO>(HttpStatus.UNAUTHORIZED);
		
		Account account = accountService.findOne(id);
		
		if (account == null)
			return new ResponseEntity<AccountDTO>(HttpStatus.NOT_FOUND);
		
		if (!userOwnsAccount(user, account))
			return new ResponseEntity<AccountDTO>(HttpStatus.UNAUTHORIZED);
		
		
		return new ResponseEntity<AccountDTO>(new AccountDTO(account), HttpStatus.OK);
	}
	
	@GetMapping(value="/{id}/messages")
	public ResponseEntity<List<MessageDTO>> getAccountMessages(@PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<List<MessageDTO>>(HttpStatus.UNAUTHORIZED);
		
		Account account = accountService.findOne(id);
		
		if (account == null)
			return new ResponseEntity<List<MessageDTO>>(HttpStatus.NOT_FOUND);

		if (!userOwnsAccount(user, account))
			return new ResponseEntity<List<MessageDTO>>(HttpStatus.UNAUTHORIZED);
		
		List<Message> messages = messageService.findByAccount(account);
		List<MessageDTO> accountMessages = new ArrayList<MessageDTO>();
		for (Message message : messages) {
			accountMessages.add(new MessageDTO(message));
		}
		
		return new ResponseEntity<List<MessageDTO>>(accountMessages, HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value="/{id}/sync")
	public ResponseEntity<Map<String, Object>> syncAccountMessages(@PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<Map<String, Object>>(HttpStatus.UNAUTHORIZED);
		
		Account account = accountService.findOne(id);
		
		if (account == null)
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NOT_FOUND);

		if (!userOwnsAccount(user, account))
			return new ResponseEntity<Map<String, Object>>(HttpStatus.UNAUTHORIZED);
		
		Map<String, Object> result = MailUtil.syncMessages(account);
		
		if (result == null)
			return new ResponseEntity<Map<String, Object>>(HttpStatus.INTERNAL_SERVER_ERROR);
		
		List<Message> newMessages = (List<Message>) result.get("messages");
		if (newMessages != null && newMessages.size() > 0) {
			account.getMessages().addAll(newMessages);
			MailUtil.storeMessagesInAccountsInboxFolder(newMessages, account);
		}
		account = accountService.save(account);
		
		Map<String, Object> resultDTO = new HashMap<String, Object>();
		resultDTO.put("mode", result.get("mode"));
		
		List<MessageDTO> newMessagesDTO = new ArrayList<MessageDTO>();
		for (Message message : newMessages) {
			newMessagesDTO.add(new MessageDTO(message));
		}
		
		resultDTO.put("messages", newMessagesDTO);
		
		return new ResponseEntity<Map<String, Object>>(resultDTO, HttpStatus.OK);
	}
	
	@GetMapping(value="/{id}/folders")
	public ResponseEntity<List<FolderDTO>> getAccountFolders(@PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<List<FolderDTO>>(HttpStatus.UNAUTHORIZED);
		
		Account account = accountService.findOne(id);
		
		if (account == null)
			return new ResponseEntity<List<FolderDTO>>(HttpStatus.NOT_FOUND);
		
		if (!userOwnsAccount(user, account))
			return new ResponseEntity<List<FolderDTO>>(HttpStatus.UNAUTHORIZED);
		
		List<Folder> folders = folderService.findByAccount(account);
		List<FolderDTO> accountFolders = new ArrayList<FolderDTO>();
		for (Folder folder : folders) {
			accountFolders.add(new FolderDTO(folder));
		}
		
		return new ResponseEntity<List<FolderDTO>>(accountFolders, HttpStatus.OK);
	}
	
	@PostMapping(value = "/{userId}", consumes = "application/json")
	public ResponseEntity<AccountDTO> saveAccount(@RequestBody AccountDTO accountDTO, @PathVariable("userId") Long userId) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<AccountDTO>(HttpStatus.UNAUTHORIZED);
		
		if (user.getId() != userId)
			return new ResponseEntity<AccountDTO>(HttpStatus.UNAUTHORIZED);
		
		Account account = new Account();
		account.setSmtpAddress(accountDTO.getSmtpAddress());
		account.setSmtpPort(accountDTO.getSmtpPort());
		account.setInServerType(accountDTO.getInServerType());
		account.setInServerAddress(accountDTO.getInServerAddress());
		account.setInServerPort(accountDTO.getInServerPort());
		account.setUsername(accountDTO.getUsername());
		account.setPassword(accountDTO.getPassword());
		account.setDisplayName(accountDTO.getDisplayName());
		user.addAccount(account);
		
		Folder f1 = new Folder();
		f1.setName("Inbox");
		Rule r1 = new Rule();
		r1.setCondition(Condition.TO);
		r1.setValue(account.getUsername());
		r1.setOperation(Operation.MOVE);
		f1.addRule(r1);
		
		Folder f2 = new Folder();
		f2.setName("Sent");
		Rule r2 = new Rule();
		r2.setCondition(Condition.FROM);
		r2.setValue(account.getUsername());
		r2.setOperation(Operation.MOVE);
		f2.addRule(r2);
		
		Folder f3 = new Folder();
		f3.setName("Drafts");
		
		account.addFolder(f1);
		account.addFolder(f2);
		account.addFolder(f3);
		
		account = accountService.save(account);
		return new ResponseEntity<AccountDTO>(new AccountDTO(account), HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<AccountDTO> updateAccount(@RequestBody AccountDTO accountDTO, @PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<AccountDTO>(HttpStatus.UNAUTHORIZED);
		
		Account account = accountService.findOne(id);
		
		if (account == null)
			return new ResponseEntity<AccountDTO>(HttpStatus.BAD_REQUEST);
		
		if (!userOwnsAccount(user, account))
			return new ResponseEntity<AccountDTO>(HttpStatus.UNAUTHORIZED);
		
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
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		
		Account account = accountService.findOne(id);
		
		if (account == null)
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		
		if (!userOwnsAccount(user, account))
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		
		account.getUser().removeAccount(account);
		accountService.remove(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
