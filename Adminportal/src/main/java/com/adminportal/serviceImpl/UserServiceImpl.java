package com.adminportal.serviceImpl;

import java.util.ArrayList;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminportal.model.ShoppingCart;
import com.adminportal.model.User;
import com.adminportal.model.UserPayment;
import com.adminportal.model.UserRole;
import com.adminportal.model.UserShipping;
import com.adminportal.repository.RoleRepository;
import com.adminportal.repository.UserRepository;
import com.adminportal.service.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public User findByUserName(String username) {

		return userRepository.findByUserName(username);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	@Transactional
	public User createUser(User user, Set<UserRole> userRoles) throws Exception {
		User localUser = userRepository.findByUserName(user.getUsername());
		if (localUser != null) {
			logger.info("user {} already exists", user.getUsername());
		} else {
			for (UserRole role : userRoles) {
				roleRepository.save(role.getRole());
			}

			user.getUserRoles().addAll(userRoles);

			/* shopping cart logic added */
			ShoppingCart shoppingCart = new ShoppingCart();
			shoppingCart.setUser(user);
			user.setShoppingCart(shoppingCart);
			user.setUserShippingList(new ArrayList<UserShipping>());
			user.setUserPaymentList(new ArrayList<UserPayment>());

			localUser = userRepository.save(user);
		}
		return localUser;
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

}
