package com.medilabo.frontend_service.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Note {

	private String id;
	private int patId;	
	private String patient;
	private String note;
	
	 public Note(int patId, String note) {
	        this.patId = patId;
	        this.note = note;
	    }

	
}
