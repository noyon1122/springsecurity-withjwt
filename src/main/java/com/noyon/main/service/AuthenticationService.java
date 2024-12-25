package com.noyon.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.sql.init.dependency.AbstractBeansOfTypeDatabaseInitializerDetector;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.noyon.main.dto.AuthenticationResponse;
import com.noyon.main.jwt.JwtService;
import com.noyon.main.model.Role;
import com.noyon.main.model.Token;
import com.noyon.main.model.User;
import com.noyon.main.repository.TokenRepository;
import com.noyon.main.repository.UserRepository;

@Service
public class AuthenticationService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TokenRepository tokenRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired 
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	//save token
	
	public void saveToken(String jwt,User user)
	{
		Token token=new Token();
		token.setToken(jwt);
		token.setLogout(false);
		token.setUser(user);
		tokenRepository.save(token);
	}
	
	//inActive all after logout or newly login
	
	public void inActiveAllToken(User user)
	{
		
		List<Token> tokens=tokenRepository.findAllTokensByUserid(user.getId());
		
		if(tokens.isEmpty())
		{
			return;
		}
		else {
			tokens.forEach(token-> token.setLogout(true));
			tokenRepository.saveAll(tokens);
		}
	}
	
	//register user account
	
	public AuthenticationResponse register(User request)
	{
		if(userRepository.findByEmail(request.getEmail()).isPresent())
		{
			return new AuthenticationResponse(null,"User Already exists");
		}
		
	
		request.setFirstName(request.getFirstName());
		request.setLastName(request.getLastName());
		request.setAddress(request.getAddress());
		request.setActive(false);
		request.setLock(true);
		request.setCellPhone(request.getCellPhone());
		request.setEmail(request.getEmail());
		request.setPassword(passwordEncoder.encode(request.getPassword()));
		request.setRole(Role.valueOf("ADMIN"));
		
		
		userRepository.save(request);
		
		String jwt=jwtService.generatedToken(request);
		saveToken(jwt, request);
		//sendActivation(user)
		
		return new AuthenticationResponse(jwt,"You are successfully registered");
		
	}
	
	//login 
	
	public AuthenticationResponse login(User request)
	{
		
		
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword())
					
					);
			User user=userRepository.findByEmail(request.getEmail()).orElseThrow();
			String jwt=jwtService.generatedToken(user);
			
			inActiveAllToken(user);
			saveToken(jwt, user);
			return new AuthenticationResponse(jwt,"Login Successfully!!");
	
		
	}
}
