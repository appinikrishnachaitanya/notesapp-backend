package com.app.notes.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

	@Email
	private String emailId;
	@NotBlank
	@Size(min = 8)
	private String password;
	@NotBlank
	@Size(min = 10, max = 12)
	private String phoneNumber;

	@NotBlank
	private String name;

}
