package com.fpoly.java5.jpas;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fpoly.java5.entities.UserEntity;

public interface UserJPA extends JpaRepository<UserEntity, Integer>{

	@Query(value = "SELECT * FROM users WHERE username=?1", nativeQuery = true)
	public Optional<UserEntity> checkUsernameExist(String username);
	
	@Query(value = "SELECT * FROM users WHERE email=?1", nativeQuery = true)
	public Optional<UserEntity> checkEmailExist(String email);
}

