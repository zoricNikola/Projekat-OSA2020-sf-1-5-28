package osa.projekat.sf1528.emailClient.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import osa.projekat.sf1528.emailClient.mail.MailUtil;
import osa.projekat.sf1528.emailClient.model.Account;
import osa.projekat.sf1528.emailClient.model.Attachment;
import osa.projekat.sf1528.emailClient.model.Contact;
import osa.projekat.sf1528.emailClient.model.Folder;
import osa.projekat.sf1528.emailClient.model.Message;
import osa.projekat.sf1528.emailClient.model.Rule;
import osa.projekat.sf1528.emailClient.model.Rule.Condition;
import osa.projekat.sf1528.emailClient.model.Rule.Operation;
import osa.projekat.sf1528.emailClient.model.Tag;
import osa.projekat.sf1528.emailClient.model.User;
import osa.projekat.sf1528.emailClient.service.AccountService;
import osa.projekat.sf1528.emailClient.service.ContactService;
import osa.projekat.sf1528.emailClient.service.FolderService;
import osa.projekat.sf1528.emailClient.service.MessageService;
import osa.projekat.sf1528.emailClient.service.RuleService;
import osa.projekat.sf1528.emailClient.service.TagService;
import osa.projekat.sf1528.emailClient.service.UserService;
import osa.projekat.sf1528.emailClient.util.Base64;


@RestController
@RequestMapping(value = "api/test")
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
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping(value = "/startData")
	public ResponseEntity<Void> createStartData(){
		User u1 = new User();
		u1.setUsername("admin");
		u1.setPassword(passwordEncoder.encode("admin"));
		u1.setFirstName("Nikola");
		u1.setLastName("Zoric");
		
		Tag t1 = new Tag();
		t1.setName("Hitno");
		Tag t2 = new Tag();
		t2.setName("Fakultet");
		u1.addTag(t1);
		u1.addTag(t2);
//		t1.setUser(u1);
//		t2.setUser(u1);
		
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
//		c1.setUser(u1);
//		c2.setUser(u1);
		
		Account a1 = new Account();
		a1.setSmtpAddress("smtp.gmail.com");
		a1.setSmtpPort(587);
		a1.setInServerType(Account.InServerType.POP3);
		a1.setInServerAddress("pop.gmail.com");
		a1.setInServerPort(995);
		a1.setUsername("nikola.se.zoric@gmail.com");
		a1.setPassword("***********");
		a1.setDisplayName("Nikola Zoric");
		
		Folder f1 = new Folder();
		f1.setName("Inbox");
//		Rule r1 = new Rule();
//		r1.setCondition(Condition.TO);
//		r1.setValue(a1.getUsername());
//		r1.setOperation(Operation.MOVE);
//		f1.addRule(r1);
//		r1.setDestination(f1);
		
		Folder f2 = new Folder();
		f2.setName("Sent");
//		Rule r2 = new Rule();
//		r2.setCondition(Condition.FROM);
//		r2.setValue(a1.getUsername());
//		r2.setOperation(Operation.MOVE);
//		f2.addRule(r2);
//		r2.setDestination(f2);
		
		Folder f5 = new Folder();
		f5.setName("Drafts");
		
//		Folder f3 = new Folder();
//		f3.setName("Spam");
//		Folder f4 = new Folder();
//		f4.setName("Fakultet");
//		Rule r4a = new Rule();
//		r4a.setCondition(Condition.FROM);
//		r4a.setValue("uns.ac.rs");
//		r4a.setOperation(Operation.MOVE);
//		f4.addRule(r4a);
////		r4a.setDestination(f4);
//		Rule r4b = new Rule();
//		r4b.setCondition(Condition.TO);
//		r4b.setValue("uns.ac.rs");
//		r4b.setOperation(Operation.MOVE);
//		f4.addRule(r4b);
//		r4b.setDestination(f4);
		
//		f1.addChildFolder(f4);
//		f4.setParent(f1);
		a1.addFolder(f1);
		a1.addFolder(f2);
		a1.addFolder(f5);
//		a1.addFolder(f3);
//		a1.addFolder(f4);
//		f1.setAccount(a1);
//		f2.setAccount(a1);
//		f3.setAccount(a1);
//		f4.setAccount(a1);
		u1.addAccount(a1);
//		a1.setUser(u1);
		
		u1 = userService.save(u1);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@GetMapping(value = "/test")
	public ResponseEntity<Void> test(){
//		User u = userService.findByUsername("admin");
//		u.setPassword(passwordEncoder.encode("admin"));
//		userService.save(u);
//		u = userService.findByUsername("pera");
//		u.setPassword(passwordEncoder.encode("pera"));
//		userService.save(u);
//		---------------------------------MAIL SENDING TEST
		Account a1 = new Account();
		a1.setSmtpAddress("smtp.gmail.com");
		a1.setSmtpPort(587);
		a1.setInServerType(Account.InServerType.POP3);
		a1.setInServerAddress("pop.gmail.com");
		a1.setInServerPort(995);
		a1.setUsername("znikoolaa@gmail.com");
//		IF YOU COMMIT THIS FILE WITH YOUR REAL PASSWORD, IT IS ON YOUR OWN RISK
		a1.setPassword("************"); 
//		IF YOU COMMIT THIS FILE WITH YOUR REAL PASSWORD, IT IS ON YOUR OWN RISK
		a1.setDisplayName("Nikola Zoric");
//		
		Message m = new Message();
		m.setFrom("znikoolaa@gmail.com");
		m.setTo("Nikola Zoric <nikola.se.zoric@gmail.com>");
		m.setCc("");
		m.setBcc("");
		m.setDateTime(LocalDateTime.now());
		m.setSubject("Subject");
		m.setContent("This is content of the message");
		m.setUnread(true);
		
//		try {
//			byte[] att = Files.readAllBytes(Paths.get("C:\\Users\\nikol\\OneDrive\\Desktop\\bore.jpg"));
//			Attachment at1 = new Attachment();
//			at1.setName("My attachment");
//			at1.setData(Base64.encodeToString(att));
//			at1.setMimeType("image/jpeg");
//			m.addAttachment(at1);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		a1.addMessage(m);
		
		boolean successful = MailUtil.sendMessage(m);
		
		if (!successful)
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
//		--------------------------------------------------------------------------------------------
//		--------------------------------MAIL CHECKING TEST
//		MailUtil.syncMessages(a1);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
