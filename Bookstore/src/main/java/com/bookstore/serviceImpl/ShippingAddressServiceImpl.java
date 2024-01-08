package com.bookstore.serviceImpl;

import org.springframework.stereotype.Service;

import com.bookstore.model.ShippingAddress;
import com.bookstore.model.UserShipping;
import com.bookstore.service.ShippingAddressService;
@Service
public class ShippingAddressServiceImpl implements ShippingAddressService {


	public ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress) {
		shippingAddress.setShippingAddressName(userShipping.getUserShippingName());
		shippingAddress.setShippingAddressStreet1(userShipping.getUserShippingStreet1());
		shippingAddress.setShippingAddressStreet2(userShipping.getUserShippingStreet2());
		shippingAddress.setShippingAddressCity(userShipping.getUserShippingCity());
		shippingAddress.setShippingAddressState(userShipping.getUserShippingState());
		shippingAddress.setShippingAddressPostcode(userShipping.getUserShippingPostcode());
		shippingAddress.setShippingAddressCountry(userShipping.getUserShippingCountry());
		return shippingAddress;
	}



}
