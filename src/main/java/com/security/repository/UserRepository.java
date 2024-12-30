package com.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.model.UserDtls;

public interface UserRepository extends JpaRepository<UserDtls, Integer> {

	UserDtls findByUserName(String username);

}
