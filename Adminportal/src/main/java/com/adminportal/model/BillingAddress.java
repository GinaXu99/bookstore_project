package com.adminportal.model;

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
public class BillingAddress {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	private String BillingAddressName;
	@NotEmpty
	private String BillingAddressStreet1;
	@NotEmpty
	private String BillingAddressStreet2;
	@NotEmpty
	private String BillingAddressState;
	@NotEmpty
	private String BillingAddressCity;

	private String BillingAddressCountry;
	@NotEmpty
	private String BillingAddressPostcode;

	@OneToOne
	private Order order;

}
