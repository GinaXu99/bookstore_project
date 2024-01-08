package com.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.model.CartItem;
import com.bookstore.model.Order;
import com.bookstore.model.ShoppingCart;

import jakarta.transaction.Transactional;

@Transactional
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);

	List<CartItem> findByOrder(Order order);

	List<CartItem> findByBookId(Long bookid);

}
