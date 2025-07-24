package com.medilabo.api_gateway.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Classe qui fournit des méthodes pour générer, valider et extraire des informations des tokens JWT.
 */

@Component
public class JwtUtil {
	
	@Value("${jwt.secret}")
    private String jwtSecret;

	    public String generateToken(String username) {
	        return Jwts.builder()
	                .setSubject(username)
	                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
	                .signWith(getKey(), SignatureAlgorithm.HS256)
	                .compact();
	    }

	    public boolean validateToken(String token) {
	        try {
	            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
	            return true;
	        } catch (JwtException e) {
	            return false;
	        }
	    }

	    public String extractUsername(String token) {
	        return Jwts.parserBuilder().setSigningKey(getKey()).build()
	                .parseClaimsJws(token).getBody().getSubject();
	    }
	    
	    private Key getKey() {
	        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
	    }


}
