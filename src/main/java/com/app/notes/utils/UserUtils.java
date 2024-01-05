package com.app.notes.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.modelmapper.ModelMapper;

import com.app.notes.models.User;
import com.app.notes.request.UserRequest;

public class UserUtils {

	public static final ModelMapper modelMapper = new ModelMapper();

	public static User requestToEntity(UserRequest userRequests) {
		return modelMapper.map(userRequests, User.class);
	}
	
	public static String generateUserId() {
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return (uuid+timestamp).substring(0, 10).toUpperCase();
	}
}
