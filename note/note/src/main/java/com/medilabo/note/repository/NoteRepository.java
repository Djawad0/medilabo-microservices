package com.medilabo.note.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.medilabo.note.model.Note;

@Repository
public interface NoteRepository extends MongoRepository<Note, String>  {
	List<Note> findByPatId(int patId);
}
