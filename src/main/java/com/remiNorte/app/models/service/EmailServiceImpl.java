package com.remiNorte.app.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements IEmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public void sendEmail(SimpleMailMessage email) {

		mailSender.send(email);
	}
	
}
