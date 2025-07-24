package com.medilabo.note.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medilabo.note.model.Note;
import com.medilabo.note.repository.NoteRepository;

/**
 * Service métier pour la gestion des notes médicales.
 * Fournit des méthodes pour récupérer et ajouter des notes liées à un patient.
 */

@Service
public class NoteService {
	
	
	 @Autowired
	    private NoteRepository noteRepository;
	 
	 
	 /**
	     * Récupère toutes les notes associées à un patient donné.
	     *
	     * @param patId L'identifiant du patient.
	     * @return La liste des notes liées à ce patient.
	     */
	
	    public List<Note> getNotesByPatId(int patId) {
	        return noteRepository.findByPatId(patId);
	    }

	    
	    /**
	     * Ajoute une nouvelle note médicale dans la base de données.
	     *
	     * @param note L'objet Note à enregistrer.
	     * @return La note enregistrée.
	     */
	  
	    public Note addNote(Note note) {
	        return noteRepository.save(note);
	    }

}
