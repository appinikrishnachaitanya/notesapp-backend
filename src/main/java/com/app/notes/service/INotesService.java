package com.app.notes.service;

import java.util.List;

import com.app.notes.request.NoteRequest;
import com.app.notes.response.NoteCreateResponse;
import com.app.notes.response.NoteResponse;
import com.app.notes.response.SharedNotesResponse;

public interface INotesService {

	public NoteCreateResponse createNote(NoteRequest noteRequest);

	public NoteResponse getNoteById(String id);

	public NoteResponse updateNoteById(String id, NoteRequest noteRequest);

	public void deleteNoteById(String id);

	public List<NoteResponse> getAllNotes();

	public String shareNotes(String noteId, String recipentEmailId);

	public List<SharedNotesResponse> getSharedNotes();

}
