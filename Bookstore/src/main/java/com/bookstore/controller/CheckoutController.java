package com.bookstore.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.bookstore.model.BillingAddress;
import com.bookstore.model.CartItem;
import com.bookstore.model.Order;
import com.bookstore.model.PaymentCard;
import com.bookstore.model.ShippingAddress;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import com.bookstore.model.UserBilling;
import com.bookstore.model.UserPayment;
import com.bookstore.model.UserShipping;
import com.bookstore.service.BillingAddressService;
import com.bookstore.service.CartItemService;
import com.bookstore.service.OrderService;
import com.bookstore.service.PaymentCardService;
import com.bookstore.service.ShippingAddressService;
import com.bookstore.service.ShoppingCartService;
import com.bookstore.service.UserPaymentService;
import com.bookstore.service.UserService;
import com.bookstore.service.UserShippingService;
import com.bookstore.utility.AUConstants;
import com.bookstore.utility.MailConstructor;

@Controller
public class CheckoutController {

	private ShippingAddress shippingAddress = new ShippingAddress();
	private BillingAddress billingAddress = new BillingAddress();
	private PaymentCard paymentCard = new PaymentCard();

	@Autowired
	private UserService userService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private ShippingAddressService shippingAddressService;

	@Autowired
	private BillingAddressService billingAddressService;

	@Autowired
	private PaymentCardService paymentCardService;

	@Autowired
	private UserShippingService userShippingService;

	@Autowired
	private UserPaymentService userPaymentService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private MailConstructor mailConstructor;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	

	@GetMapping("/checkout/{id}")
	public String checkout(@PathVariable("id") Long cartId, Model model, Principal principal) {
		User user = userService.findByUserName(principal.getName());
		if (cartId != user.getShoppingCart().getId()) {
			return "badRequestPage";
		}

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
		if (cartItemList.size() == 0) {
			model.addAttribute("emptyCart", true);
			return "forward:/shoppingCart/cart";
		}

		for (CartItem cartItem : cartItemList) {
			if (cartItem.getBook().getInStockNumber() < cartItem.getQty()) {
				model.addAttribute("notEnoughStock", true);
				return "forward:/shoppingCart/cart";
			}
		}

		List<UserShipping> userShippingList = user.getUserShippingList();
		List<UserPayment> userPaymentList = user.getUserPaymentList();
		model.addAttribute("userPaymentList", userPaymentList);
		model.addAttribute("userShippingList", userShippingList);
		
		if (userShippingList.size() == 0) {
			model.addAttribute("emptyShippingList", true);
		} else {
			model.addAttribute("emptyShippingList", false);
		}
		
		if (userPaymentList.size() == 0) {
			model.addAttribute("emptyPaymentList", true);
		} else {
			model.addAttribute("emptyPaymentList", false);
		}

		ShoppingCart shoppingCart = user.getShoppingCart();
		for (UserShipping userShipping : userShippingList) {
			if (userShipping.isUserShippingDefault()) {
				shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			}
		}

		for (UserPayment userPayment : userPaymentList) {
			if (userPayment.isDefaultPayment()) {
				paymentCardService.setByUserPayment(userPayment, paymentCard);
				billingAddressService.SetByUserBilling(userPayment.getUserBilling(), billingAddress);
			}
		}

		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("paymentCard", paymentCard);
		model.addAttribute("billingAddress", billingAddress);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", shoppingCart);

		List<String> stateList = AUConstants.listOfAuStateAbbrevCodes;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);

		model.addAttribute("classActiveShipping", true);
//		if (missingRequiredField) {
//			model.addAttribute("missingRequiredField", true);
//		}

