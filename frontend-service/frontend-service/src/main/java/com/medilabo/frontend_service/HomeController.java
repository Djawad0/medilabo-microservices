package com.medilabo.frontend_service;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;



@Controller
public class HomeController {
	
	 private final RestTemplate restTemplate = new RestTemplate();
	    private final String BACKEND_URL = "http://localhost:8080/info/api/patient";

	    @GetMapping("/")
	    public String home(@RequestParam(required = false) String nom,
	                       @RequestParam(required = false) String mode,
	                       Model model) {
	    	if (nom != null && !nom.trim().isEmpty() && nom.matches("^[A-Za-zÀ-ÿ\\s\\-]+$")) {
	            try {
	                ResponseEntity<List<Patient>> response = restTemplate.exchange(
	                        BACKEND_URL + "/nom/" + nom,
	                        HttpMethod.GET,
	                        null,
	                        new ParameterizedTypeReference<>() {}
	                );
	                List<Patient> patients = response.getBody();
	                if (patients != null && !patients.isEmpty()) {
	                    model.addAttribute("patients", patients);
	                    model.addAttribute("mode", mode);
	                } else {
	                    model.addAttribute("message", "Aucun patient trouvé.");
	                }
	            } catch (Exception e) {
	                model.addAttribute("message", "Erreur lors de la recherche.");
	            }
	        } else if (nom != null) {
	            model.addAttribute("message", "Nom invalide : uniquement des lettres autorisées.");
	        }

	        return "index";
	    }

	   
	    @PostMapping("/modifier/{id}")
	    public String modifierPatient(@PathVariable int id, @ModelAttribute Patient patient, Model model) {
	        restTemplate.put(BACKEND_URL + "/" + id, patient);
	        model.addAttribute("patient", patient);
	        model.addAttribute("readonly", true);
	        model.addAttribute("message", "Patient mis à jour avec succès !");
	        return "index";
	    }
	    
	    @GetMapping("/ajouter")
	    public String afficherFormAjout(Model model) {
	        model.addAttribute("patient", new Patient());
	        return "ajout";
	    }

	    @PostMapping("/ajouter")
	    public String ajouterPatient(@ModelAttribute Patient patient, Model model) {
	        RestTemplate restTemplate = new RestTemplate();
	        String backendUrl = "http://localhost:8080/info/api/patient";
	        restTemplate.postForObject(backendUrl, patient, Patient.class);
	        return "redirect:/"; 
	    }

}
