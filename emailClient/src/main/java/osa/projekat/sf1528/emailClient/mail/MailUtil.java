package osa.projekat.sf1528.emailClient.mail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StreamUtils;

import osa.projekat.sf1528.emailClient.model.Account;
import osa.projekat.sf1528.emailClient.model.Attachment;
import osa.projekat.sf1528.emailClient.model.Folder;
import osa.projekat.sf1528.emailClient.model.Message;
import osa.projekat.sf1528.emailClient.model.Rule;
import osa.projekat.sf1528.emailClient.service.FolderService;
import osa.projekat.sf1528.emailClient.service.MessageService;
import osa.projekat.sf1528.emailClient.util.Base64;
import osa.projekat.sf1528.emailClient.util.FilesUtil;

public class MailUtil {
	
	private static JavaMailSenderImpl getJavaMailSender(Account account) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(account.getSmtpAddress());
		mailSender.setPort(account.getSmtpPort());
		
		mailSender.setUsername(account.getUsername());
		mailSender.setPassword(account.getPassword());
		
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");
		
		return mailSender;
	}
	
	public static boolean sendMessage(Message message) {
		try {
			JavaMailSenderImpl mailSender = getJavaMailSender(message.getAccount());
			
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			
			helper.setFrom(message.getFrom());
			helper.setTo(message.getTo());
			if (message.getCc() != null && !message.getCc().equals(""))
				helper.setCc(message.getCc());
			if (message.getBcc() != null && !message.getBcc().equals(""))
				helper.setBcc(message.getBcc());
			helper.setSubject(message.getSubject());
			helper.setText(message.getContent());
			
			for (Attachment attachment : message.getAttachments()) {
				if (attachment.getDataPath() != null && !attachment.getDataPath().isEmpty()) {
					byte[] attachmentData = FilesUtil.readBytes(attachment.getDataPath());
					if (attachmentData != null) {
						ByteArrayDataSource dataSource = new ByteArrayDataSource(attachmentData, attachment.getMimeType());
						helper.addAttachment(attachment.getName(), dataSource);
					}
				}
				
	
			}
			
			mailSender.send(mimeMessage);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static Map<String, Object> syncMessages(Account account) {
		LocalDateTime lastSync = account.getLastMailSync();
		try {
			Session session = null;
			Store store = null;
			if (account.getInServerType() == Account.InServerType.POP3) {
				Properties props = new Properties();
				props.put("mail.store.protocol", "pop3");
				props.put("mail.pop3.host", account.getInServerAddress());
				props.put("mail.pop3.port", account.getInServerPort());
				props.put("mail.pop3.starttls.enable", "true");
				session = Session.getDefaultInstance(props);
				store = session.getStore("pop3s");
			}
			else if (account.getInServerType() == Account.InServerType.IMAP) {
				Properties props = new Properties();
				props.put("mail.store.protocol", "imap");
				props.put("mail.imap.host", account.getInServerAddress());
				props.put("mail.imap.port", account.getInServerPort());
				props.put("mail.imap.starttls.enable", "true");
				session = Session.getDefaultInstance(props);
				store = session.getStore("imap");
			}
			
			store.connect(account.getInServerAddress(), account.getUsername(), account.getPassword());
			
			account.setLastMailSync(LocalDateTime.now());
			javax.mail.Folder inboxFolder = store.getFolder("INBOX");
			inboxFolder.open(javax.mail.Folder.READ_ONLY);
			
			javax.mail.Message[] messages = inboxFolder.getMessages();
			Map<String, Object> result = new HashMap<String, Object>();
			List<Message> myMessages = new ArrayList<Message>();
			
			if (lastSync != null) {
				result.put("mode", "sync");
				for (int i = (messages.length - 1); i > -1; i--) {
					javax.mail.Message message = messages[i];
					if ( LocalDateTime.ofInstant(message.getSentDate().toInstant(), 
								ZoneId.systemDefault()).isAfter(lastSync)) {
						try {
							Message myMessage = javaxMessageToMyMessage(message);
							myMessage.setAccount(account);
							//					account.addMessage(myMessage);
							//					storeInAccountsInboxFolder(myMessage, account);
							myMessages.add(myMessage);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					else {
//						break;
					}
				}
			}
			else {
				result.put("mode", "startData");
				for (int i = (messages.length - 1); i > -1; i--) {
					javax.mail.Message message = messages[i];
					try {
						Message myMessage = javaxMessageToMyMessage(message);
						myMessage.setAccount(account);
						//				account.addMessage(myMessage);
						//				storeInAccountsInboxFolder(myMessage, account);
						myMessages.add(myMessage);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			inboxFolder.close(false);
			store.close();
			
			result.put("messages", myMessages);
			return result;
			
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		
		account.setLastMailSync(lastSync);
		return null;
	}
	
	public static Message javaxMessageToMyMessage(javax.mail.Message javaxMessage) throws MessagingException, IOException {
		Message message = new Message();
		Address[] addresses;
		StringBuilder sb;
		try {
			message.setFrom(javaxMessage.getFrom()[0].toString().split("<")[1].split(">")[0]);
		} catch (Exception e) {
			message.setFrom(javaxMessage.getFrom()[0].toString());
		}
		
		if ((addresses = javaxMessage.getRecipients(javax.mail.Message.RecipientType.TO)) != null) {
			sb = new StringBuilder("");
			for (int i = 0; i < addresses.length; i++) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(addresses[i].toString());
			}
			message.setTo(sb.toString());
		}
		
		if ((addresses = javaxMessage.getRecipients(javax.mail.Message.RecipientType.CC)) != null) {
			sb = new StringBuilder("");
			for (int i = 0; i < addresses.length; i++) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(addresses[i].toString());
			}
			message.setCc(sb.toString());
		}
		
		if ((addresses = javaxMessage.getRecipients(javax.mail.Message.RecipientType.BCC)) != null) {
			sb = new StringBuilder("");
			for (int i = 0; i < addresses.length; i++) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(addresses[i].toString());
			}
			message.setBcc(sb.toString());
		}
		
		message.setDateTime(LocalDateTime.ofInstant(javaxMessage.getSentDate().toInstant(), ZoneId.systemDefault()));
		
		message.setSubject(javaxMessage.getSubject());
		
		message.setUnread(!javaxMessage.isSet(Flags.Flag.SEEN));
		
		if (javaxMessage.getContentType().toLowerCase().contains("multipart")) {
			Multipart mp = (Multipart) javaxMessage.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				proccessPart(mp.getBodyPart(i), message);
			}
		}
		else if (javaxMessage.getContentType().toLowerCase().contains("text")) {
			message.setContent(javaxMessage.getContent().toString());
		}
		
		return message;
	}
	
	private static void proccessPart(Part part, Message message) throws MessagingException, IOException {
		if (part.getContentType().toLowerCase().contains("text")) {
			if (message.getContent() != null)
				message.setContent(message.getContent() + part.getContent().toString());
			else
				message.setContent(part.getContent().toString());
		}
		else if (part.getContentType().toLowerCase().contains("multipart")) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				proccessPart(mp.getBodyPart(i), message);
			}
//			if (part.getContent() instanceof MimeMultipart) {
//				MimeMultipart mp = (MimeMultipart) part.getContent();
//				message.setContent(message.getContent() + mp.getBodyPart(0).getContent().toString());
//			}
		}
		else {
			if (part.getDisposition() != null && part.getDisposition().equalsIgnoreCase(Part.ATTACHMENT) 
					&& part.getFileName() != null && !part.getFileName().isEmpty()) {
				
//				------ 1 ------------
//				byte[] attachmentBytes = new byte[part.getSize()];
//				IOUtils.readFully(part.getInputStream(), attachmentBytes);
//				String encodedAttachment = Base64.encodeToString(attachmentBytes);
//				----------------------
//				
//				------ 2 -------------
//				ByteArrayOutputStream os = new ByteArrayOutputStream();
//				byte[] attachmentBytes = new byte[8192];
//				
//				int read;
//				while ((read = part.getInputStream().read(attachmentBytes)) != -1)
//					os.write(attachmentBytes, 0, read);
//				
//				String encodedAttachment = Base64.encodeToString(os.toByteArray());
//				
//				
//				----- 3 ----------------
				byte[] attachmentBytes = StreamUtils.copyToByteArray(part.getInputStream());
				String path = String.format("./data/attachments/%d", new Date().hashCode());
				
				if (FilesUtil.saveBytes(attachmentBytes, path)) {
					Attachment attachment = new Attachment();
					attachment.setName(part.getFileName());
					attachment.setMimeType(part.getContentType().split(";")[0]);
					attachment.setDataPath(path);
					message.addAttachment(attachment);
				}
				
			}
		}
	}
	
	public static void storeMessagesInFolders(List<Message> messages, Account account) {
		
		doRulesForIncomingMessages(messages, account);
		
		List<Message> inboxMessages = new ArrayList<Message>();
		List<Message> sentMessages = new ArrayList<Message>();
		
		for (Message message : messages) {
			if (message.getFolder() == null && message.getAccount() != null) {
				if (message.getTo().contains(account.getUsername())
						|| (message.getCc() != null && message.getCc().contains(account.getUsername()) )
						|| (message.getBcc() != null && message.getBcc().contains(account.getUsername())) )
					inboxMessages.add(message);
				if (message.getFrom().contains(account.getUsername()))
					sentMessages.add(message);
			}
		}
		
		storeMessagesInAccountsInboxFolder(inboxMessages, account);
		storeMessagesInAccountsSentFolder(sentMessages, account);
		
	}
	
	public static void doRulesForAllMessages(List<Rule> rules, MessageService messageService) {
		if (rules.size() > 0) {
			List<Message> messages = new ArrayList<Message>();
			messages.addAll(rules.get(0).getDestination().getAccount().getMessages());
			
			for (Message message : messages) {
				for (Rule rule : rules) {
					Message m = rule.doRule(message);
					if (m != null && m.getAccount() == null) {
						messageService.remove(message.getId());
					}
					else if (m != null) {
						message = messageService.save(message);
					}
				}
			}
		}
	}
	
	public static void doRulesForIncomingMessages(List<Message> messages, Account account) {
		List<Folder> rootFolders = new ArrayList<Folder>();
		for (Folder folder : account.getFolders()) {
			if (folder.getParent() == null) {
				rootFolders.add(folder);
			}
		}
		
		for (Message message : messages) {
			doRulesForMessage(message, rootFolders);
		}
	}
	
	public static void doRulesForMessage(Message message, List<Folder> folders) {
		List<Folder> nextLayerFolders = new ArrayList<Folder>();
		for (Folder folder : folders) {
			for (Rule rule : folder.getRules()) {
				Message m = rule.doRule(message);
				if (m != null && m.getAccount() == null )
					return;
			}
			nextLayerFolders.addAll(folder.getChildFolders());
		}
		
		if (nextLayerFolders.size() > 0)
			doRulesForMessage(message, nextLayerFolders);
	}
	
	public static void storeMessagesInAccountsInboxFolder(List<Message> messages, Account account) {
		for (Folder folder : account.getFolders()) {
			if (folder.getName().equalsIgnoreCase("inbox")) {
				for (Message message : messages)
					folder.addMessage(message);
				break;
			}
		}
	}
	
	public static void storeMessagesInAccountsSentFolder(List<Message> messages, Account account) {
		for (Folder folder : account.getFolders()) {
			if (folder.getName().equalsIgnoreCase("sent")) {
				for (Message message : messages)
					folder.addMessage(message);
				break;
			}
		}
	}

}
