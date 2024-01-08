package com.bookstore.service;

import com.bookstore.model.BillingAddress;
import com.bookstore.model.UserBilling;

public interface BillingAddressService {
	BillingAddress SetByUserBilling(UserBilling userBilling, BillingAddress billingAddress);

}