		return "checkout";

	}

	@GetMapping("/setShippingAddress/{userShippingId}")
	public String setShippingAddress(@PathVariable("userShippingId") Long userShippingId, Principal principal,
			Model model) {

		User user = userService.findByUserName(principal.getName());
		UserShipping userShipping = userShippingService.findById(userShippingId);
		if (userShipping.getUser().getId() != user.getId()) {
			return "badRequestPage";
		} else {
			shippingAddressService.setByUserShipping(userShipping, shippingAddress); // key logic
			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("payment", paymentCard);
			model.addAttribute("billingAddress", billingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", user.getShoppingCart());

			List<String> stateList = AUConstants.listOfAuStateAbbrevCodes;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);

			List<UserShipping> userShippingList = user.getUserShippingList();
			List<UserPayment> userPaymentList = user.getUserPaymentList();
			model.addAttribute("userShippingList", userShippingList);
			model.addAttribute("userPaymentList", userPaymentList);
			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("classActiveShipping", true);
			model.addAttribute("emptyShippingList", false);

			if (userPaymentList.size() == 0) {
				model.addAttribute("emptyPaymentList", true);
			} else {
				model.addAttribute("emptyPaymentList", false);
			}
			
			
			for (UserPayment userPayment : userPaymentList) {
				if (userPayment.isDefaultPayment()) {
					paymentCardService.setByUserPayment(userPayment, paymentCard);
					billingAddressService.SetByUserBilling(userPayment.getUserBilling(), billingAddress);
				}
			}
			model.addAttribute("paymentCard", paymentCard);

			return "checkout";
		}
	}

	@GetMapping("/setPaymentMethod/{userPaymentId}")
	public String setPaymentMethod(@PathVariable("userPaymentId") Long userPaymentId, Model model,
			Principal principal) {
		User user = userService.findByUserName(principal.getName());
		UserPayment userPayment = userPaymentService.findById(userPaymentId);
		UserBilling userBilling = userPayment.getUserBilling();

		if (userPayment.getUser().getId() != user.getId()) {
			return "badRequestPage";
		} else {
			paymentCardService.setByUserPayment(userPayment, paymentCard);
			billingAddressService.SetByUserBilling(userBilling, billingAddress);

			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("paymentCard", paymentCard);
			model.addAttribute("billingAddress", billingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", user.getShoppingCart());

			List<String> stateList = AUConstants.listOfAuStateAbbrevCodes;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);

			List<UserShipping> userShippingList = user.getUserShippingList();
			List<UserPayment> userPaymentList = user.getUserPaymentList();
			model.addAttribute("userShippingList", userShippingList);
			model.addAttribute("userPaymentList", userPaymentList);
			model.addAttribute("classActivePayment", true);
			model.addAttribute("emptyPaymentList", false);

			if (userShippingList.size() == 0) {
				model.addAttribute("emptyShippingList", true);
			} else {
				model.addAttribute("emptyShippingList", false);
			}

			return "checkout";
		}
	}

	@PostMapping("/checkout")
	public String checkoutPost(@ModelAttribute("shippingAddress") ShippingAddress shippingAddress,
			@ModelAttribute("billingAddress") BillingAddress billingAddress, @ModelAttribute("paymentCard") PaymentCard paymentCard,
			@ModelAttribute("billingSameAsShipping") String billingSameAsShipping,
			@ModelAttribute("shippingMethod") String shippingMethod, Principal principal, Model model)

	{
		ShoppingCart shoppingCart = userService.findByUserName(principal.getName()).getShoppingCart();
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		model.addAttribute("cartItemList", cartItemList);
		
		if(billingSameAsShipping.equals("true")) {
			billingAddress.setBillingAddressName(shippingAddress.getShippingAddressName());
			billingAddress.setBillingAddressStreet1(shippingAddress.getShippingAddressStreet1());
			billingAddress.setBillingAddressStreet2(shippingAddress.getShippingAddressStreet2());
			billingAddress.setBillingAddressCity(shippingAddress.getShippingAddressCity());
			billingAddress.setBillingAddressState(shippingAddress.getShippingAddressState());
			billingAddress.setBillingAddressCountry(shippingAddress.getShippingAddressCountry());
			billingAddress.setBillingAddressPostcode(shippingAddress.getShippingAddressPostcode());
		}
		
		if(shippingAddress.getShippingAddressCity().isEmpty() || 
				shippingAddress.getShippingAddressName().isEmpty() 
				|| shippingAddress.getShippingAddressPostcode().isEmpty() || shippingAddress.getShippingAddressState().isEmpty()
				|| shippingAddress.getShippingAddressStreet1().isEmpty() || shippingAddress.getShippingAddressStreet2().isEmpty()
				|| shippingAddress.getShippingAddressCity().isEmpty() || paymentCard.getCardNumber().isEmpty() || paymentCard.getCvc() == 0
				|| billingAddress.getBillingAddressCity().isEmpty()  ||
				billingAddress.getBillingAddressName().isEmpty() || billingAddress.getBillingAddressPostcode().isEmpty()
				|| billingAddress.getBillingAddressStreet1().isEmpty() || billingAddress.getBillingAddressStreet2().isEmpty()
				) {
			return "redirect:/checkout?id=" + shoppingCart.getId() + "&missingRequiredField=true";
		}
		
		User user = userService.findByUserName(principal.getName());
		Order order = orderService.createOrder(shoppingCart, shippingAddress, billingAddress, paymentCard, shippingMethod, user);
		mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, order, Locale.ENGLISH));
		shoppingCartService.clearShoppingCart(shoppingCart);
		
		LocalDate today = LocalDate.now();
		LocalDate estimatedDeliveryDate;
		
		if(shippingMethod.equals("groundShipping")) {
			estimatedDeliveryDate = today.plusDays(5);
		}else {
			estimatedDeliveryDate = today.plusDays(3);
		}
		
		model.addAttribute("estimatedDeliveryDate", estimatedDeliveryDate); 
		return "orderSubmitted";

	}

}
