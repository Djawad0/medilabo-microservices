package com.medilabo.note.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Filtre de sécurité JWT qui s'exécute une fois par requête HTTP.
 * 
 * Ce filtre extrait le token JWT depuis l'en-tête Authorization,
 * vérifie sa validité et, en cas de succès, place l'utilisateur authentifié
 * dans le contexte de sécurité Spring.
 */

public class JwtFilter extends OncePerRequestFilter {
	
	 private final String jwtSecret;

	    public JwtFilter(String jwtSecret) {
	        this.jwtSecret = jwtSecret;
	    }

	    
	    /**
	     * Méthode principale exécutée pour chaque requête HTTP.
	     * 
	     * Elle vérifie la présence et la validité du token JWT dans l'en-tête Authorization.
	     * Si le token est valide, l'authentification est placée dans le contexte Spring Security.
	     * Sinon, la requête est rejetée avec un statut 401 (Unauthorized).
	     */
	    @Override
	    protected void doFilterInternal(HttpServletRequest request,
	                                    HttpServletResponse response,
	                                    FilterChain filterChain)
	            throws ServletException, IOException {

	        String authHeader = request.getHeader("Authorization");
	    
	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            String token = authHeader.substring(7);

	            try {	            	

	                Claims claims = Jwts.parserBuilder()
	                        .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
	                        .build()
	                        .parseClaimsJws(token)
	                        .getBody();

	                String username = claims.getSubject();

	                if (username != null) {
	                    UsernamePasswordAuthenticationToken authentication =
	                            new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());

	                    SecurityContextHolder.getContext().setAuthentication(authentication);
	                }

	            } catch (JwtException e) {
	                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                return;
	            }
	        } else {
	           
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            return;
	        }

	        filterChain.doFilter(request, response);
	    }

}
