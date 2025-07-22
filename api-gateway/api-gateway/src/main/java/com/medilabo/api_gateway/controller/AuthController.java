package com.medilabo.api_gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medilabo.api_gateway.model.LoginRequest;
import com.medilabo.api_gateway.security.JwtUtil;
import com.medilabo.api_gateway.service.AuthService;

/**
 * Contrôleur REST pour la gestion de l'authentification et de l'enregistrement des utilisateurs.
 */

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	 private final JwtUtil jwtUtil;
	    private final AuthService authService;
	    
	    /**
		 * Constructeur avec injection des dépendances JwtUtil et AuthService.
		 * 
		 * @param jwtUtil Utilitaire pour la gestion des JWT.
		 * @param authService Service d'authentification des utilisateurs.
		 */

	    public AuthController(JwtUtil jwtUtil, AuthService authService) {
	        this.jwtUtil = jwtUtil;
	        this.authService = authService;
	    }

	    
	    /**
		 * Endpoint pour la connexion d'un utilisateur.
		 * 
		 * @param request Objet contenant le nom d'utilisateur et le mot de passe.
		 * @return Un JWT si les identifiants sont valides, sinon une erreur 401.
		 */
	    @PostMapping("/login")
	    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
	        return authService.authenticate(request.getUsername(), request.getPassword())
	                .map(user -> ResponseEntity.ok(jwtUtil.generateToken(user.getUsername())))
	                .orElse(ResponseEntity.status(401).body("Invalid credentials"));
	    }
	    
	    /**
		 * Endpoint pour l'enregistrement d'un nouvel utilisateur.
		 * 
		 * @param request Objet contenant le nom d'utilisateur et le mot de passe.
		 * @return Message de succès ou d'erreur si le nom d'utilisateur existe déjà.
		 */

	    @PostMapping("/register")
	    public ResponseEntity<String> register(@RequestBody LoginRequest request) {
	        boolean created = authService.register(request.getUsername(), request.getPassword());
	        if (created) {
	            return ResponseEntity.ok("User registered successfully");
	        } else {
	            return ResponseEntity.status(400).body("Username already exists");
	        }
	    }
	}

	

