package com.security.service;

import java.security.Key;
import java.util.Base64.Decoder;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.id.insert.GetGeneratedKeysDelegate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private String secretKey="";

	public JwtService() {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk = keyGen.generateKey();
			secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String generateToken(String userName) {

		Map<String, Object> claims = new HashMap<>();
		claims.put("userName", userName);
//		String compact = Jwts.builder().claims().add(claims).subject(userName)
//				.issuedAt(new Date(System.currentTimeMillis()))
//				.expiration(new Date(System.currentTimeMillis() + 60 * 60 * 10)).and().signWith(getKey()).compact();
//
//		return compact;
		
		String token = Jwts.builder().claims().add(claims).subject(userName)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 1000*60 * 60 *10 )).and().signWith(getKey()).compact();

		return token;

	}

	private Key getKey() {

		byte[] decode = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(decode);
	}

	private Claims extractAllClaims(String token) {
		Claims claims = Jwts.parser().verifyWith(decryptKey(secretKey)).build().parseSignedClaims(token).getPayload();
		return claims;
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	private SecretKey decryptKey(String secratekey2) {

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);

	}

	private Date extractExpairation(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public String extractNamString(String token) {
		Claims claims = extractAllClaims(token);
		return claims.get("userName", String.class); // Extracts custom claim
	}

	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Boolean validatToken(String tokenString, UserDetails userByUsername) {
		String userName = extractUserName(tokenString);
		Boolean iseExpired = isTokenExpired(tokenString);

		if (userName.equals(userByUsername.getUsername()) && !iseExpired) {
			return true;
		}
		return false;
	}

	private Boolean isTokenExpired(String token) {
		Date expiredDate = extractExpairation(token);
		return expiredDate.before(new Date());
	}

}
