package com.adminportal.model;



import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class UserPayment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String type;
	@NotEmpty
	private String cardName;
	@NotEmpty
	private String cardNumber;
	@NotNull
	private int expiryMonth;
	@NotNull
	private int expiryYear;
	@NotNull
	private int cvc;
	@NotEmpty
	private String holderName;

	private boolean defaultPayment;

	@ManyToOne
	@JoinColumn(name = "bookstoreuser_id")
	private User user;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "userPayment")
	private UserBilling userBilling;
}
