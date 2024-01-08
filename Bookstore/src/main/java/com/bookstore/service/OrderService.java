package com.bookstore.service;

import com.bookstore.model.BillingAddress;
import com.bookstore.model.Order;
import com.bookstore.model.PaymentCard;
import com.bookstore.model.ShippingAddress;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;

public interface OrderService {
	
	Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress,
			BillingAddress billingAddress, PaymentCard aymentCard, 
			String shippingMethod,
			User user);
	
	Order findById(Long Id);

}
