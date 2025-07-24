package com.medilabo.api_gateway.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.medilabo.api_gateway.model.User;
import com.medilabo.api_gateway.repository.UserRepository;


/**
 * Service d'authentification et d'enregistrement des utilisateurs.
 * 
 * Fournit des méthodes pour authentifier un utilisateur et pour
 * enregistrer un nouvel utilisateur avec un mot de passe encodé.
 */

@Service
public class AuthService {
	
	 private final UserRepository userRepository;
	    private final PasswordEncoder passwordEncoder;

	    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
	        this.userRepository = userRepository;
	        this.passwordEncoder = passwordEncoder;
	    }

	    
	    /**
	     * Authentifie un utilisateur en fonction de son nom d'utilisateur et mot de passe.
	     * 
	     * @param username Le nom d'utilisateur fourni.
	     * @param password Le mot de passe fourni.
	     * @return Un Optional contenant l'utilisateur authentifié si les identifiants sont valides, sinon vide.
	     */
	    public Optional<User> authenticate(String username, String password) {
	        return userRepository.findByUsername(username)
	                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
	    }

	    
	    /**
	     * Enregistre un nouvel utilisateur avec un mot de passe encodé.
	     * 
	     * @param username Le nom d'utilisateur souhaité.
	     * @param rawPassword Le mot de passe.
	     * @return true si l'utilisateur a bien été enregistré, false si le nom d'utilisateur existe déjà.
	     */
	    public boolean register(String username, String rawPassword) {
	        if (userRepository.findByUsername(username).isPresent()) {
	            return false; 
	        }

	        User user = new User();
	        user.setUsername(username);
	        user.setPassword(passwordEncoder.encode(rawPassword));
	        userRepository.save(user);
	        return true;
	    }

}
