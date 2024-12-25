package com.noyon.main.model.controller;

import java.util.List;

import org.hibernate.dialect.MySQL8Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.noyon.main.dto.AuthenticationResponse;
import com.noyon.main.model.User;
import com.noyon.main.service.AuthenticationService;
import com.noyon.main.service.MyUserService;

@RestController
public class AuthenticationController {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private MyUserService myUserService;
	
	//register user account
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody User user)
	{
		return ResponseEntity.ok(authenticationService.register(user));
	}
	
	//login user account 
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody User user)
	{
		return ResponseEntity.ok(authenticationService.login(user));
	}
	
	
	//get admin account
	@GetMapping("/admin/info")
	public ResponseEntity<List<User>> getAdmininfo()
	{
		return ResponseEntity.ok(myUserService.getAllUser());
	}
	
	//get user account 
	@GetMapping("/user/info")
	public ResponseEntity<List<User>> getUserInfo()
	{
		return ResponseEntity.ok(myUserService.getAllUser());
	}

}
