package com.app.notes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.notes.request.LoginRequest;
import com.app.notes.request.UserRequest;
import com.app.notes.response.UserCreateResponse;
import com.app.notes.serviceimpl.UserServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class UserController {

	@Autowired
	private UserServiceImpl userService;

	@PostMapping("/login")
	public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
		return ResponseEntity.ok(userService.login(loginRequest));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<UserCreateResponse> createUser(@Valid @RequestBody UserRequest userRequest)
	{
		return ResponseEntity.ok(userService.createUser(userRequest));
	}

}
