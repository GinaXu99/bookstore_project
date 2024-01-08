package com.adminportal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class UserShipping {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty
	private String userShippingName;
	@NotEmpty
	private String userShippingStreet1;
	@NotEmpty
	private String userShippingStreet2;
	@NotEmpty
	private String userShippingCity;
	@NotEmpty
	private String userShippingState;

	private String userShippingCountry;
	@NotEmpty
	private String userShippingPostcode;
	private boolean userShippingDefault; //default shipping

	@ManyToOne
	@JoinColumn(name = "bookstoreuser_id")
	private User user;

}
