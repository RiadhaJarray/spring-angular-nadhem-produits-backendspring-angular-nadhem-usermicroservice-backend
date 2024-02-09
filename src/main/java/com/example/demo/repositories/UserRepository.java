package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByUsername(String username);
	//je vais utiliser cet methode pour verifier que l'email n'est pas utilisé
	Optional<User> findByEmail(String email);
}

