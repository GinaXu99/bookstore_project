package com.bookstore.service;

import java.util.Set;

import com.bookstore.model.PasswordResetToken;
import com.bookstore.model.User;
import com.bookstore.model.UserBilling;
import com.bookstore.model.UserPayment;
import com.bookstore.model.UserRole;
import com.bookstore.model.UserShipping;

public interface UserService {
	PasswordResetToken getPasswordResetToken(String token);

	void createPasswordResetTokenForUser(User user, String token);

	User findByUserName(String username);

	User findByEmail(String email);

	User createUser(User user, Set<UserRole> userRoles) throws Exception;

	User save(User user);

	User findById(Long id);

	PasswordResetToken findPasswordResetTokenByUser(User user);

	void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);

	void setUserDefaultPayment(Long userPaymentId, User user);

	void updateUserShipping(UserShipping userShipping, User user);

	void setUserDefaultShipping(Long defaultShippingId, User user);

}
