package com.bookstore.config;

import java.util.Properties;
import org.springframework.context.annotation.Configuration;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;

@Configuration
public class SessionConfig {

	Session getSession() {
		String host = "localhost";
		String username = "gina123321gina@gmail.com";
		String password = "nzqh pkda jvyf vjsc";

		Properties prop = new Properties();
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.host", host);
		prop.put("mail.smtp.port", 465);
		prop.put("mail.smtp.ssl.trust", host);
		prop.put("mail.transport.protocol", "smtp");

		Session session = Session.getInstance(prop, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		return session;
	}
}
