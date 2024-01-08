package com.bookstore.serviceImpl;

import org.springframework.stereotype.Service;

import com.bookstore.model.PaymentCard;
import com.bookstore.model.UserPayment;
import com.bookstore.service.PaymentCardService;
@Service
public class PaymentCardServiceImpl implements PaymentCardService {

	@Override
	public PaymentCard setByUserPayment(UserPayment userPayment, PaymentCard paymentCard) {
		paymentCard.setCardName(userPayment.getCardName());
		paymentCard.setCardNumber(userPayment.getCardNumber());
		paymentCard.setHolderName(userPayment.getHolderName());
		paymentCard.setType(userPayment.getType());
		paymentCard.setExpiryMonth(userPayment.getExpiryMonth());
		paymentCard.setExpiryYear(userPayment.getExpiryYear());
		paymentCard.setCvc(userPayment.getCvc());
		return paymentCard;
	}

}
