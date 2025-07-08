package com.medilabo.info.controller;


import java.util.List;

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

@RestController
@RequestMapping("/api/patient")
@CrossOrigin(origins = "http://localhost:8082")
public class PatientController {
	
	@Autowired
	private PatientService patientService;
	
	@GetMapping("/nom/{nom}")
	public ResponseEntity<List<Patient>> findByNom(@PathVariable String nom) {
		  List<Patient> patients = patientService.findByNom(nom);
	        if (patients.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(patients);
	        }
	        return ResponseEntity.ok(patients);
	}

	
	@PutMapping("/{id}")
	public ResponseEntity<?> updatePatient(@PathVariable int id, @RequestBody Patient updatedPersonne) {
	    return patientService.updatePersonne(id, updatedPersonne)
	            .map(ResponseEntity::ok)
	            .orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public ResponseEntity<Patient> ajouterPatient(@RequestBody Patient patient) {
		Patient saved = patientService.save(patient);
	    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

}
