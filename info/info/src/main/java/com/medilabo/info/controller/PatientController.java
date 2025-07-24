package com.medilabo.info.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medilabo.info.model.Patient;
import com.medilabo.info.service.PatientService;


/**
 * Contrôleur REST permettant de gérer les opérations liées aux patients.
 * Il expose des endpoints pour rechercher, ajouter et mettre à jour les patients.
 */

@RestController
@RequestMapping("/api/patient")
@CrossOrigin(origins = "http://localhost:8082")
public class PatientController {
	
	@Autowired
	private PatientService patientService;
	
	/**
     * Recherche les patients par leur nom.
     *
     * @param nom Le nom du patient à rechercher.
     * @return Une réponse contenant la liste des patients trouvés, ou un code 404 si aucun n'est trouvé.
     */
	
	@GetMapping("/nom/{nom}")
	public ResponseEntity<List<Patient>> findByNom(@PathVariable String nom) {
		  List<Patient> patients = patientService.findByNom(nom);
	        if (patients.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(patients);
	        }
	        return ResponseEntity.ok(patients);
	}
	
	 /**
     * Recherche un patient par son identifiant unique.
     *
     * @param id L'identifiant du patient.
     * @return Une réponse contenant le patient s'il est trouvé, ou un code 404 sinon.
     */
	
	@GetMapping("/patId/{id}")
	public ResponseEntity<Optional<Patient>> findById(@PathVariable int id) {
		  Optional<Patient> patients = patientService.findById(id);
	        if (patients.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(patients);
	        }
	        return ResponseEntity.ok(patients);
	}
	
	
	/**
     * Met à jour les informations d'un patient existant.
     *
     * @param id L'identifiant du patient à mettre à jour.
     * @param updatedPersonne L'objet Patient contenant les nouvelles données.
     * @return Une réponse avec le patient mis à jour, ou un code 404 si le patient n'existe pas.
     */

	@PutMapping("/{id}")
	public ResponseEntity<?> updatePatient(@PathVariable int id, @RequestBody Patient updatedPersonne) {
	    return patientService.updatePersonne(id, updatedPersonne)
	            .map(ResponseEntity::ok)
	            .orElse(ResponseEntity.notFound().build());
	}
	
	/**
     * Ajoute un nouveau patient à la base de données.
     *
     * @param patient L'objet Patient à ajouter.
     * @return Une réponse contenant le patient enregistré avec un code 201 CREATED.
     */
	
	@PostMapping
	public ResponseEntity<Patient> ajouterPatient(@RequestBody Patient patient) {
		Patient saved = patientService.save(patient);
	    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

}
