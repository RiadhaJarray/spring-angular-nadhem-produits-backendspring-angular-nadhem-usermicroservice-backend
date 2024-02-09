package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class EmailService implements EmailSender {


	//nous avons remplacer "@Autowired" par "@AllArgsConstructor" de lombok : injection par constructeur
	//@Autowired
	private final JavaMailSender mailSender;

	@Override
	public void sendEmail(String to, String email) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
			
			helper.setText(email, true);
			helper.setTo(to);
			helper.setSubject("Confirm your email");
			helper.setFrom("riadh.jarray@web-atrio.com");
			mailSender.send(mimeMessage);
			
		} catch (MessagingException e) {
			throw new IllegalStateException("failed to send email");
		} 
	}

}
