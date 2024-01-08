package com.bookstore.service;

import java.util.List;

import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.model.Order;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;

public interface CartItemService {
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	CartItem updateCartItem(CartItem cartItem);
	void addBookToCartItem(Book book, User user, int bookqty);
	void removeCartItem(Long id);
	CartItem findById(Long id);
	void save(CartItem cartItem);
	List<CartItem> findByOrder(Order order);
	
	void emptyCartItemsByBookId(Long bookid);

}
