package com.app.notes.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class NoteCustomExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		Map<String, String> map = new HashMap<>();

		ex.getFieldErrors().stream().forEach((e) -> {

			String fieldName = e.getField();
			String message = e.getDefaultMessage();
			map.put(fieldName, message);

		});

		return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(NotesNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleNoteNotFoundExceptions(NotesNotFoundException ex, WebRequest request) {
		ErrorDetails err = new ErrorDetails();
		err.setDateTime(LocalDateTime.now());
		err.setPath(request.getContextPath());
		err.setMessage(ex.getMessage());
		err.setStatus("NOTES_NOT_FOUND");

		return new ResponseEntity<ErrorDetails>(err, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorDetails> handleaUnauthorizedExceptions(UnauthorizedException ex, WebRequest request) {
		ErrorDetails err = new ErrorDetails();
		err.setDateTime(LocalDateTime.now());
		err.setPath(request.getContextPath());
		err.setMessage(ex.getMessage());
		err.setStatus("UNAUTHORIZED_USER");

		return new ResponseEntity<ErrorDetails>(err, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(UserAlreadyExistException.class)
	public ResponseEntity<ErrorDetails> handleUserAlreadyExistException(UserAlreadyExistException ex ,WebRequest request)
	{
		ErrorDetails err = new ErrorDetails();
		err.setDateTime(LocalDateTime.now());
		err.setPath(request.getContextPath());
		err.setMessage(ex.getMessage());
		err.setStatus("USER_ALREADY_EXIST");

		return new ResponseEntity<ErrorDetails>(err, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(TitleAlreadyExistException.class)
	public ResponseEntity<ErrorDetails> handleTitleAlreadyExistException(TitleAlreadyExistException ex ,WebRequest request)
	{
		ErrorDetails err = new ErrorDetails();
		err.setDateTime(LocalDateTime.now());
		err.setPath(request.getContextPath());
		err.setMessage(ex.getMessage());
		err.setStatus("NOTE_TITLE_ALREADY_EXIST");

		return new ResponseEntity<ErrorDetails>(err, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(SharedNotesException.class)
	public ResponseEntity<ErrorDetails> handleSharedNtoesException(SharedNotesException ex ,WebRequest request)
	{
		ErrorDetails err = new ErrorDetails();
		err.setDateTime(LocalDateTime.now());
		err.setPath(request.getContextPath());
		err.setMessage(ex.getMessage());
		err.setStatus("SHARED_NOTES_EXCEPTION");

		return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
	}

}
