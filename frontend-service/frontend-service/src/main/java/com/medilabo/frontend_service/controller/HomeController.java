package com.medilabo.frontend_service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.medilabo.frontend_service.model.AuthRequest;
import com.medilabo.frontend_service.model.Note;
import com.medilabo.frontend_service.model.Patient;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Controller
public class HomeController {
	
	// Instance de RestTemplate pour effectuer des appels HTTP REST vers les microservices
	 private final RestTemplate restTemplate = new RestTemplate();

	// URL de base du gateway API pour accéder aux différents services
	 private final String GATEWAY_URL = "http://api-gateway:8080";

	// URL des différentes API en passant par la gateway
	 private final String PATIENT_URL = GATEWAY_URL + "/info/api/patient";
	 private final String NOTE_URL = GATEWAY_URL + "/note/api/notes";
	 private final String ANALYSE_URL = GATEWAY_URL + "/analyse/api/analyse/evaluate";
	 private final String AUTH_URL = GATEWAY_URL + "/auth/login";

	/*
	 *  Méthode gérant la page d'accueil "/"
	 *  Permet de rechercher des patients par nom 
	 */
	  
	    @GetMapping("/")
	    public String home(@RequestParam(required = false) String nom,
	                       @RequestParam(required = false) String mode,
	                       HttpServletRequest request,              
	                       HttpServletResponse servletResponse,
	                       Model model) {
	    	
	    	servletResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    	servletResponse.setHeader("Pragma", "no-cache");
	    	servletResponse.setDateHeader("Expires", 0);

	    	// Récupération du token JWT depuis les cookies
	        String jwt = getJwtFromCookies(request);
	        if (jwt == null) return "redirect:/login";

	     // Création des headers avec le JWT pour authentification
	        HttpHeaders headers = createHeadersWithJwt(jwt);
	        HttpEntity<?> entity = new HttpEntity<>(headers);

	        if (nom != null && !nom.trim().isEmpty() && nom.matches("^[A-Za-zÀ-ÿ\\s\\-]+$")) {
	            try {
	                ResponseEntity<List<Patient>> response = restTemplate.exchange(
	                        PATIENT_URL + "/nom/" + nom,
	                        HttpMethod.GET,
	                        entity,
	                        new ParameterizedTypeReference<>() {}
	                );

	                List<Patient> patients = response.getBody();
	                model.addAttribute("patients", patients);
	                model.addAttribute("mode", mode);

	            
	                Map<Integer, String> statuts = new HashMap<>();
	                for (Patient p : patients) {
	                    try {
	                        ResponseEntity<String> res = restTemplate.exchange(
	                                ANALYSE_URL + "/" + p.getId(),
	                                HttpMethod.GET,
	                                entity,
	                                String.class
	                        );
	                        statuts.put(p.getId(), res.getBody());
	                    } catch (Exception e) {
	                        statuts.put(p.getId(), "Inconnu");
	                    }
	                }
	                model.addAttribute("statuts", statuts);

	            } catch (Exception e) {
	                model.addAttribute("message", "Erreur lors de la recherche de patients.");
	            }
	        } else if (nom != null) {
	            model.addAttribute("message", "Nom invalide : uniquement des lettres autorisées.");
	        }

	        return "index";
	    }

	/*
	 * Méthode pour modifier un patient existant
	 */
	    @PostMapping("/modifier/{id}")
	    public String modifierPatient(@PathVariable int id,
	                                   @ModelAttribute Patient patient,
	                                   HttpServletRequest request,
	                                   HttpServletResponse servletResponse,
	                                   Model model) {
	    	
	    	servletResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    	servletResponse.setHeader("Pragma", "no-cache");
	    	servletResponse.setDateHeader("Expires", 0);
	    	
	    	
	        String jwt = getJwtFromCookies(request);
	        if (jwt == null) return "redirect:/login";

	        HttpHeaders headers = createHeadersWithJwt(jwt);
	        HttpEntity<Patient> entity = new HttpEntity<>(patient, headers);

	        restTemplate.exchange(PATIENT_URL + "/" + id, HttpMethod.PUT, entity, Void.class);

	        model.addAttribute("message", "Patient mis à jour avec succès !");
	        return "redirect:/";
	    }

	 // Affiche le formulaire d'ajout d'un nouveau patient
	    @GetMapping("/ajouter")
	    public String afficherFormAjout(Model model, HttpServletResponse servletResponse) {
	    	
	    	servletResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    	servletResponse.setHeader("Pragma", "no-cache");
	    	servletResponse.setDateHeader("Expires", 0);
	    	
	        model.addAttribute("patient", new Patient());
	        return "ajout";
	    }

	 // Gère l'ajout d'un patient
	    @PostMapping("/ajouter")
	    public String ajouterPatient(@ModelAttribute Patient patient,
	    		 HttpServletResponse servletResponse,
	                                 HttpServletRequest request) {
	    	
	    	servletResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    	servletResponse.setHeader("Pragma", "no-cache");
	    	servletResponse.setDateHeader("Expires", 0);
	    	
	        String jwt = getJwtFromCookies(request);
	        if (jwt == null) return "redirect:/login";

	        HttpHeaders headers = createHeadersWithJwt(jwt);
	        HttpEntity<Patient> entity = new HttpEntity<>(patient, headers);

	        restTemplate.postForEntity(PATIENT_URL, entity, Patient.class);
	        return "redirect:/";
	    }

