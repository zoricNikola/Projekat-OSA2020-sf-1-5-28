package osa.projekat.sf1528.emailClient.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import osa.projekat.sf1528.emailClient.model.Folder;
import osa.projekat.sf1528.emailClient.model.Rule;
import osa.projekat.sf1528.emailClient.repository.RuleRepository;

@Service
public class RuleService implements RuleServiceInterface{
	
	@Autowired
	RuleRepository ruleRepository;
	
	@Override
	public Rule findOne(Long ruleId) {
		return ruleRepository.getOne(ruleId);
	}
	
	@Override
	public List<Rule> findByDestination(Folder destination){
		return ruleRepository.findByDestination(destination);
	}
	
	@Override
	public Rule save(Rule rule) {
		return ruleRepository.save(rule);
	}
	
	@Override
	public void remove(Long ruleId) {
		ruleRepository.deleteById(ruleId);
	}

}
