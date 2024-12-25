package com.noyon.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.noyon.main.model.User;
import com.noyon.main.repository.UserRepository;

@Service
public class MyUserService {

	@Autowired
	private UserRepository ueRepository;
	
	public List<User> getAllUser()
	{
		return ueRepository.findAll();
	}
}
