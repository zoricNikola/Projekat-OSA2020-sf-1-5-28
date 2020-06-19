package osa.projekat.sf1528.emailClient.service;

import java.util.List;

import osa.projekat.sf1528.emailClient.model.Account;

public interface AccountServiceInterface {
	
	Account findOne(Long accountId);
	
//	List<Account> findByUser(User user);
	
	Account save(Account account);
	
	void remove(Long accountId);

}
