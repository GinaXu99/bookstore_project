package com.adminportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adminportal.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserName(String username);

	User findByEmail(String email);
}
