package com.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.model.BookToCartItem;

import jakarta.transaction.Transactional;

@Transactional
public interface BookToCartItemRepository extends JpaRepository<BookToCartItem, Long> {
	
	void deleteCartItemById(Long id);
	void deleteByBookId(Long id);
}
