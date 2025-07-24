package com.medilabo.info.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.medilabo.info.model.Patient;
import com.medilabo.info.repository.PatientRepository;

/**
 * Service de gestion des patients. Fournit des méthodes pour
 * rechercher, enregistrer et mettre à jour les informations des patients.
 */

@Service
public class PatientService {
	
	@Autowired
	private PatientRepository patientRepository;
	
	 private final Pattern namePattern = Pattern.compile("^[A-Za-zÀ-ÿ\\s\\-]+$");
	 
	 
	 /**
	     * Recherche une liste de patients dont le nom correspond au nom fourni.
	     *
	     * @param nom Le nom à rechercher.
	     * @return Une liste de patients ayec le nom fourni, ou une liste vide si le nom est invalide.
	     */

	    public List<Patient> findByNom(String nom) {
	        if (nom == null || !namePattern.matcher(nom).matches()) {
	            return List.of();
	        }
	        return patientRepository.findByNomIgnoreCase(nom);
	    }

	    
	    /**
	     * Recherche un patient par son identifiant.
	     *
	     * @param id L'identifiant du patient.
	     * @return Un Optional contenant le patient s'il est trouvé, sinon un Optional vide.
	     */
		public Optional<Patient> findById(int id) {       
		        return patientRepository.findById(id);
		}
		
		
		/**
	     * Met à jour les informations d'un patient existant.
	     *
	     * @param id L'identifiant du patient à mettre à jour.
	     * @param updatedData Les nouvelles données du patient.
	     * @return Un Optional contenant le patient mis à jour s'il existe, sinon un Optional vide.
	     */

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
	
	 /**
     * Enregistre un nouveau patient dans la base de données.
     *
     * @param patient Le patient à enregistrer.
     * @return Le patient enregistré.
     */
	public Patient save(Patient patient) {	
		return patientRepository.save(patient);
	}
}
