package com.bookstore.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
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
public class UserBilling {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String userBillingName;
	@NotEmpty
	private String userBillingStreet1;
	@NotEmpty
	private String userBillingStreet2;
	@NotEmpty
	private String userBillingCity;
	@NotEmpty
	private String userBillingState;

	private String userBillingCountry;
	@NotEmpty
	private String userBillingPostcode;

	@OneToOne(cascade = CascadeType.ALL)
	private UserPayment userPayment;

}
