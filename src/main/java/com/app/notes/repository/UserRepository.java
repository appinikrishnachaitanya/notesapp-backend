package com.app.notes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.notes.models.User;

public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByEmailIdOrPhoneNumber(String email, String phoneNumber);
	
	User  findByEmailId(String emailid);
	

}
