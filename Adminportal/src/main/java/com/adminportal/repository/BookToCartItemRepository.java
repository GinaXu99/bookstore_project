package com.adminportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adminportal.model.BookToCartItem;

import jakarta.transaction.Transactional;

@Transactional
public interface BookToCartItemRepository extends JpaRepository<BookToCartItem, Long> {
	
	void deleteCartItemById(Long id);
}
