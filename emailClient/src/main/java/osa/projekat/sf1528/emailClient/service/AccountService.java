package osa.projekat.sf1528.emailClient.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import osa.projekat.sf1528.emailClient.model.Account;
import osa.projekat.sf1528.emailClient.model.User;
import osa.projekat.sf1528.emailClient.repository.AccountRepository;

@Service
public class AccountService implements AccountServiceInterface {

	@Autowired
	AccountRepository accountRepository;
	
	@Override
	public Account findOne(Long accountId) {
		return accountRepository.getOne(accountId);
	}

	@Override
	public List<Account> findByUser(User user) {
		return accountRepository.findByUser(user);
	}

	@Override
	public Account save(Account account) {
		return accountRepository.save(account);
	}

	@Override
	public void remove(Long accountId) {
		accountRepository.deleteById(accountId);
	}

}
