package com.app.notes.service;

import com.app.notes.request.LoginRequest;
import com.app.notes.request.UserRequest;
import com.app.notes.response.UserCreateResponse;

public interface IUserService {

	public UserCreateResponse createUser(UserRequest userRequest);

	public String login(LoginRequest loginRequest);

}
