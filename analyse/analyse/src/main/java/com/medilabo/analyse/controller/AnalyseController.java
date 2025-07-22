package com.medilabo.analyse.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.medilabo.analyse.model.Note;
import com.medilabo.analyse.model.Patient;
import com.medilabo.analyse.service.AnalyseService;

/**
 * Contrôleur REST pour l'analyse des risques liés aux patients.
 * 
 * Ce contrôleur interagit avec d'autres microservices via RestTemplate
 * pour récupérer les données nécessaires (Patient et Notes) et
 * utilise AnalyseService pour calculer le risque.
 */

@RestController
@RequestMapping("/api/analyse")
@CrossOrigin(origins = "*")
public class AnalyseController {
	
	@Autowired
	private AnalyseService analyseService;
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	
	/**
	 * Évalue le risque du patient identifié par son ID.
	 * 
	 * Récupère le patient et ses notes depuis les microservices externes,
	 * puis calcule le risque en utilisant AnalyseService.
	 * 
	 * @param patId L'identifiant du patient.
	 * @param authHeader Le header Authorization (token JWT) à transmettre aux autres services.
	 * @return Le niveau de risque calculé sous forme de chaîne de caractères.
	 */
	
	@GetMapping("/evaluate/{patId}")
	public String evaluateRisk(@PathVariable int patId, @RequestHeader("Authorization") String authHeader) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Authorization", authHeader);
	    HttpEntity<?> entity = new HttpEntity<>(headers);

	    ResponseEntity<Patient> patientResponse = restTemplate.exchange(
	        "http://api-gateway:8080/info/api/patient/patId/" + patId,
	        HttpMethod.GET,
	        entity,
	        Patient.class
	    );

	    ResponseEntity<List<Note>> notesResponse = restTemplate.exchange(
	        "http://api-gateway:8080/note/api/notes/" + patId,
	        HttpMethod.GET,
	        entity,
	        new ParameterizedTypeReference<List<Note>>() {}
	    );

	    Patient patient = patientResponse.getBody();
	    List<Note> notes = notesResponse.getBody();

	    return analyseService.evaluateRisk(patient, notes);
	}

}
