package osa.projekat.sf1528.emailClient.mail;

import java.util.Properties;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import osa.projekat.sf1528.emailClient.model.Account;
import osa.projekat.sf1528.emailClient.model.Attachment;
import osa.projekat.sf1528.emailClient.model.Message;
import osa.projekat.sf1528.emailClient.util.Base64;

public class MailUtil {
	
	public static JavaMailSenderImpl getJavaMailSender(Account account) {
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
				
				ByteArrayDataSource dataSource = new ByteArrayDataSource(Base64.decode(attachment.getData()), attachment.getMimeType());
	
				helper.addAttachment(attachment.getName(), dataSource);
			}
			
			mailSender.send(mimeMessage);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
