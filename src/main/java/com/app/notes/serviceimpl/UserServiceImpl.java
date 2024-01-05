package com.app.notes.serviceimpl;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.notes.exceptions.UserAlreadyExistException;
import com.app.notes.models.User;
import com.app.notes.repository.UserRepository;
import com.app.notes.request.LoginRequest;
import com.app.notes.request.UserRequest;
import com.app.notes.response.UserCreateResponse;
import com.app.notes.service.IUserService;
import com.app.notes.utils.UserUtils;

@Service
public class UserServiceImpl implements IUserService {

	private AuthenticationManager authenticationManager;
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	public UserServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
			PasswordEncoder passwordEncoder) {

		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserCreateResponse createUser(UserRequest userRequest)  {
		Optional<User> user = userRepository.findByEmailIdOrPhoneNumber(userRequest.getEmailId(),
				userRequest.getPhoneNumber());

		if (user.isEmpty()) {
			User userEntity = UserUtils.requestToEntity(userRequest);
			userEntity.setRole("USER");
			userEntity.setPassword(this.passwordEncoder.encode(userRequest.getPassword()));
			userEntity.setId(UserUtils.generateUserId());
			User userEntityResponse = this.userRepository.save(userEntity);
			UserCreateResponse userCreateResponse = new UserCreateResponse();
			userCreateResponse.setUserId(userEntityResponse.getId());
			return userCreateResponse;
		} else {
			throw new UserAlreadyExistException("User Already Exists ....");
		}

	}

	@Override
	public String login(LoginRequest loginRequest) {
//		Optional<User> user = userRepository.findByEmailOrPhoneNumber(loginRequest.getUsername(), loginRequest.getUsername());
//		if(user.isEmpty())
//		{
//			throw new UnauthorizedException("Not a Valid Username/Password ");
//		}
//		

		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authenticate);

		return "User Logged In Successfully";
	}

}
