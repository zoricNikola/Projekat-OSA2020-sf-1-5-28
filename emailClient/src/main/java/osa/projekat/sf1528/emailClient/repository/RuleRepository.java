package osa.projekat.sf1528.emailClient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import osa.projekat.sf1528.emailClient.model.Folder;
import osa.projekat.sf1528.emailClient.model.Rule;

public interface RuleRepository extends JpaRepository<Rule, Long>{
	
	List<Rule> findByDestination(Folder destination);

}
