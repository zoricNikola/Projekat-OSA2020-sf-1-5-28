package osa.projekat.sf1528.emailClient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import osa.projekat.sf1528.emailClient.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	User findByUsername(String username);

}
