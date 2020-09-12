package osa.projekat.sf1528.emailClient.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import osa.projekat.sf1528.emailClient.dto.FolderDTO;
import osa.projekat.sf1528.emailClient.dto.MessageDTO;
import osa.projekat.sf1528.emailClient.dto.RuleDTO;
import osa.projekat.sf1528.emailClient.model.Account;
import osa.projekat.sf1528.emailClient.model.Folder;
import osa.projekat.sf1528.emailClient.model.Message;
import osa.projekat.sf1528.emailClient.model.Rule;
import osa.projekat.sf1528.emailClient.model.User;
import osa.projekat.sf1528.emailClient.service.AccountService;
import osa.projekat.sf1528.emailClient.service.FolderService;
import osa.projekat.sf1528.emailClient.service.MessageService;
import osa.projekat.sf1528.emailClient.service.RuleService;
import osa.projekat.sf1528.emailClient.service.UserService;

@RestController
@RequestMapping(value = "api/folders")
public class FolderController {
	
	@Autowired
	FolderService folderService;
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	RuleService ruleService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	AccountService accountService;
	
	private boolean userOwnsFolder(User user, Folder folder) {
		return user.getId() == folder.getAccount().getUser().getId();
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<FolderDTO> getFolder(@PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<FolderDTO>(HttpStatus.UNAUTHORIZED);
		
		Folder folder = folderService.findOne(id);
		
		if (folder == null)
			return new ResponseEntity<FolderDTO>(HttpStatus.NOT_FOUND);
		
		if (!userOwnsFolder(user, folder))
			return new ResponseEntity<FolderDTO>(HttpStatus.UNAUTHORIZED);
		
		return new ResponseEntity<FolderDTO>(new FolderDTO(folder), HttpStatus.OK);
	}
	
	@GetMapping(value="/{id}/messages")
	public ResponseEntity<List<MessageDTO>> getFolderMessages(@PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<List<MessageDTO>>(HttpStatus.UNAUTHORIZED);
		
		Folder folder = folderService.findOne(id);
		
		if (folder == null)
			return new ResponseEntity<List<MessageDTO>>(HttpStatus.NOT_FOUND);
		
		if (!userOwnsFolder(user, folder))
			return new ResponseEntity<List<MessageDTO>>(HttpStatus.UNAUTHORIZED);
		
		List<Message> messages = messageService.findByFolder(folder);
		Collections.sort(messages, new Comparator<Message>() {
			public int compare(Message m1, Message m2) {
				return m1.getDateTime().isAfter(m2.getDateTime()) ? -1 : 1;
			}
		});
		List<MessageDTO> folderMessages = new ArrayList<MessageDTO>();
		for (Message message : messages) {
			folderMessages.add(new MessageDTO(message));
		}
		
		return new ResponseEntity<List<MessageDTO>>(folderMessages, HttpStatus.OK);
	}
	
	@GetMapping(value="/{id}/childFolders")
	public ResponseEntity<List<FolderDTO>> getFolderChildFolders(@PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<List<FolderDTO>>(HttpStatus.UNAUTHORIZED);
		
		Folder parentFolder = folderService.findOne(id);
		
		if (parentFolder == null)
			return new ResponseEntity<List<FolderDTO>>(HttpStatus.NOT_FOUND);
		
		if (!userOwnsFolder(user, parentFolder))
			return new ResponseEntity<List<FolderDTO>>(HttpStatus.UNAUTHORIZED);
		
		List<Folder> folders = folderService.findByParent(parentFolder);
		List<FolderDTO> folderChildFolders = new ArrayList<FolderDTO>();
		for (Folder folder : folders) {
			folderChildFolders.add(new FolderDTO(folder));
		}
		
		return new ResponseEntity<List<FolderDTO>>(folderChildFolders, HttpStatus.OK);
	}
	
	@PostMapping(value = "/{id}/childFolders", consumes = "application/json")
	public ResponseEntity<FolderDTO> addFolderChildFolder(@RequestBody FolderDTO folderDTO, @PathVariable("id") Long id){
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<FolderDTO>(HttpStatus.UNAUTHORIZED);
		
		Folder parentFolder = folderService.findOne(id);
		
		if (parentFolder == null)
			return new ResponseEntity<FolderDTO>(HttpStatus.BAD_REQUEST);
		
		if (!userOwnsFolder(user, parentFolder))
			return new ResponseEntity<FolderDTO>(HttpStatus.UNAUTHORIZED);
		
		Folder folder = new Folder();
		folder.setName(folderDTO.getName());
		parentFolder.addChildFolder(folder);
		parentFolder.getAccount().addFolder(folder);
		
		folder = folderService.save(folder);
		return new ResponseEntity<FolderDTO>(new FolderDTO(folder), HttpStatus.CREATED);
	}
	
	@GetMapping(value="/{id}/rules")
	public ResponseEntity<List<RuleDTO>> getFolderRules(@PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<List<RuleDTO>>(HttpStatus.UNAUTHORIZED);
		
		Folder folder = folderService.findOne(id);
		
		if (folder == null)
			return new ResponseEntity<List<RuleDTO>>(HttpStatus.NOT_FOUND);
		
		if (!userOwnsFolder(user, folder))
			return new ResponseEntity<List<RuleDTO>>(HttpStatus.UNAUTHORIZED);
		
		List<Rule> rules = ruleService.findByDestination(folder);
		List<RuleDTO> folderRules = new ArrayList<RuleDTO>();
		for (Rule rule : rules) {
			folderRules.add(new RuleDTO(rule));
		}
		
		return new ResponseEntity<List<RuleDTO>>(folderRules, HttpStatus.OK);
	}
	
	@PutMapping(value="/{id}/rules")
	public ResponseEntity<List<RuleDTO>> updateFolderRules(@PathVariable("id") Long id, @RequestBody List<RuleDTO> updatedRules) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<List<RuleDTO>>(HttpStatus.UNAUTHORIZED);
		
		Folder folder = folderService.findOne(id);
		
		if (folder == null)
			return new ResponseEntity<List<RuleDTO>>(HttpStatus.NOT_FOUND);
		
		if (!userOwnsFolder(user, folder))
			return new ResponseEntity<List<RuleDTO>>(HttpStatus.UNAUTHORIZED);
		
		List<Rule> currentRules = ruleService.findByDestination(folder);
		
		Set<Rule> toDeleteRules = new HashSet<Rule>();
		Set<RuleDTO> toAddRules = new HashSet<RuleDTO>();
		
		for (Rule cR : currentRules) {
			boolean shouldDelete = true;
			for (RuleDTO uR : updatedRules) {
				if (uR.getId() != null && uR.getId() > 0) {
					if (cR.getId() == uR.getId()) {
						shouldDelete = false;
						break;
					}
				}
			}
			
			if (shouldDelete)
				toDeleteRules.add(cR);
		}
		
		for (RuleDTO uR : updatedRules) {
			if (uR.getId() == null || uR.getId() == 0)
				toAddRules.add(uR);
		}
		
		for (Rule r : toDeleteRules)
			folder.removeRule(r);
		
		for (RuleDTO ruleDTO : toAddRules) {
			Rule rule = new Rule();
			rule.setValue(ruleDTO.getValue());
			rule.setCondition(ruleDTO.getCondition());
			rule.setOperation(ruleDTO.getOperation());
			folder.addRule(rule);
		}
		
		folder = folderService.save(folder);
		
		List<RuleDTO> rules = new ArrayList<RuleDTO>();
		for (Rule r : folder.getRules())
			rules.add(new RuleDTO(r));
		
		return new ResponseEntity<List<RuleDTO>>(rules, HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/{accountId}", consumes = "application/json")
	public ResponseEntity<FolderDTO> saveFolder(@RequestBody FolderDTO folderDTO, @PathVariable("accountId") Long accountId) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<FolderDTO>(HttpStatus.UNAUTHORIZED);
		
		Account account = accountService.findOne(accountId);
		
		if (account == null)
			return new ResponseEntity<FolderDTO>(HttpStatus.BAD_REQUEST);
		
		if (user.getId() != account.getUser().getId())
			return new ResponseEntity<FolderDTO>(HttpStatus.UNAUTHORIZED);
		
		Folder folder = new Folder();
		folder.setName(folderDTO.getName());
		folder.setParent(null);
		account.addFolder(folder);
		
		folder = folderService.save(folder);
		return new ResponseEntity<FolderDTO>(new FolderDTO(folder), HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<FolderDTO> updateFolder(@RequestBody FolderDTO folderDTO, @PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<FolderDTO>(HttpStatus.UNAUTHORIZED);
		
		Folder folder = folderService.findOne(id);
		
		if (folder == null)
			return new ResponseEntity<FolderDTO>(HttpStatus.BAD_REQUEST);
		
		if (!userOwnsFolder(user, folder))
			return new ResponseEntity<FolderDTO>(HttpStatus.UNAUTHORIZED);
		
		folder.setName(folderDTO.getName());
		
		folder = folderService.save(folder);
		return new ResponseEntity<FolderDTO>(new FolderDTO(folder), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteFolder(@PathVariable("id") Long id) {
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if (user == null)
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		
		Folder folder = folderService.findOne(id);
		
		if (folder == null)
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		
		if (!userOwnsFolder(user, folder))
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		
		if (folder.getParent() != null)
			folder.getParent().removeChildFolder(folder);
		folder.getAccount().removeFolder(folder);
		folderService.remove(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
}
