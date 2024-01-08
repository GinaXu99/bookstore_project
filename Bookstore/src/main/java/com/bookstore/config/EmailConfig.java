package com.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {

	@Bean
	JavaMailSender getJavaMailSender() {
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(465);
		mailSender.setUsername("gina123321gina@gmail.com");
		mailSender.setPassword("nzqh pkda jvyf vjsc");
		mailSender.setProtocol("smtp");

		mailSender.getJavaMailProperties().setProperty("mail.smtp.auth", "true");
		mailSender.getJavaMailProperties().setProperty("mail.smtp.starttls.enable", "true");
		mailSender.getJavaMailProperties().setProperty("mail.debug", "true");
		mailSender.getJavaMailProperties().setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		mailSender.getJavaMailProperties().setProperty("mail.debug", "true");
		mailSender.getJavaMailProperties().setProperty("mail.smtp.socketFactory.fallback", "false");
		mailSender.getJavaMailProperties().setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");
		mailSender.getJavaMailProperties().setProperty("mail.transport.protocol", "smtp");

		return mailSender;
	}
}
