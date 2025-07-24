package com.medilabo.note.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.medilabo.note.model.Note;
import com.medilabo.note.service.NoteService;

/**
 * Contrôleur REST permettant de gérer les notes médicales des patients.
 */

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "http://localhost:8082")
public class NoteController {

	
	 @Autowired
	    private NoteService noteService;
	 
	 /**
	     * Récupère toutes les notes liées à un patient donné via son identifiant.
	     *
	     * @param patId L'identifiant du patient.
	     * @return La liste des notes associées à ce patient.
	     */

	    @GetMapping("/{patId}")
	    public List<Note> getNotesByPatId(@PathVariable int patId) {
	        return noteService.getNotesByPatId(patId);
	    }
	    
	    /**
	     * Ajoute une nouvelle note médicale à la base de données.
	     *
	     * @param note L'objet Note à enregistrer.
	     * @return La note enregistrée avec son identifiant.
	     */

	    @PostMapping
	    public Note addNote(@RequestBody Note note) {
	        return noteService.addNote(note);
	    }
}
