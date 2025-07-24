package com.medilabo.note.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Document(collection = "notes")
public class Note {
	

	@Id
	private String id;
	
	private int patId;
	
	private String patient;
	
	private String note;

}
