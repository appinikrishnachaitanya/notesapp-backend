package com.app.notes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.notes.models.Note;

public interface NoteRepository extends JpaRepository<Note, String> {

	List<Note> findByIdIn(List<String> ids);

	Optional<Note> findByIdAndUserId(String noteid, String userId);

	List<Note> findByUserId(String userId);

	@Query(value = "SELECT * FROM Note n WHERE "
			+ "(:keyword IS NULL OR :keyword = '' OR to_tsvector('english', n.title || ' ' || n.content) @@ to_tsquery('english', :keyword))", nativeQuery = true)
	List<Note> searchByKeyword(@Param("keyword") String keyword);

}