	 // Affiche l'historique des notes pour un patient donné
	    @GetMapping("/historique/{patId}")
	    public String voirHistorique(@PathVariable int patId,
	                                 HttpServletRequest request,
	                                 HttpServletResponse servletResponse,
	                                 Model model) {
	    	
	    	servletResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    	servletResponse.setHeader("Pragma", "no-cache");
	    	servletResponse.setDateHeader("Expires", 0);
	    	
	        String jwt = getJwtFromCookies(request);
	        if (jwt == null) return "redirect:/login";

	        HttpHeaders headers = createHeadersWithJwt(jwt);
	        HttpEntity<?> entity = new HttpEntity<>(headers);

	        ResponseEntity<List<Note>> response = restTemplate.exchange(
	                NOTE_URL + "/" + patId,
	                HttpMethod.GET,
	                entity,
	                new ParameterizedTypeReference<>() {}
	        );

	        model.addAttribute("notes", response.getBody());
	        model.addAttribute("patId", patId);
	        return "historique";
	    }

	 // Ajoute une note à un patient
	    @PostMapping("/historique/{patId}")
	    public String ajouterNote(@PathVariable int patId,
	                              @RequestParam String note,
	                              HttpServletResponse servletResponse,
	                              HttpServletRequest request) {
	    	
	    	servletResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    	servletResponse.setHeader("Pragma", "no-cache");
	    	servletResponse.setDateHeader("Expires", 0);
	    	
	        String jwt = getJwtFromCookies(request);
	        if (jwt == null) return "redirect:/login";

	        String nomPatient = getNomDuPatientDepuisAPI(patId, jwt);

	        Note newNote = new Note(patId, note);
	        newNote.setPatient(nomPatient);

	        HttpHeaders headers = createHeadersWithJwt(jwt);
	        HttpEntity<Note> entity = new HttpEntity<>(newNote, headers);

	        restTemplate.postForEntity(NOTE_URL, entity, Note.class);
	        return "redirect:/historique/" + patId;
	    }

	   // Méthode pour récupérer le nom du patient
	    private String getNomDuPatientDepuisAPI(int patId, String jwt) {
	    	
	    	
	        HttpHeaders headers = createHeadersWithJwt(jwt);
	        HttpEntity<?> entity = new HttpEntity<>(headers);

	        try {
	            ResponseEntity<Patient> res = restTemplate.exchange(
	                    PATIENT_URL + "/patId/" + patId,
	                    HttpMethod.GET,
	                    entity,
	                    Patient.class
	            );
	            return res.getBody().getNom();
	        } catch (Exception e) {
	            return "Inconnu";
	        }
	    }

	 // Affiche le formulaire de login
	    @GetMapping("/login")
	    public String showLoginForm(HttpServletResponse servletResponse) {
	    	servletResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    	servletResponse.setHeader("Pragma", "no-cache");
	    	servletResponse.setDateHeader("Expires", 0);
	        return "login";
	    }

	 // Gère la connexion (authentification) de l'utilisateur
	    @PostMapping("/login")
	    public String login(@RequestParam String username,
	                        @RequestParam String password,
	                        HttpServletResponse response) {

	        AuthRequest loginRequest = new AuthRequest(username, password);
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        HttpEntity<AuthRequest> entity = new HttpEntity<>(loginRequest, headers);

	        try {
	            ResponseEntity<String> tokenResponse = restTemplate.postForEntity(
	                    AUTH_URL, entity, String.class);

	            if (tokenResponse.getStatusCode().is2xxSuccessful()) {
	                String token = tokenResponse.getBody();

	                ResponseCookie cookie = ResponseCookie.from("jwt", token)
	                        .httpOnly(true)
	                        .path("/")
	                        .sameSite("Strict")
	                        .build();

	                response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	                return "redirect:/";
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return "redirect:/login?error";
	    }

	 // Méthode pour récupérer le token JWT dans les cookies de la requête
	    private String getJwtFromCookies(HttpServletRequest request) {
	        if (request.getCookies() == null) return null;
	        for (Cookie c : request.getCookies()) {
	            if ("jwt".equals(c.getName())) return c.getValue();
	        }
	        return null;
	    }

	 // Création des headers HTTP avec authentification Bearer JWT et content type JSON
	    private HttpHeaders createHeadersWithJwt(String jwt) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.setBearerAuth(jwt);
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        return headers;
	    }
	    
	 // Affiche le formulaire d'inscription utilisateur
	    @GetMapping("/register")
	    public String showRegisterForm() {
	        return "registration";
	    }

	 // Gère l'inscription d'un nouvel utilisateur
	    @PostMapping("/register")
	    public String register(@RequestParam String username,
	                           @RequestParam String password) {

	        AuthRequest request = new AuthRequest(username, password);
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        HttpEntity<AuthRequest> entity = new HttpEntity<>(request, headers);

	        try {
	            ResponseEntity<String> response = restTemplate.postForEntity(
	                    GATEWAY_URL + "/auth/register", entity, String.class);

	            if (response.getStatusCode().is2xxSuccessful()) {
	                return "redirect:/login";
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return "redirect:/register?error";
	    }

	 // Déconnexion : suppression du cookie JWT et invalidation de session
	    @GetMapping("/logout")
	    public String logout(HttpServletRequest request, HttpServletResponse response) {
	    	Cookie cookie = new Cookie("jwt", null);
	        cookie.setPath("/");
	        cookie.setMaxAge(0);
	        cookie.setHttpOnly(true);
	        response.addCookie(cookie);
	        
	        request.getSession().invalidate();
	        
	        return "redirect:/login?logout";
	    }



}
