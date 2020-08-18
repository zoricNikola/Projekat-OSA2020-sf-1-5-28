package osa.projekat.sf1528.emailClient.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import osa.projekat.sf1528.emailClient.service.CustomUserDetailsService;
import osa.projekat.sf1528.emailClient.util.TokenUtil;

public class TokenAuthenticationFilter extends OncePerRequestFilter{

	private TokenUtil tokenUtil;
	
	private CustomUserDetailsService userDetailsService;
	
	public TokenAuthenticationFilter(TokenUtil tokenUtil, CustomUserDetailsService userDetailsService) {
		super();
		this.tokenUtil = tokenUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String username;
		String authToken = tokenUtil.getTokenFromRequest(request);
		
		if (authToken != null) {
			username = tokenUtil.getUsernameFromToken(authToken);
			
			if (username != null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				
				if (tokenUtil.validateToken(authToken, userDetails)) {
					TokenAuthentication authentication = new TokenAuthentication(userDetails);
					authentication.setToken(authToken);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}
		
		filterChain.doFilter(request, response);
	}

	
	
}
