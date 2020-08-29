package osa.projekat.sf1528.emailClient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import osa.projekat.sf1528.emailClient.dto.UserDTO;
import osa.projekat.sf1528.emailClient.model.User;
import osa.projekat.sf1528.emailClient.security.LoginDTO;
import osa.projekat.sf1528.emailClient.security.TokenDTO;
import osa.projekat.sf1528.emailClient.service.CustomUserDetailsService;
import osa.projekat.sf1528.emailClient.service.UserService;
import osa.projekat.sf1528.emailClient.util.TokenUtil;

@RestController
@RequestMapping(value = "api/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping(value = "/login")
	public ResponseEntity<TokenDTO> userLogin(@RequestBody LoginDTO loginDTO) {
		
		String username = loginDTO.getUsername();
		User user = userService.findByUsername(username);
		
		if (user == null)
			return new ResponseEntity<TokenDTO>(HttpStatus.NOT_FOUND);
		
		try {
			Authentication authentication = authenticationManager.
					authenticate(new UsernamePasswordAuthenticationToken(username, loginDTO.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (BadCredentialsException e) {
			return new ResponseEntity<TokenDTO>(HttpStatus.UNAUTHORIZED);
		} catch(Exception e) {
			return new ResponseEntity<TokenDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		String token = tokenUtil.generateToken(userDetails);
		
		user.setPassword(null);
		
		return new ResponseEntity<TokenDTO>(new TokenDTO(token, new UserDTO(user)), HttpStatus.OK);
	}
	
	@PostMapping(value = "/register")
	public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
		
		if (userService.findByUsername(userDTO.getUsername()) != null)
			return new ResponseEntity<UserDTO>(HttpStatus.IM_USED);
		
		User user = new User();
		user.setUsername(userDTO.getUsername());
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		
		user = userService.save(user);
		user.setPassword(null);
		return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.CREATED);
	}
	
}
