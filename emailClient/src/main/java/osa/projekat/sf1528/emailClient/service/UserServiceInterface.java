package osa.projekat.sf1528.emailClient.service;

import osa.projekat.sf1528.emailClient.model.User;

public interface UserServiceInterface {
	
	User findOne(Long userId);
	User findByUsernameAndPassword(String username, String password);
	User save(User user);
	void remove(Long userId);

}
