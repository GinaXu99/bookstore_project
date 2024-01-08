package com.bookstore.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="user_order") //order is a keyword in DB
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Date orderDate;
	private Date shippingDate;
	private String shippingMethod;
	private String orderStatus;
	private BigDecimal orderTotal;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<CartItem> cartItemList;

	@OneToOne(cascade = CascadeType.ALL)
	private ShippingAddress shippingAddress;
	
	@OneToOne(cascade = CascadeType.ALL)
	private BillingAddress billingAddress;

	@OneToOne(cascade = CascadeType.ALL)
	private PaymentCard paymentCard;

	@ManyToOne(cascade = CascadeType.ALL)
	private User user;

}
