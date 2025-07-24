package com.medilabo.frontend_service.model;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Patient {
	
	private int id;
	private String nom;
	private String prenom;
	private String dateNaissance;
	private String genre;
	private String adresse;
	private String telephone;
	    
}
