package com.app.notes.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.modelmapper.ModelMapper;

import com.app.notes.models.Note;
import com.app.notes.request.NoteRequest;
import com.app.notes.response.NoteResponse;

public class NoteUtils {

	public static final ModelMapper modelMapper = new ModelMapper();

	public static NoteResponse entityToResponse(Note note) {
		return modelMapper.map(note, NoteResponse.class);
	}

	public static Note requestToEntity(NoteRequest noteRequest) {
		return modelMapper.map(noteRequest, Note.class);
	}

	public static String generateNoteId() {
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return  (uuid+timestamp).substring(0, 20).toUpperCase();
	}

	

}
