package com.bookstore.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.model.UserShipping;
import com.bookstore.repository.UserShippingRepository;
import com.bookstore.service.UserShippingService;

@Service
public class UserShippingServiceImpl implements UserShippingService {

	@Autowired
	private UserShippingRepository userShippingRepository;
	
	
	@Override
	public UserShipping findById(Long id) {
		return userShippingRepository.findById(id).get();
	}

	@Override
	public void removeById(Long id) {
		userShippingRepository.deleteById(id);
	}
}
