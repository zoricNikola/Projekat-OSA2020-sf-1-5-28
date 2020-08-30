package osa.projekat.sf1528.emailClient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import osa.projekat.sf1528.emailClient.dto.RuleDTO;
import osa.projekat.sf1528.emailClient.model.Folder;
import osa.projekat.sf1528.emailClient.model.Rule;
import osa.projekat.sf1528.emailClient.model.User;
import osa.projekat.sf1528.emailClient.service.FolderService;
import osa.projekat.sf1528.emailClient.service.RuleService;
import osa.projekat.sf1528.emailClient.service.UserService;

@RestController
@RequestMapping(value = "api/rules")
public class RuleController {
	
	@Autowired
	RuleService ruleService;
	
	@Autowired
	FolderService folderService;
	
	@Autowired
	UserService userService;
	
	private boolean userOwnsRule(User user, Rule rule) {
		return user.getId() == rule.getDestination().getAccount().getUser().getId();
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<RuleDTO> getRule (@PathVariable("id") Long id){
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if(user == null) {
			return new ResponseEntity<RuleDTO>(HttpStatus.UNAUTHORIZED);
		}
		
		Rule rule = ruleService.findOne(id);
		
		if(rule == null) {
			return new ResponseEntity<RuleDTO>(HttpStatus.NOT_FOUND);
		}
		if(!userOwnsRule(user, rule)) {
			return new ResponseEntity<RuleDTO>(HttpStatus.UNAUTHORIZED);
		}
	
		return new ResponseEntity<RuleDTO>(new RuleDTO(rule), HttpStatus.OK);
		
	}
	
	@PostMapping(value = "/{folderId}", consumes = "application/json")
	public ResponseEntity<RuleDTO> saveRule(@RequestBody RuleDTO ruleDTO, @PathVariable("folderId") Long folderId){
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if(user == null) {
			return new ResponseEntity<RuleDTO>(HttpStatus.UNAUTHORIZED);
		}
		
		Folder folder = folderService.findOne(folderId);
		
		if (folder == null) {
			return new ResponseEntity<RuleDTO>(HttpStatus.BAD_REQUEST);
		}
		if(user.getId() != folder.getAccount().getUser().getId()) {
			return new ResponseEntity<RuleDTO>(HttpStatus.UNAUTHORIZED);
		}
		
		Rule rule = new Rule();
		rule.setValue(ruleDTO.getValue());
		rule.setCondition(ruleDTO.getCondition());
		rule.setOperation(ruleDTO.getOperation());
		folder.addRule(rule);
		
		rule = ruleService.save(rule);
		return new ResponseEntity<RuleDTO>(new RuleDTO(rule), HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteRule(@PathVariable("id") Long id){
		User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		if(user == null) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		
		Rule rule = ruleService.findOne(id);
		
		if(rule == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		if(!userOwnsRule(user, rule)) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		
		rule.getDestination().removeRule(rule);
		ruleService.remove(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
