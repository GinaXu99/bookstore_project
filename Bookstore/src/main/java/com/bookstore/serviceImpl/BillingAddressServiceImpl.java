package com.bookstore.serviceImpl;

import org.springframework.stereotype.Service;

import com.bookstore.model.BillingAddress;
import com.bookstore.model.UserBilling;
import com.bookstore.service.BillingAddressService;

@Service
public class BillingAddressServiceImpl implements BillingAddressService {

	@Override
	public BillingAddress SetByUserBilling(UserBilling userBilling, BillingAddress billingAddress) {
		billingAddress.setBillingAddressName(userBilling.getUserBillingName());
		billingAddress.setBillingAddressCountry(userBilling.getUserBillingCountry());
		billingAddress.setBillingAddressStreet1(userBilling.getUserBillingStreet1());
		billingAddress.setBillingAddressStreet2(userBilling.getUserBillingStreet2());
		billingAddress.setBillingAddressState(userBilling.getUserBillingState());
		billingAddress.setBillingAddressPostcode(userBilling.getUserBillingPostcode());
		billingAddress.setBillingAddressCity(userBilling.getUserBillingCity());
		return billingAddress;
	}

}
