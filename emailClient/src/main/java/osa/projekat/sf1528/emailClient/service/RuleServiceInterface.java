package osa.projekat.sf1528.emailClient.service;

import java.util.List;

import osa.projekat.sf1528.emailClient.model.Folder;
import osa.projekat.sf1528.emailClient.model.Rule;

public interface RuleServiceInterface {

	Rule findOne(Long ruleId);
	List<Rule> findByDestination(Folder destination);
	Rule save(Rule rule);
	
	void remove(Long ruleId);
}
