package com.app.notes.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteRequest {

	@NotBlank(message = "it  should not be empty ")
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "it should contains only Alphabets")
	@Size(min = 3, max = 20)
	private String title;
	@NotBlank(message = "it should not be empty")
	private String content;
}
