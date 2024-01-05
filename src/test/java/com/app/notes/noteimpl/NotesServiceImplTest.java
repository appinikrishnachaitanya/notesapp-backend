package com.app.notes.noteimpl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.app.notes.exceptions.NotesNotFoundException;
import com.app.notes.exceptions.SharedNotesException;
import com.app.notes.models.Note;
import com.app.notes.models.User;
import com.app.notes.repository.NoteRepository;
import com.app.notes.repository.SharedNoteRepository;
import com.app.notes.repository.UserRepository;
import com.app.notes.request.NoteRequest;
import com.app.notes.response.NoteCreateResponse;
import com.app.notes.response.NoteResponse;
import com.app.notes.response.SharedNotesResponse;
import com.app.notes.serviceimpl.NotesServiceImpl;

class NotesServiceImplTest {

	@Mock
	private NoteRepository noteRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private SharedNoteRepository sharedNoteRepository;

	@InjectMocks
	private NotesServiceImpl notesService;

	private User mockUser;
	private Note mockNote;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);

		mockUser = new User();
		mockUser.setId("1");
		mockUser.setEmailId("user@example.com");

		mockNote = new Note();
		mockNote.setId("noteId");
		mockNote.setTitle("Note Title");
		mockNote.setContent("Note Content");
		mockNote.setCreatedAt(LocalDateTime.now());
		mockNote.setUpdatedAt(LocalDateTime.now());
		mockNote.setUser(mockUser);

		// Mocking authentication context
		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn(mockUser.getEmailId());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Mocking userRepository
		when(userRepository.findByEmailId(anyString())).thenReturn(mockUser);

		// Mocking noteRepository
		when(noteRepository.findByIdAndUserId(anyString(), anyString())).thenReturn(Optional.of(mockNote));
		when(noteRepository.findByUserId(anyString())).thenReturn(List.of(mockNote));

		// Mocking sharedNoteRepository
		when(sharedNoteRepository.findByNoteIdAndRecipientEmail(anyString(), anyString())).thenReturn(Optional.empty());
		when(sharedNoteRepository.findByUserId(anyString())).thenReturn(new ArrayList<>());
	}

	@Test
	void createNote_ValidNoteRequest_NoteCreateResponse() {
		NoteRequest noteRequest = new NoteRequest();
		noteRequest.setTitle("New Note");
		noteRequest.setContent("New Content");

		NoteCreateResponse response = notesService.createNote(noteRequest);

		assertNotNull(response);
		assertNotNull(response.getId());

	}

	@Test
	void getNoteById_ValidNoteId_NoteResponse() {
		String noteId = "noteId";
		NoteResponse response = notesService.getNoteById(noteId);

		assertNotNull(response);
		assertEquals(mockNote.getId(), response.getId());
	}

	@Test
	void updateNoteById_NoteRepositoryReturnsEmpty_NotesNotFoundException() {
		String noteId = "noteId";
		NoteRequest noteRequest = new NoteRequest();

		when(noteRepository.findByIdAndUserId(anyString(), anyString())).thenReturn(Optional.empty());

		assertThrows(NotesNotFoundException.class, () -> notesService.updateNoteById(noteId, noteRequest));
	}

	@Test
	void deleteNoteById_ValidNoteId_NoExceptionsThrown() {
		String noteId = "noteId";
		assertDoesNotThrow(() -> notesService.deleteNoteById(noteId));
	}

	@Test
	void shareNotes_RecipientUserEqualsCurrentUser_SharedNotesException() {
		String noteId = "noteId";
		String recipientEmail = "user@example.com";

		assertThrows(SharedNotesException.class, () -> notesService.shareNotes(noteId, recipientEmail));
	}

}
