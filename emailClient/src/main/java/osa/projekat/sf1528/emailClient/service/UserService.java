package osa.projekat.sf1528.emailClient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import osa.projekat.sf1528.emailClient.model.User;
import osa.projekat.sf1528.emailClient.repository.UserRepository;

@Service
public class UserService implements UserServiceInterface{
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public User findOne(Long userId) {
		return userRepository.getOne(userId);
	}
	
	@Override
	public User findByUsernameAndPassword(String username, String password) {
		User user = userRepository.findByUsername(username);
		if(user.getPassword().equals(password)) {
			return user;
		}else {
			return null;
		}
	}
	
	@Override 
	public User save(User user) {
		return userRepository.save(user);
	}
	
	@Override
	public void remove(Long userId) {
		userRepository.deleteById(userId);
	}

}
