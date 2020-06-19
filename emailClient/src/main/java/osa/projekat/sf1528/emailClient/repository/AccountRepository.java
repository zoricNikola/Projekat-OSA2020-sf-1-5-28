package osa.projekat.sf1528.emailClient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import osa.projekat.sf1528.emailClient.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{

//	List<Account> findByUser(User user);
}
