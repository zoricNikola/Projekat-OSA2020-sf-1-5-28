package osa.projekat.sf1528.emailClient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import osa.projekat.sf1528.emailClient.model.Tag;
import osa.projekat.sf1528.emailClient.model.User;

public interface TagRepository extends JpaRepository<Tag, Long> {
	
	List<Tag> findByUser(User user);

}