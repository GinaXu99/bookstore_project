package com.bookstore.serviceImpl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.model.BillingAddress;
import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.model.Order;
import com.bookstore.model.PaymentCard;
import com.bookstore.model.ShippingAddress;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import com.bookstore.repository.OrderRepository;
import com.bookstore.service.CartItemService;
import com.bookstore.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CartItemService cartItemService;

	@Override
	public synchronized Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress,
			BillingAddress billingAddress, PaymentCard paymentCard, String shippingMethod, User user) {

		Order order = new Order();
		order.setBillingAddress(billingAddress);
		order.setShippingAddress(shippingAddress);
		order.setOrderStatus("created");
		order.setPaymentCard(paymentCard);
		order.setShippingMethod(shippingMethod);

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		for (CartItem cartItem : cartItemList) {
			Book book = cartItem.getBook();
			cartItem.setOrder(order);
			book.setInStockNumber(book.getInStockNumber() - cartItem.getQty());
		}

		order.setCartItemList(cartItemList);
		order.setOrderDate(Calendar.getInstance().getTime());
		order.setOrderTotal(shoppingCart.getGrandTotal());
		shippingAddress.setOrder(order);
		billingAddress.setOrder(order);
		paymentCard.setOrder(order);
		order.setUser(user);
		order = orderRepository.save(order);
		return order;
	}

	@Override
	public Order findById(Long Id) {
		return orderRepository.findById(Id).get();
	}

}
