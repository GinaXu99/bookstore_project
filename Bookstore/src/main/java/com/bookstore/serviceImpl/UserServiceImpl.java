package com.bookstore.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.model.PasswordResetToken;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import com.bookstore.model.UserBilling;
import com.bookstore.model.UserPayment;
import com.bookstore.model.UserRole;
import com.bookstore.model.UserShipping;
import com.bookstore.repository.PasswordResetTokenRepository;
import com.bookstore.repository.RoleRepository;
import com.bookstore.repository.UserPaymentRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.repository.UserShippingRepository;
import com.bookstore.service.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserPaymentRepository userPaymentRepository;

	@Autowired
	private UserShippingRepository userShippingRepository;

	/*password and token part*/
	@Override
	public PasswordResetToken getPasswordResetToken(String token) {
		return passwordResetTokenRepository.findByToken(token);
	}

	@Override
	public void createPasswordResetTokenForUser(User user, String token) {

		PasswordResetToken existToken = passwordResetTokenRepository.findByUser(user);

		if (existToken != null) {
			passwordResetTokenRepository.save(existToken);
		} else {
			PasswordResetToken myToken = new PasswordResetToken(token, user);
			passwordResetTokenRepository.save(myToken);
		}
	}

	@Override
	public PasswordResetToken findPasswordResetTokenByUser(User user) {
		return passwordResetTokenRepository.findByUser(user);
	}

	/*user part*/
	@Override
	public User findByUserName(String username) {
		return userRepository.findByUserName(username);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User findById(Long id) {
		return userRepository.findById(id).get();
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

	@Override
	public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user) {
		userPayment.setUser(user);
		userPayment.setUserBilling(userBilling);
		userPayment.setDefaultPayment(true);

		userBilling.setUserPayment(userPayment);
		user.getUserPaymentList().add(userPayment);
		save(user);
	}

	@Override
	public void setUserDefaultPayment(Long defaultPaymentId, User user) {
		List<UserPayment> userPaymentList = (List<UserPayment>) userPaymentRepository.findAll();
		for (UserPayment userPayment : userPaymentList) {
			if (userPayment.getId() == defaultPaymentId) {
				userPayment.setDefaultPayment(true);
				userPaymentRepository.save(userPayment);
			} else {
				userPayment.setDefaultPayment(false);
				userPaymentRepository.save(userPayment);
			}
		}
	}

	@Override
	public void updateUserShipping(UserShipping userShipping, User user) {
		userShipping.setUser(user);
		user.getUserShippingList().add(userShipping);
		userShipping.setUserShippingDefault(true);
		save(user);
	}

	@Override
	public void setUserDefaultShipping(Long defaultShippingId, User user) {
		List<UserShipping> userShippingList = (List<UserShipping>) userShippingRepository.findAll();

		for (UserShipping userShipping : userShippingList) {
			if (userShipping.getId() == defaultShippingId) {
				userShipping.setUserShippingDefault(true);
				userShippingRepository.save(userShipping);
			} else {
				userShipping.setUserShippingDefault(false);
				userShippingRepository.save(userShipping);
			}
		}
	}

}
