package com.bookstore.service;

import com.bookstore.model.UserShipping;

public interface UserShippingService {
	
	UserShipping findById(Long id);
	void removeById(Long id);
}
