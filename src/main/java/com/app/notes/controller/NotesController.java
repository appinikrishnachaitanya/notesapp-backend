package com.app.notes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.notes.request.NoteRequest;
import com.app.notes.response.NoteCreateResponse;
import com.app.notes.response.NoteResponse;
import com.app.notes.response.SharedNotesResponse;
import com.app.notes.serviceimpl.NotesServiceImpl;

import io.github.bucket4j.Bucket;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/note")
@SecurityRequirement(name = "notessecurity")
public class NotesController {

	private NotesServiceImpl noteService;

	@Autowired
	private Bucket bucket;

	public NotesController(NotesServiceImpl noteService) {
		this.noteService = noteService;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<NoteCreateResponse> createNotes(@Valid @RequestBody NoteRequest noteRequest) {
		if (bucket.tryConsume(1)) {
			return new ResponseEntity<>(this.noteService.createNote(noteRequest), HttpStatus.CREATED);
		}
		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();

	}

	@GetMapping("/{id}")
	public ResponseEntity<NoteResponse> getNotesById(@PathVariable("id") String id) {
		return new ResponseEntity<NoteResponse>(this.noteService.getNoteById(id), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<NoteResponse> updateNotesById(@PathVariable("id") String id,
			@Valid @RequestBody NoteRequest noteRequest) {
		return new ResponseEntity<NoteResponse>(this.noteService.updateNoteById(id, noteRequest), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteNotesById(@PathVariable("id") String id) {
		this.noteService.deleteNoteById(id);

		return ResponseEntity.noContent().build();

	}

	@GetMapping
	public ResponseEntity<List<NoteResponse>> getAllNotes()

	{
		return ResponseEntity.ok(this.noteService.getAllNotes());
	}

	@PostMapping("/{id}/share")
	public ResponseEntity<String> shareNotes(@PathVariable("id") String id,
			@RequestParam(required = true) String recipentEmailId) {
		return ResponseEntity.ok(this.noteService.shareNotes(id, recipentEmailId));
	}

	@GetMapping("/sharednotes")
	public ResponseEntity<List<SharedNotesResponse>> getSharedNotes() {
		return ResponseEntity.ok(this.noteService.getSharedNotes());
	}

	@GetMapping("/search")
	public ResponseEntity<List<NoteResponse>> fullTextSearch(@RequestParam String keyword) {
		List<NoteResponse> searchResults = noteService.fullTextSearch(keyword);
		return new ResponseEntity<>(searchResults, HttpStatus.OK);
	}

}
