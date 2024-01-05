package com.app.notes.serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.notes.exceptions.NotesNotFoundException;
import com.app.notes.exceptions.SharedNotesException;
import com.app.notes.exceptions.UserNotFoundException;
import com.app.notes.models.Note;
import com.app.notes.models.NoteShared;
import com.app.notes.models.User;
import com.app.notes.repository.NoteRepository;
import com.app.notes.repository.SharedNoteRepository;
import com.app.notes.repository.UserRepository;
import com.app.notes.request.NoteRequest;
import com.app.notes.response.NoteCreateResponse;
import com.app.notes.response.NoteResponse;
import com.app.notes.response.SharedNotesResponse;
import com.app.notes.service.INotesService;
import com.app.notes.utils.NoteUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotesServiceImpl implements INotesService {

	private final NoteRepository noteRepository;

	private final UserRepository userRepository;

	private final SharedNoteRepository shareNotesRepository;

	public NotesServiceImpl(NoteRepository noteRepository, UserRepository userRepository,
			SharedNoteRepository shareNotesRepository) {
		this.noteRepository = noteRepository;
		this.userRepository = userRepository;

		this.shareNotesRepository = shareNotesRepository;
	}

	private User getCurrentUserDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return this.userRepository.findByEmailId(authentication.getName());

	}

	@Override
	public NoteCreateResponse createNote(NoteRequest noteRequest) {
		User user = this.getCurrentUserDetails();
		log.info("Creating notes {} " + noteRequest);
		Note note = NoteUtils.requestToEntity(noteRequest);
		note.setId(NoteUtils.generateNoteId());
		note.setCreatedAt(LocalDateTime.now());
		note.setUpdatedAt(LocalDateTime.now());
		note.setUser(user);
		log.info("Notes Created Successfully with Id {} " + note.getId());
		this.noteRepository.save(note);
		NoteCreateResponse noteCreateResponse = new NoteCreateResponse();
		noteCreateResponse.setId(note.getId());
		return noteCreateResponse;

	}

	@Override
	public NoteResponse getNoteById(String id) {
		User user = this.getCurrentUserDetails();
		log.info("Fetching the notes with Id {} " + id);
		Note note = this.noteRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new NotesNotFoundException("Notes Not Found with Id :" + id));
		return NoteUtils.entityToResponse(note);

	}

	@Override
	public NoteResponse updateNoteById(String id, NoteRequest noteRequest) {
		User user = this.getCurrentUserDetails();
		log.info("Updating the notes with Id {} " + id);
		Note note = this.noteRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new NotesNotFoundException("Notes Not Found with Id :" + id));
		note.setTitle(noteRequest.getTitle());
		note.setContent(noteRequest.getContent());
		note.setUpdatedAt(LocalDateTime.now());
		Note noteEntityResponse = this.noteRepository.save(note);
		log.info("Notes Created Successfully with Id {} " + note.getId());

		return NoteUtils.entityToResponse(noteEntityResponse);
	}

	@Override
	@Transactional
	public void deleteNoteById(String id) {

		User user = this.getCurrentUserDetails();
		log.info("Updating the notes with Id {} " + id);
		Note note = this.noteRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new NotesNotFoundException("Notes Not Found with Id :" + id));
		this.noteRepository.delete(note);
		
	}

	@Override
	public List<NoteResponse> getAllNotes() {
		User user = this.getCurrentUserDetails();
		return this.noteRepository.findByUserId(user.getId()).stream().map(NoteUtils::entityToResponse).toList();
	}

	@Override
	public String shareNotes(String noteId, String recipentEmailId) {

		User recipentUser = this.userRepository.findByEmailId(recipentEmailId);
		User user = this.getCurrentUserDetails();
		log.info("Fetching the notes with Id {} " + noteId);
		this.noteRepository.findByIdAndUserId(noteId, user.getId())
				.orElseThrow(() -> new NotesNotFoundException("Notes Not Found with Id :" + noteId));

		if (recipentUser == null) {
			throw new UserNotFoundException("Please Check the Recipent Emaid Id");
		}
		if (recipentUser.getEmailId().equals(user.getEmailId())) {
			throw new SharedNotesException("You can share notes only with others.");
		} else {
			Optional<NoteShared> noteShared = this.shareNotesRepository.findByNoteIdAndRecipientEmail(noteId,
					recipentEmailId);

			if (noteShared.isEmpty()) {

				NoteShared noteSharedEntity = new NoteShared();
				noteSharedEntity.setNoteId(noteId);
				noteSharedEntity.setRecipientEmail(recipentEmailId);
				noteSharedEntity.setUser(user);
				this.shareNotesRepository.save(noteSharedEntity);
				return "Notes Shared Successfully to " + recipentEmailId;
			}

			else {
				throw new SharedNotesException("You already shared the notes ");
			}
		}
	}

	@Override
	public List<SharedNotesResponse> getSharedNotes() {
		User user = this.getCurrentUserDetails();
		List<SharedNotesResponse> sharedNotes = new ArrayList<>();
		List<NoteShared> noteshared = this.shareNotesRepository.findByUserId(user.getId());
		if (noteshared.isEmpty()) {
			return new ArrayList<>();
		} else {
			noteshared.stream().forEach((n) -> {
				SharedNotesResponse sharedNotesResponse = new SharedNotesResponse();
				sharedNotesResponse.setNoteId(n.getNoteId());
				sharedNotesResponse.setRecepientEmail(n.getRecipientEmail());
				sharedNotes.add(sharedNotesResponse);
			});
			return sharedNotes;

		}

	}

	public List<NoteResponse> fullTextSearch(String keyword) {
		return noteRepository.searchByKeyword(keyword).stream().map(NoteUtils::entityToResponse).toList();
	}

}
