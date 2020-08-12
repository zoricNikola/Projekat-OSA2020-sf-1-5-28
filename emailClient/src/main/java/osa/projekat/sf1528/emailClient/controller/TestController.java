package osa.projekat.sf1528.emailClient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import osa.projekat.sf1528.emailClient.model.Account;
import osa.projekat.sf1528.emailClient.model.Contact;
import osa.projekat.sf1528.emailClient.model.Folder;
import osa.projekat.sf1528.emailClient.model.Rule;
import osa.projekat.sf1528.emailClient.model.Rule.Condition;
import osa.projekat.sf1528.emailClient.model.Rule.Operation;
import osa.projekat.sf1528.emailClient.model.Tag;
import osa.projekat.sf1528.emailClient.model.User;
import osa.projekat.sf1528.emailClient.service.AccountService;
import osa.projekat.sf1528.emailClient.service.ContactService;
import osa.projekat.sf1528.emailClient.service.FolderService;
import osa.projekat.sf1528.emailClient.service.RuleService;
import osa.projekat.sf1528.emailClient.service.TagService;
import osa.projekat.sf1528.emailClient.service.UserService;


@RestController
@RequestMapping(value = "api")
public class TestController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	TagService tagService;
	
	@Autowired
	ContactService contactService;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	FolderService folderService;
	
	@Autowired
	RuleService ruleService;
	
	@GetMapping(value = "/startData")
	public ResponseEntity<Void> createStartData(){
		User u1 = new User();
		u1.setUsername("admin");
		u1.setPassword("admin");
		u1.setFirstName("Nikola");
		u1.setLastName("Zoric");
		
		Tag t1 = new Tag();
		t1.setName("Hitno");
		Tag t2 = new Tag();
		t2.setName("Fakultet");
		u1.addTag(t1);
		u1.addTag(t2);
		
		Contact c1 = new Contact();
		c1.setFirstName("Pera");
		c1.setLastName("Peric");
		c1.setDisplayName("Perica");
		c1.setEmail("pera.peric@gmail.com");
		c1.setNote("");
		c1.setPhotoPath("");
		Contact c2 = new Contact();
		c2.setFirstName("Sima");
		c2.setLastName("Simic");
		c2.setDisplayName("Sima");
		c2.setEmail("sima.simic@gmail.com");
		c2.setNote("");
		c2.setPhotoPath("");
		u1.addContact(c1);
		u1.addContact(c2);
		
		Account a1 = new Account();
		a1.setSmtpAddress("smtp.gmail.com");
		a1.setSmtpPort(587);
		a1.setInServerType((short) 1);
		a1.setInServerAddress("imap.gmail.com");
		a1.setInServerPort(993);
		a1.setUsername("admin.admin@gmail.com");
		a1.setPassword("adminadmin");
		a1.setDisplayName("Nikola Zoric");
		
		Folder f1 = new Folder();
		f1.setName("Inbox");
		Rule r1 = new Rule();
		r1.setCondition(Condition.TO);
		r1.setValue(a1.getUsername());
		r1.setOperation(Operation.MOVE);
		f1.addRule(r1);
		
		Folder f2 = new Folder();
		f2.setName("Sent");
		Rule r2 = new Rule();
		r2.setCondition(Condition.FROM);
		r2.setValue(a1.getUsername());
		r2.setOperation(Operation.MOVE);
		f2.addRule(r2);
		
		Folder f3 = new Folder();
		f3.setName("Spam");
		Folder f4 = new Folder();
		f4.setName("Fakultet");
		Rule r4a = new Rule();
		r4a.setCondition(Condition.FROM);
		r4a.setValue("uns.ac.rs");
		r4a.setOperation(Operation.MOVE);
		f4.addRule(r4a);
		Rule r4b = new Rule();
		r4b.setCondition(Condition.TO);
		r4b.setValue("uns.ac.rs");
		r4b.setOperation(Operation.MOVE);
		f4.addRule(r4b);
		
		f1.addChildFolder(f4);
		a1.addFolder(f1);
		a1.addFolder(f2);
		a1.addFolder(f3);
		a1.addFolder(f4);
		u1.addAccount(a1);
		
		u1 = userService.save(u1);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@GetMapping(value = "/test")
	public ResponseEntity<Void> test(){
		long accountId = 4;
		Folder folder = new Folder();
		folder.setName("Novi folder");
		
		Account account = accountService.findOne(accountId);
		if (account == null)
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		
		account.addFolder(folder);
		
		account = accountService.save(account);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
