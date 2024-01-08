package com.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Role findByName(String name);
}
