package com.adminportal;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import com.adminportal.model.Role;
import com.adminportal.model.User;
import com.adminportal.model.UserRole;
import com.adminportal.service.UserService;
import com.adminportal.utility.SecurityUtility;

@SpringBootApplication(
scanBasePackages={
		"com.adminportal.config", "com.adminportal.controller","com.adminportal.model", "com.adminportal.repository"
		,"com.adminportal.service", "com.adminportal.serviceImpl", "com.adminportal.utility"})
public class AdminportalApplication implements CommandLineRunner{
	
	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(AdminportalApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		User user1 = new User();
		user1.setFirstName("user");
		user1.setLastName("user");
		user1.setUserName("gina");
		user1.setPassword(SecurityUtility.passwordEncoder().encode("test"));
		user1.setEmail("gina@gmail.com");
		Set<UserRole> userRoles1 = new HashSet<>();
		Role role1= new Role();
		role1.setRoleId(1L);
		role1.setName("ROLE_USER");
		userRoles1.add(new UserRole(user1, role1));
		userService.createUser(user1, userRoles1);
		
		
		User user2 = new User();
		user2.setFirstName("user");
		user2.setLastName("user");
		user2.setUserName("admin");
		user2.setPassword(SecurityUtility.passwordEncoder().encode("test"));
		user2.setEmail("admin@gmail.com");
		Set<UserRole> userRoles2 = new HashSet<>();
		Role role2= new Role();
		role2.setRoleId(2L);
		role2.setName("ROLE_ADMIN");
		userRoles2.add(new UserRole(user2, role2));
		userService.createUser(user2, userRoles2);
		
	}

}
