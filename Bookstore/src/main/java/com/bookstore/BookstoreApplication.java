package com.bookstore;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bookstore.model.Role;
import com.bookstore.model.User;
import com.bookstore.model.UserRole;
import com.bookstore.service.UserService;
import com.bookstore.utility.SecurityUtility;

@SpringBootApplication(
scanBasePackages={
		"com.bookstore.config", "com.bookstore.controller","com.bookstore.model", "com.bookstore.repository"
		,"com.bookstore.service", "com.bookstore.serviceImpl", "com.bookstore.utility"})
public class BookstoreApplication implements CommandLineRunner {
	
	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		User user1 = new User();
		user1.setFirstName("user");
		user1.setLastName("user");
		user1.setUserName("admin");
		user1.setPassword(SecurityUtility.passwordEncoder().encode("test"));
		user1.setEmail("admin@gmail.com");
		
		Set<UserRole> userRoles = new HashSet<>();
		Role role1= new Role();
		role1.setRoleId(2L);
		role1.setName("ROLE_ADMIN");
		userRoles.add(new UserRole(user1, role1));
		
		userService.createUser(user1, userRoles);
		
	}

}
