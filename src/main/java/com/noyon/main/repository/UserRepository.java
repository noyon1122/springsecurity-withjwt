package com.noyon.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.noyon.main.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User>findByEmail(String email);
}
