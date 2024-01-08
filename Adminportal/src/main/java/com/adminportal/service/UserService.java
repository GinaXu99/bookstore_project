package com.adminportal.service;

import java.util.Set;

import com.adminportal.model.User;
import com.adminportal.model.UserRole;

public interface UserService {

	User findByUserName(String username);
	User findByEmail (String email);
	User createUser(User user, Set<UserRole> userRoles)throws Exception;
	User save(User user);
	
}
