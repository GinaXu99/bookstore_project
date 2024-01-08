package com.bookstore.service;

import com.bookstore.model.PaymentCard;
import com.bookstore.model.UserPayment;

public interface PaymentCardService {
	PaymentCard setByUserPayment(UserPayment userPayment, PaymentCard payment);

}
