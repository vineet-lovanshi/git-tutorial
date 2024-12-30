package com.security.service;

import java.util.List;

import com.security.dto.UserRequest;
import com.security.model.UserDtls;

public interface UserService {

	public String login(UserRequest userRequest);
	
	public List<UserDtls> getUserDtls();
}
