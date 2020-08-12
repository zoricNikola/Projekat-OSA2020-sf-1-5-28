package osa.projekat.sf1528.emailClient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import osa.projekat.sf1528.emailClient.dto.RuleDTO;
import osa.projekat.sf1528.emailClient.model.Rule;
import osa.projekat.sf1528.emailClient.service.FolderService;
import osa.projekat.sf1528.emailClient.service.RuleService;

@RestController
@RequestMapping(value = "api/rules")
public class RuleController {
	
	@Autowired
	RuleService ruleService;
	
	@Autowired
	FolderService folderService;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<RuleDTO> getRule (@PathVariable("id") Long id){
		Rule rule = ruleService.findOne(id);
		if(rule == null) {
			return new ResponseEntity<RuleDTO>(HttpStatus.NOT_FOUND);
		}	
	
		return new ResponseEntity<RuleDTO>(new RuleDTO(rule), HttpStatus.OK);
		
	}
	
	@PostMapping(consumes = "application/json")
	public ResponseEntity<RuleDTO> saveRule(@RequestBody RuleDTO ruleDTO){
		Rule rule = new Rule();
		rule.setValue(ruleDTO.getValue());
		rule.setCondition(ruleDTO.getCondition());
		rule.setOperation(ruleDTO.getOperation());
		folderService.findOne(ruleDTO.getDestination().getId()).addRule(rule);
		
		rule = ruleService.save(rule);
		return new ResponseEntity<RuleDTO>(new RuleDTO(rule), HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteRule(@PathVariable("id") Long id){
		Rule rule = ruleService.findOne(id);
		if(rule == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		
		rule.getDestination().removeRule(rule);
		ruleService.remove(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
