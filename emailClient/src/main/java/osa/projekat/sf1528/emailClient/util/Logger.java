package osa.projekat.sf1528.emailClient.util;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import osa.projekat.sf1528.emailClient.dto.AccountDTO;
import osa.projekat.sf1528.emailClient.dto.ContactDTO;
import osa.projekat.sf1528.emailClient.dto.FolderDTO;
import osa.projekat.sf1528.emailClient.dto.MessageDTO;
import osa.projekat.sf1528.emailClient.dto.MessageDataDTO;
import osa.projekat.sf1528.emailClient.dto.RuleDTO;
import osa.projekat.sf1528.emailClient.dto.UserDTO;

@Aspect
@Component
public class Logger {
	
	org.slf4j.Logger logger = LoggerFactory.getLogger(Logger.class);
	
	@AfterReturning(value = "execution(* osa..AuthenticationController.registerUser(..))", returning = "returnValue")
	public void logAfterRegistration(JoinPoint joinPoint, ResponseEntity<UserDTO> returnValue) {
		UserDTO user = (UserDTO) joinPoint.getArgs()[0];
		if (returnValue.getStatusCode() == HttpStatus.CREATED)
			logger.info("New user registered with username: {}\n", user.getUsername());
		else if (returnValue.getStatusCode() == HttpStatus.IM_USED)
			logger.error("New user tried to register with existing username: {}\n", user.getUsername());
	}
	
	@AfterReturning(value = "execution(* osa..UserController.updateUser(..))", returning = "returnValue")
	public void logAfterUserUpdate(JoinPoint joinPoint, ResponseEntity<UserDTO> returnValue) {
		UserDTO user = (UserDTO) joinPoint.getArgs()[0];
		if (returnValue.getStatusCode() == HttpStatus.OK)
			logger.info("User updated: {}\n", user.getUsername());
		else if (returnValue.getStatusCode() == HttpStatus.UNAUTHORIZED)
			logger.error("Unauthorized user update tried for user with username: {}\n", user.getUsername());
	}
	
	@AfterReturning(value = "execution(* osa..UserController.deleteuser(..))", returning = "returnValue")
	public void logAfterUserDelete(JoinPoint joinPoint, ResponseEntity<Void> returnValue) {
		Long id = (Long) joinPoint.getArgs()[0];
		if (returnValue.getStatusCode() == HttpStatus.OK)
			logger.info("User with id {} deleted\n", id);
		else if (returnValue.getStatusCode() == HttpStatus.UNAUTHORIZED)
			logger.error("Unauthorized user delete tried for user with id: {}\n", id);
	}
	
	@AfterReturning(value = "execution(* osa..MessageController.saveMessage(..))", returning = "returnValue")
	public void logAfterMessageSent(JoinPoint joinPoint, ResponseEntity<MessageDTO> returnValue) {
		MessageDTO message = returnValue.getBody();
		Long accountId = (Long) joinPoint.getArgs()[1];
		if (returnValue.getStatusCode() == HttpStatus.CREATED)
			logger.info("Message with id {} sent\n", message.getId());
		else if (returnValue.getStatusCode() == HttpStatus.UNAUTHORIZED || returnValue.getStatusCode() == HttpStatus.BAD_REQUEST)
			logger.error("Unauthorized message sending tried for accountId: {}\n", accountId);
		else if (returnValue.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
			logger.error("Error sending message for accountId: {}\n", accountId);
	}
	
	@AfterReturning(value = "execution(* osa..MessageController.updateMessageTags(..))", returning = "returnValue")
	public void logAfterMessageTagsUpdate(JoinPoint joinPoint, ResponseEntity<MessageDTO> returnValue) {
		MessageDTO message = returnValue.getBody();
		Long messageId = (Long) joinPoint.getArgs()[1];
		if (returnValue.getStatusCode() == HttpStatus.CREATED)
			logger.info("Tags updated for message with id {}\n", message.getId());
		else if (returnValue.getStatusCode() == HttpStatus.UNAUTHORIZED || returnValue.getStatusCode() == HttpStatus.BAD_REQUEST)
			logger.error("Unauthorized tags update tried for message with id {}\n", messageId);
	}
	
	@AfterReturning(value = "execution(* osa..MessageController.moveMessage(..))", returning = "returnValue")
	public void logAfterMessageMove(JoinPoint joinPoint, ResponseEntity<MessageDTO> returnValue) {
		Long messageId = (Long) joinPoint.getArgs()[0];
		Long folderId = (Long) joinPoint.getArgs()[1];
		if (returnValue.getStatusCode() == HttpStatus.OK)
			logger.info("Message with id {} moved to folder with id {}\n", messageId, folderId);
		else if (returnValue.getStatusCode() == HttpStatus.UNAUTHORIZED || returnValue.getStatusCode() == HttpStatus.BAD_REQUEST)
			logger.error("Unauthorized message move tried for message with id {}\n", messageId);
	}
	
	@AfterReturning(value = "execution(* osa..MessageController.deleteMessage(..))", returning = "returnValue")
	public void logAfterMessageDelete(JoinPoint joinPoint, ResponseEntity<Void> returnValue) {
		Long id = (Long) joinPoint.getArgs()[0];
		if (returnValue.getStatusCode() == HttpStatus.OK)
			logger.info("Message with id {} deleted\n", id);
		else if (returnValue.getStatusCode() == HttpStatus.UNAUTHORIZED)
			logger.error("Unauthorized message delete tried for message with id: {}\n", id);
		else if (returnValue.getStatusCode() == HttpStatus.NOT_FOUND)
			logger.error("Tried deleting message that doesn't exist id: {}\n", id);
	}
	
	@AfterReturning(value = "execution(* osa..AccountController.saveAccount(..))", returning = "returnValue")
	public void logAfterAccountCreate(JoinPoint joinPoint, ResponseEntity<AccountDTO> returnValue) {
		AccountDTO account = returnValue.getBody();
		Long userId = (Long) joinPoint.getArgs()[1];
		if (returnValue.getStatusCode() == HttpStatus.CREATED)
			logger.info("Account with id {} created for user with id {}\n", account.getId(), userId);
		else if (returnValue.getStatusCode() == HttpStatus.UNAUTHORIZED)
			logger.error("Unauthorized account creation tried for user with id: {}\n", userId);
	}
	
	@AfterReturning(value = "execution(* osa..AccountController.updateAccount(..))", returning = "returnValue")
	public void logAfterAccountUpdate(JoinPoint joinPoint, ResponseEntity<AccountDTO> returnValue) {
		Long id = (Long) joinPoint.getArgs()[1];
		if (returnValue.getStatusCode() == HttpStatus.CREATED)
			logger.info("Updated account with id {}\n", id);
		else if (returnValue.getStatusCode() == HttpStatus.UNAUTHORIZED || returnValue.getStatusCode() == HttpStatus.BAD_REQUEST)
			logger.error("Unauthorized account update tried for account with id: {}\n", id);
	}
	
}
