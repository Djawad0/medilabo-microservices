package com.medilabo.info.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medilabo.info.model.Patient;
import com.medilabo.info.repository.PatientRepository;

@Service
public class PatientService {
	
	@Autowired
	private PatientRepository patientRepository;
	
	 private final Pattern namePattern = Pattern.compile("^[A-Za-zÀ-ÿ\\s\\-]+$");

	    public List<Patient> findByNom(String nom) {
	        if (nom == null || !namePattern.matcher(nom).matches()) {
	            return List.of();
	        }
	        return patientRepository.findByNomIgnoreCase(nom);
	    }


	public Optional<Patient> updatePersonne(int id, Patient updatedData) {
	    return patientRepository.findById(id).map(existingPersonne -> {
	        existingPersonne.setNom(updatedData.getNom());
	        existingPersonne.setPrenom(updatedData.getPrenom());
	        existingPersonne.setDateNaissance(updatedData.getDateNaissance());
	        existingPersonne.setGenre(updatedData.getGenre());
	        existingPersonne.setAdresse(updatedData.getAdresse());
	        existingPersonne.setTelephone(updatedData.getTelephone());
	        return patientRepository.save(existingPersonne);
	    });
	}
	
	public Patient save(Patient patient) {	
		return patientRepository.save(patient);
	}
}
