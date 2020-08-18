package osa.projekat.sf1528.emailClient.util;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenUtil {
	
	@Value("sEcReT.tHaT.oNlY.wE.kNoW")
	private String SECRET_KEY;
	
	@Value("2592000000")
	private long EXPIRES_IN/* = 1000 * 60 * 60 * 24 * 30*/;
	
	@Value("Authorization")
	private String AUTH_HEADER;
	
	private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
	
	public String generateToken(UserDetails userDetails) {
		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(generateExpiriationDate())
				.signWith(SIGNATURE_ALGORITHM, SECRET_KEY).compact();
	}
	
	private Date generateExpiriationDate() {
		return new Date(System.currentTimeMillis() + EXPIRES_IN);
	}
	
	public String getUsernameFromToken(String token) {
		try {
			return getAllClaimsFromToken(token).getSubject();
		} catch (Exception e) {
			return null;
		}
	}
	
	public Date getIssuedAtFromToken(String token) {
		try {
			return getAllClaimsFromToken(token).getIssuedAt();
		} catch (Exception e) {
			return null;
		}
	}
	
	public Date getExpirationDateFromToken(String token) {
		try {
			return getAllClaimsFromToken(token).getExpiration();
		} catch (Exception e) {
			return null;
		}
	}
	
	public String getAuthHeaderFromRequest(HttpServletRequest request) {
		return request.getHeader(AUTH_HEADER);
	}
	
	public String getTokenFromRequest(HttpServletRequest request) {
		String authHeader = getAuthHeaderFromRequest(request);
		if (authHeader != null && authHeader.startsWith("Bearer "))
			return authHeader.substring(7);
		return null;
	}
	
	private boolean isTokenExpired(String token) {
		return getExpirationDateFromToken(token).before(new Date());
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	private Claims getAllClaimsFromToken(String token) {
		try {
			return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			return null;
		}
	}
}
