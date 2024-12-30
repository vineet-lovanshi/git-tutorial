package com.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.security.dto.UserRequest;
import com.security.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HomeController {

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public ResponseEntity<?> getGreetings(HttpServletRequest request) {

//		String id = request.getSession().getId();
		return new ResponseEntity<>("Hello User Welcome to SpringBoot tutorial ", HttpStatus.OK);

	}

	@GetMapping("/get-user")
	public ResponseEntity<?> getUserDetails(HttpServletRequest request) {

//		String id = request.getSession().getId();
		return new ResponseEntity<>(userService.getUserDtls(), HttpStatus.OK);

	}

	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserRequest userRequest) {

		String token = userService.login(userRequest);

		if (token == null) {
			return new ResponseEntity<>("Invalid Credentilas", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(token, HttpStatus.OK);

	}
}
