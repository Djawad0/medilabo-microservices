package com.medilabo.analyse.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.stereotype.Service;

import com.medilabo.analyse.model.Note;
import com.medilabo.analyse.model.Patient;

/**
 * Service fournissant des fonctionnalités d'analyse médicale
 * basées sur les informations du patient et ses notes.
 */

@Service
public class AnalyseService {
	
	
	/**
	 * Liste des mots-clés utilisés pour calculer le score de risque
	 * à partir des notes du patient.
	 */
	private static final List<String> KEYWORDS = List.of(
		    "Hémoglobine A1C", "Microalbumine", "Taille", "Poids",
		    "Fumeur", "Fumeuse", "Anormal", "Cholestérol", "Vertiges",
		    "Rechute", "Réaction", "Anticorps"
		);

	
	/**
	 * Évalue le niveau de risque du patient à partir de ses notes médicales
	 * et de ses données personnelles.
	 *
	 * @param patient Le patient à analyser.
	 * @param notes La liste des notes associées au patient.
	 * @return Un libellé correspondant au niveau de risque : 
	 *         "None", "Borderline", "In Danger", ou "Early onset".
	 */
	public String evaluateRisk(Patient patient, List<Note> notes) {
		
		 int age = calculerAge(patient.getDateNaissance());
		 int score = calculerScore(notes);	    
		 String genre = patient.getGenre();

		    if (score == 0) return "None";

		    if (score >= 2 && score <= 5 && age > 30) return "Borderline";

		    if ((genre.equals("M") && score == 3 && age <= 30) || (genre.equals("F") && score == 4 && age <= 30)) {
		        return "In Danger";
		    }
		    
		    if ((genre.equals("M") || genre.equals("F")) && age > 30 && (score == 6 || score == 7)) {
			        return "In Danger";
			    }

		    if ((genre.equals("M") && score >= 5 && age <= 30) ||
		        (genre.equals("F") && score >= 7 && age <= 30) ||
		        ((genre.equals("M") || genre.equals("F")) && age > 30 && score >= 8)) {
		        return "Early onset";
		    }

		    return "None";
	}
	
	
	/**
	 * Calcule l'âge du patient à partir de sa date de naissance.
	 *
	 * @param dateNaissance La date de naissance du patient au format YYYY-MM-DD.
	 * @return L'âge du patient en années.
	 */
	
	public int calculerAge(String dateNaissance) {
		 LocalDate birthDate = LocalDate.parse(dateNaissance);
		    return Period.between(birthDate, LocalDate.now()).getYears();
		
	}
	
	/**
	 * Calcule un score basé sur la présence des mots-clés dans les notes du patient.
	 *
	 * @param notes La liste des notes du patient.
	 * @return Le score calculé en fonction du nombre d'occurrences de mots-clés.
	 */
	public int calculerScore(List<Note> notes) {
		int score = 0;
	    for (Note note : notes) {
	        String content = note.getNote().toLowerCase();
	        for (String keyword : KEYWORDS) {
	            if (content.contains(keyword.toLowerCase())) {
	                score++;
	            }
	        }
	    }
	    return score;
		
	}
	
}
