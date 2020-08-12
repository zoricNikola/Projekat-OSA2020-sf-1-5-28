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

import osa.projekat.sf1528.emailClient.dto.FolderDTO;
import osa.projekat.sf1528.emailClient.dto.MessageDTO;
import osa.projekat.sf1528.emailClient.dto.RuleDTO;
import osa.projekat.sf1528.emailClient.model.Folder;
import osa.projekat.sf1528.emailClient.model.Message;
import osa.projekat.sf1528.emailClient.model.Rule;
import osa.projekat.sf1528.emailClient.service.AccountService;
import osa.projekat.sf1528.emailClient.service.FolderService;
import osa.projekat.sf1528.emailClient.service.MessageService;
import osa.projekat.sf1528.emailClient.service.RuleService;

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
	AccountService accountService;
	
	@GetMapping(value="/{id}")
	public ResponseEntity<FolderDTO> getFolder(@PathVariable("id") Long id) {
		Folder folder = folderService.findOne(id);
		if (folder == null)
			return new ResponseEntity<FolderDTO>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<FolderDTO>(new FolderDTO(folder), HttpStatus.OK);
	}
	
	@GetMapping(value="/{id}/messages")
	public ResponseEntity<List<MessageDTO>> getFolderMessages(@PathVariable("id") Long id) {
		Folder folder = folderService.findOne(id);
		if (folder == null)
			return new ResponseEntity<List<MessageDTO>>(HttpStatus.NOT_FOUND);
		
		List<Message> messages = messageService.findByFolder(folder);
		List<MessageDTO> folderMessages = new ArrayList<MessageDTO>();
		for (Message message : messages) {
			folderMessages.add(new MessageDTO(message));
		}
		
		return new ResponseEntity<List<MessageDTO>>(folderMessages, HttpStatus.OK);
	}
	
	@GetMapping(value="/{id}/childFolders")
	public ResponseEntity<List<FolderDTO>> getFolderChildFolders(@PathVariable("id") Long id) {
		Folder parentFolder = folderService.findOne(id);
		if (parentFolder == null)
			return new ResponseEntity<List<FolderDTO>>(HttpStatus.NOT_FOUND);
		
		List<Folder> folders = folderService.findByParent(parentFolder);
		List<FolderDTO> folderChildFolders = new ArrayList<FolderDTO>();
		for (Folder folder : folders) {
			folderChildFolders.add(new FolderDTO(folder));
		}
		
		return new ResponseEntity<List<FolderDTO>>(folderChildFolders, HttpStatus.OK);
	}
	
	@GetMapping(value="/{id}/rules")
	public ResponseEntity<List<RuleDTO>> getFolderRules(@PathVariable("id") Long id) {
		Folder folder = folderService.findOne(id);
		if (folder == null)
			return new ResponseEntity<List<RuleDTO>>(HttpStatus.NOT_FOUND);
		
		List<Rule> rules = ruleService.findByDestination(folder);
		List<RuleDTO> folderRules = new ArrayList<RuleDTO>();
		for (Rule rule : rules) {
			folderRules.add(new RuleDTO(rule));
		}
		
		return new ResponseEntity<List<RuleDTO>>(folderRules, HttpStatus.OK);
	}
	
//	@PostMapping(consumes = "application/json")
//	public ResponseEntity<FolderDTO> saveFolder(@RequestBody FolderDTO folderDTO) {
//		Folder folder = new Folder();
//		folder.setName(folderDTO.getName());
//		
////		if(folderDTO.getParent() != null && folderDTO.getParent().getId() != null){
////			folderService.findOne(folderDTO.getParent().getId()).addChildFolder(folder);
////		}
////		
////		accountService.findOne(folderDTO.getAccount().getId()).addFolder(folder);
//		
//		folder = folderService.save(folder);
//		return new ResponseEntity<FolderDTO>(new FolderDTO(folder), HttpStatus.CREATED);
//	}
	
	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<FolderDTO> updateFolder(@RequestBody FolderDTO folderDTO, @PathVariable("id") Long id) {
		Folder folder = folderService.findOne(id);
		if (folder == null)
			return new ResponseEntity<FolderDTO>(HttpStatus.BAD_REQUEST);
		folder.setName(folderDTO.getName());
		
		folder = folderService.save(folder);
		return new ResponseEntity<FolderDTO>(new FolderDTO(folder), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteFolder(@PathVariable("id") Long id) {
		Folder folder = folderService.findOne(id);
		if (folder == null)
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		
		folder.getParent().removeChildFolder(folder);
		folder.getAccount().removeFolder(folder);
		folderService.remove(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
}
