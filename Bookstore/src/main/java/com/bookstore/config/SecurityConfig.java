package com.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


import com.bookstore.utility.SecurityUtility;

@Configuration
public class SecurityConfig {

	String[] staticResources = { "/css/**", "/image/**", "/images/**", "/fonts/**", "/js/**" };

	@Bean
	BCryptPasswordEncoder passwordEncoder2() {
		return SecurityUtility.passwordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests((authorize) -> authorize.requestMatchers(staticResources).permitAll()
				.requestMatchers("/login/**", "/logout/**", "/forgetPassword/**", "/error/**", "/smtp/**", "/", "/mail",
						"/bookShelf/**", "/bookDetail/**", "/newUser/**", "/listOfCreditCards/**",
						"/addNewCreditCard/**", "/removeCreditCard/**", "/updateCreditCard/**", "/setDefaultPayment/**",
						"/listOfShippingAddresses/**", "/addNewShippingAddress/**", "/updateUserShipping/**",
						"/setDefaultShippingAddress/**", "/removeUserShipping/**", "/checkout/**",
						"/setShippingAddress/**", "/setPaymentMethod/**", "/updateUserInfo/**", "/checkout/**",
						"/orderDetail/**", "/myProfile/**", "/shoppingCart/**", "/showFirstPage/**")
				.permitAll());

		http.formLogin(login -> login.loginPage("/login").permitAll()).logout(logout -> logout.permitAll()
				.logoutUrl("/logout").logoutSuccessUrl("/login").deleteCookies("remember-me"))
				.csrf(csrf -> csrf.disable());
		
		return http.build();
	};
}
