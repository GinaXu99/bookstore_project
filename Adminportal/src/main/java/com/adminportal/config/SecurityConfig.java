package com.adminportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.adminportal.utility.SecurityUtility;

@Configuration
public class SecurityConfig {
	
	@Bean
	BCryptPasswordEncoder passwordEncoder2() {
		return SecurityUtility.passwordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/css/**", "/js/**", "/images/**",
				"/fonts/**", "/cdn/**", "/login/**", "/**", "/admin/**", "/bookDetail/**", "/bookInfo/**",
				"/deleteBook/**").permitAll());
        http.formLogin(login -> login
                .loginPage("/login")
                .permitAll())
                .logout(logout -> logout
                        .permitAll()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("remember-me"))
                .csrf(csrf -> csrf.disable());
        
		return http.build();
	};
}
