package com.career.career_guidance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String toEmail, String username) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Welcome to Vision Path");

        message.setText(
                "Hello " + username + ",\n\n" +
                "Welcome to Vision Path!\n\n" +
                "Your account has been created successfully.\n" +
                "You can now login and explore your career roadmap.\n\n" +
                "Thank You,\nVision Path Team"
        );

        mailSender.send(message);
    }

   public void sendResetPasswordEmail(String toEmail) {

    SimpleMailMessage message = new SimpleMailMessage();

    message.setTo(toEmail);
    message.setSubject("Vision Path - Password Reset");

    message.setText(
            "Hello,\n\n" +
            "We received a request to reset your Vision Path password.\n\n" +
            "Click this link to reset your password:\n" +
            "http://127.0.0.1:5500/career-reset-password.html\n\n" +
            "If you did not request this, please ignore this email.\n\n" +
            "Thank You,\nVision Path Team"
    );

    mailSender.send(message);
}
}
