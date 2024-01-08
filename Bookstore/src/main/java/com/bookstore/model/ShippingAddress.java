package com.bookstore.model;


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
public class ShippingAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty
	private String shippingAddressName;
	@NotEmpty
	private String shippingAddressStreet1;
	@NotEmpty
	private String shippingAddressStreet2;
	@NotEmpty
	private String shippingAddressCity;
	@NotEmpty
	private String shippingAddressState;

	private String shippingAddressCountry;
	@NotEmpty
	private String shippingAddressPostcode;

	@OneToOne
	private Order order;

}
