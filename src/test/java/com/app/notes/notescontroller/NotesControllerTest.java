package com.app.notes.notescontroller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.app.notes.controller.NotesController;
import com.app.notes.request.NoteRequest;
import com.app.notes.response.NoteCreateResponse;
import com.app.notes.response.NoteResponse;
import com.app.notes.serviceimpl.NotesServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bucket4j.Bucket;

@RunWith(SpringRunner.class)
@WebMvcTest(NotesController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
public class NotesControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private NotesServiceImpl noteService;

	@MockBean
	private Bucket bucket;

	@Test
	public void testCreateNotes() throws Exception {
		NoteRequest noteRequest = new NoteRequest();
		noteRequest.setTitle("Test Note");
		noteRequest.setContent("Test Content");

		NoteCreateResponse response = new NoteCreateResponse();
		response.setId("123");

		when(noteService.createNote(any(NoteRequest.class))).thenReturn(response);
		when(bucket.tryConsume(1)).thenReturn(true); // Assuming you have properly configured the bucket in your test

		mockMvc.perform(post("/api/note").contentType(MediaType.APPLICATION_JSON).content(asJsonString(noteRequest)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.id").value("123"));

		verify(noteService, times(1)).createNote(any(NoteRequest.class));
	}

	@Test
	public void testGetNotesById() throws Exception {
		String noteId = "123";
		NoteResponse response = new NoteResponse();
		response.setId(noteId);
		when(noteService.getNoteById(anyString())).thenReturn(response);

		mockMvc.perform(get("/api/note/{id}", noteId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(noteId)));

		verify(noteService, times(1)).getNoteById(eq(noteId));
	}

	@Test
	public void testUpdateNotesById() throws Exception {
		String noteId = "123";
		NoteRequest noteRequest = new NoteRequest();
		noteRequest.setTitle("Updated Title");
		noteRequest.setContent("Updated Content");

		NoteResponse response = new NoteResponse();
		response.setId(noteId);

		when(noteService.updateNoteById(eq(noteId), any(NoteRequest.class))).thenReturn(response);

		mockMvc.perform(put("/api/note/{id}", noteId).contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(noteRequest))).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(noteId)));

		verify(noteService, times(1)).updateNoteById(eq(noteId), any(NoteRequest.class));
	}

	@Test
	public void testDeleteNotesById() throws Exception {
		String noteId = "123";

		mockMvc.perform(delete("/api/note/{id}", noteId)).andExpect(status().isNoContent());

		verify(noteService, times(1)).deleteNoteById(eq(noteId));
	}

	@Test
	public void testShareNotes() throws Exception {
		String noteId = "123";
		String recipientEmail = "recipient@example.com";
		String responseMessage = "Notes Shared Successfully";

		when(noteService.shareNotes(eq(noteId), eq(recipientEmail))).thenReturn(responseMessage);

		mockMvc.perform(post("/api/note/{id}/share", noteId).param("recipentEmailId", recipientEmail))
				.andExpect(status().isOk()).andExpect(content().string(responseMessage));

		verify(noteService, times(1)).shareNotes(eq(noteId), eq(recipientEmail));
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
