package com.app.notes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.notes.models.NoteShared;

public interface SharedNoteRepository extends JpaRepository<NoteShared, String> {

	Optional<NoteShared> findByNoteIdAndRecipientEmail(String noteId, String mail);
	List<NoteShared> findByUserId(String userId);

}
