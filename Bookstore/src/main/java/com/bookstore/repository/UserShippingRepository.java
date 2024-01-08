package com.bookstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.model.UserShipping;

public interface UserShippingRepository extends JpaRepository<UserShipping, Long> {

	Optional<UserShipping> findById(Long id);
	void removeById(Long id);
}
