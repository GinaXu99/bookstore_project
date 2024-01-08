package com.bookstore.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.model.CartItem;
import com.bookstore.model.Order;
import com.bookstore.model.PasswordResetToken;
import com.bookstore.model.Role;
import com.bookstore.model.User;
import com.bookstore.model.UserBilling;
import com.bookstore.model.UserPayment;
import com.bookstore.model.UserRole;
import com.bookstore.model.UserShipping;
import com.bookstore.service.CartItemService;
import com.bookstore.service.OrderService;
import com.bookstore.service.UserPaymentService;
import com.bookstore.service.UserService;
import com.bookstore.service.UserShippingService;
import com.bookstore.serviceImpl.UserDetailServiceImpl;
import com.bookstore.utility.AUConstants;
import com.bookstore.utility.MailConstructor;
import com.bookstore.utility.SecurityUtility;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class AccountController {

	static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private UserDetailServiceImpl userDetailServiceImpl;

	@Autowired
	private UserPaymentService userPaymentService;

	@Autowired
	private UserShippingService userShippingService;

	@Autowired
	private MailConstructor mailConstructor;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private OrderService orderService;

	@Autowired
	private CartItemService cartItemService;

	
	/*
	 * 
	 * User Login/Register Section
	 * 
	 */
	@GetMapping("/login")
	public String loginAccount(Model model) {
		model.addAttribute("classActivelogin", true);
		return "myAccount";
	}

	@PostMapping("/forgetPassword")
	public String forgetPassword(Model model, HttpServletRequest request, @ModelAttribute("email") String email)
			throws Exception {

		model.addAttribute("classActiveForgetPassword", true);

		User user = userService.findByEmail(email);

		if (user == null) {
			model.addAttribute("emailNotExist", true);
			return "myAccount";
		}

		String password = SecurityUtility.randomPassword();
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);
		userService.save(user);

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);
		LOGGER.info("token is {}", token);

		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		LOGGER.info("appURL is {}", appUrl);

		SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user,
				password);
		LOGGER.info("mail is {}", newEmail);

		LOGGER.info("mailsender is {}", mailSender);
		mailSender.send(newEmail);
		model.addAttribute("forgetPasswordEmailSent", "true");

		return "myAccount";
	}

	@PostMapping("/newUser")
	public String newUserPost(HttpServletRequest request, @ModelAttribute("email") String email,
			@ModelAttribute("username") String userName, Model model) throws Exception {

		model.addAttribute("classActiveNewAccount", true);
		model.addAttribute("email", email);
		model.addAttribute("username", userName);

		if (userService.findByUserName(userName) != null) {
			model.addAttribute("usernameExists", true);
			return "myAccount";
		}

		if (userService.findByEmail(email) != null) {
			model.addAttribute("emailExists", true);
			return "myAccount";
		}

		User user = new User();
		user.setUserName(userName);
		user.setEmail(email);

		String password = SecurityUtility.randomPassword();
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);

		Role role = new Role();
		role.setRoleId(1L);
		role.setName("ROLE_ADMIN");
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user, role));
		userService.createUser(user, userRoles);

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);
		LOGGER.info("token is {}", token);

		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		LOGGER.info("appURL is {}", appUrl);

		SimpleMailMessage mail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user,
				password);
		LOGGER.info("mail is {}", mail);

		model.addAttribute("user", user);
		LOGGER.info("mailsender is {}", mailSender);
		mailSender.send(mail);
		
		model.addAttribute("emailSent", "true");
		model.addAttribute("orderList", user.getOrderList());

		return "myAccount";

	}

	@GetMapping("/newUser")
	public String registerAccount(Locale locale, @RequestParam("token") String token, Model model) {

		PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);

		if (passwordResetToken == null) {
			String message = "Invalid Token Used";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
		}

		User user = passwordResetToken.getUser();
		String username = user.getUsername();

		UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(username);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		model.addAttribute("user", user);
		model.addAttribute("classActiveEdit", true);

		return "myProfile";
	}

	@GetMapping("/myProfile")
	public String myProfile(Model model, Principal principal) {

			User user = userService.findByUserName(principal.getName());

			model.addAttribute("user", user);
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());

			UserShipping userShipping = new UserShipping();
			model.addAttribute("userShipping", userShipping);

			model.addAttribute("listOfCreditCards", true);
			model.addAttribute("listOfShippingAddresses", true);

			List<String> stateList = AUConstants.listOfAuStateAbbrevCodes;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			model.addAttribute("classActiveEdit", true);

			return "myProfile";
		
	}

	@PostMapping("/updateUserInfo")
	public String updateUserInfo(@ModelAttribute("user") User user, @ModelAttribute("newPassword") String newPassword,
			Model model) throws Exception {

		/*check user is the same user*/
		User currentUser = userService.findById(user.getId());

		if (currentUser == null) {
			throw new Exception("User not found");
		}

		/* check email already exists */
		if (userService.findByEmail(user.getEmail()) != null) {
			if (userService.findByEmail(user.getEmail()).getId() != currentUser.getId()) {
				model.addAttribute("useremailalreayExists", true);
				return "myProfile";
			}
		}

		/* check username already exists */
		if (userService.findByUserName(user.getUsername()) != null) {
			if (userService.findByUserName(user.getUsername()).getId() != currentUser.getId()) {
				model.addAttribute("usernamealreayExists", true);
				return "myProfile";
			}
		}

		/* update password */
		if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")) {
			BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
			String dbPassword = currentUser.getPassword();
			if (passwordEncoder.matches(user.getPassword(), dbPassword)) {
				currentUser.setPassword(passwordEncoder.encode(newPassword));
			} else {
				model.addAttribute("incorrectPassword", true);
				return "myProfile";
			}
		}

		currentUser.setFirstName(user.getFirstName());
		currentUser.setLastName(user.getLastName());
		currentUser.setUserName(user.getUsername());
		currentUser.setPhone(user.getPhone());
		currentUser.setEmail(user.getEmail());
		userService.save(currentUser);

		model.addAttribute("user", currentUser);
		model.addAttribute("classActiveEdit", true);

		UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(currentUser.getUsername());
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		model.addAttribute("updateSuccess", true);

		return "myProfile";
	}

	/*
	 * CreditCard Section
	 * 
	 */
	@GetMapping("/listOfCreditCards")
	public String listOfCreditCards(Model model, Principal principal, HttpServletRequest request) {

		User user = userService.findByUserName(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());

		model.addAttribute("orderList", user.getOrderList());
		model.addAttribute("listOfCreditcards", true);
		// model.addAttribute("addNewCreditCard", true);
		model.addAttribute("classActiveBilling", true);
		// model.addAttribute("listOfShippingAddresses", true);

		return "myProfile";

	}

	@GetMapping("/addNewCreditCard")
	public String addNewCreditCard(Model model, Principal principal) {
		User user = userService.findByUserName(principal.getName());
		model.addAttribute("user", user);

		// model.addAttribute("listOfCreditcards", true);
		model.addAttribute("addNewCreditCard", true);
		model.addAttribute("classActiveBilling", true);

		// model.addAttribute("listOfShippingAddresses", true);

		UserBilling userBilling = new UserBilling();
		UserPayment userPayment = new UserPayment();

		model.addAttribute("userBilling", userBilling);
		model.addAttribute("userPayment", userPayment);

		List<String> stateList = AUConstants.listOfAuStateAbbrevCodes;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);

		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());

		return "myProfile";

	}

	@PostMapping("/addNewCreditCard")
	public String addNewCreditCardPost(@Valid @ModelAttribute("userPayment") UserPayment userPayment,
			@Valid @ModelAttribute("userBilling") UserBilling userBilling, Principal principal, Model model) {

		User user = userService.findByUserName(principal.getName());
		userService.updateUserBilling(userBilling, userPayment, user);

		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());

		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("addNewCreditCard", true);
		//model.addAttribute("listOfShippingAddress", true);
		model.addAttribute("classActiveBilling", true);

		model.addAttribute("orderList", user.getOrderList());

		List<String> stateList = AUConstants.listOfAuStateAbbrevCodes;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);

		return "redirect:/listOfCreditCards";

	}

	@GetMapping("/updateCreditCard/{id}")
	public String updateCreditCard(@ModelAttribute("id") Long creditCardId, Model model, Principal principal) {
		User user = userService.findByUserName(principal.getName());
		UserPayment userPayment = userPaymentService.findById(creditCardId);

		if (user.getId() != userPayment.getUser().getId()) {
			return "badRequestPage";
		} else {

			UserBilling userBilling = userPayment.getUserBilling();
			model.addAttribute("user", user);
			model.addAttribute("userBilling", userBilling);
			model.addAttribute("userPayment", userPayment);
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());

			List<String> stateList = AUConstants.listOfAuStateAbbrevCodes;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);

			// model.addAttribute("listOfCreditcards", true);
			model.addAttribute("addNewCreditCard", true);

			// model.addAttribute("listOfShippingAddress", true);
			model.addAttribute("classActiveBilling", true);
			model.addAttribute("orderList", user.getOrderList());
			model.addAttribute("orderList", user.getOrderList());

			return "myProfile";
		}
	}

	@GetMapping("/removeCreditCard/{id}")
	public String removeCreditCard(@ModelAttribute("id") Long creditCardId, Model model, Principal principal) {

		User user = userService.findByUserName(principal.getName());
		UserPayment userPayment = userPaymentService.findById(creditCardId);

		if (user.getId() != userPayment.getUser().getId()) {
			return "badRequestPage";
		} else {

			userPaymentService.removeById(creditCardId);

			model.addAttribute("user", user);
			model.addAttribute("listOfCreditcards", true);
			// model.addAttribute("addNewCreditCard", true);

			// model.addAttribute("listOfShippingAddress", true);
			model.addAttribute("classActiveBilling", true);

			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());
			return "myProfile";
		}

	}

	@PostMapping("/setDefaultPayment")
	public String setDefaultPayment(@ModelAttribute("defaultPaymentId") Long defaultPaymentId, Principal principal,
			Model model) {

		User user = userService.findByUserName(principal.getName());
		userService.setUserDefaultPayment(defaultPaymentId, user);

		model.addAttribute("user", user);
		model.addAttribute("listOfCreditcards", true);
		model.addAttribute("addNewCreditCard", true);

		// model.addAttribute("listOfShippingAddress", true);
		model.addAttribute("classActiveBilling", true);

		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());

		return "myProfile";

	}

	/*
	 * Shipping Address Section
	 * 
	 */
	@GetMapping("/listOfShippingAddresses")
	public String listOfShippingAddresses(Model model, Principal principal, HttpServletRequest request) {

		User user = userService.findByUserName(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());

		// model.addAttribute("listOfCreditcards", true);
		// model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveShipping", true);

		return "myProfile";

	}

	@GetMapping("/addNewShippingAddress")
	public String addNewShippingAddress(Model model, Principal principal) {
		User user = userService.findByUserName(principal.getName());
		model.addAttribute("user", user);

		model.addAttribute("addNewShippingAddress", true);
		model.addAttribute("classActiveShipping", true);

		UserShipping userShipping = new UserShipping();

		model.addAttribute("userShipping", userShipping);

		List<String> stateList = AUConstants.listOfAuStateAbbrevCodes;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());

		return "myProfile";
	}

	@PostMapping("/addNewShippingAddress")
	public String addNewShippingAddressPost(@ModelAttribute("userShipping") UserShipping userShipping,
			Principal principal, Model model) {
		User user = userService.findByUserName(principal.getName());
		userService.updateUserShipping(userShipping, user);

		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveShipping", true);
		// model.addAttribute("listOfCreditcards", true);
		model.addAttribute("orderList", user.getOrderList());

		return "redirect:/listOfShippingAddresses";
	}

	@GetMapping("/updateUserShipping/{id}")
	public String updateUserShipping(@ModelAttribute("id") Long userShippingId, Principal principal, Model model) {

		User user = userService.findByUserName(principal.getName());
		UserShipping userShipping = userShippingService.findById(userShippingId);

		if (user.getId() != userShipping.getUser().getId()) {
			return "badRequestPage";
		} else {

			model.addAttribute("user", user);
			model.addAttribute("userShipping", userShipping);

			List<String> stateList = AUConstants.listOfAuStateAbbrevCodes;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);

			model.addAttribute("addNewShippingAddress", true);
			model.addAttribute("classActiveShipping", true);

			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());

			return "myProfile";
		}

	}

	@GetMapping("/removeUserShipping/{id}")
	public String removeUserShipping(@ModelAttribute("id") Long userShippingId, Principal principal, Model model) {

		User user = userService.findByUserName(principal.getName());
		UserShipping userShipping = userShippingService.findById(userShippingId);

		if (user.getId() != userShipping.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);

			userShippingService.removeById(userShippingId);

			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("classActiveShipping", true);

			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());

			return "myProfile";
		}
	}

	@PostMapping("/setDefaultShippingAddress")
	public String setDefaultShippingAddress(@ModelAttribute("defaultShippingAddressId") Long defaultShippingId,
			Principal principal, Model model) {
		User user = userService.findByUserName(principal.getName());
		userService.setUserDefaultShipping(defaultShippingId, user);

		model.addAttribute("user", user);
		// model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddresses", true);

		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());

		return "myProfile";
	}

	/* order detail */
	@GetMapping("/orderDetail/{id}")
	public String orderDetail(@ModelAttribute("id") long orderId, Principal principal, Model model) {
		User user = userService.findByUserName(principal.getName());
		Order order = orderService.findById(orderId);
		if (order.getUser().getId() != user.getId()) {
			return "badRequestPage";
		} else {
			List<CartItem> cartItemList = cartItemService.findByOrder(order);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("user", user);
			model.addAttribute("order", order);

			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());

			UserShipping userShipping = new UserShipping();
			model.addAttribute("userShipping", userShipping);

			List<String> stateList = AUConstants.listOfAuStateAbbrevCodes;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);

			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("classActiveOrders", true);
			model.addAttribute("listOfCreditCards", true);
			model.addAttribute("displayOrderDetail", true);

			return "myProfile";

		}

	}
}
