package com.career.career_guidance.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${frontend.url}")
    private String frontendUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendWelcomeEmail(String toEmail, String username) {

        String subject = "Welcome to Vision Path";

        String content =
                "Hello " + username + ",<br><br>" +
                "Welcome to Vision Path!<br><br>" +
                "Your account has been created successfully.<br>" +
                "You can now login and explore your career roadmap.<br><br>" +
                "Thank You,<br>Vision Path Team";

        sendEmail(toEmail, subject, content);
    }

    public void sendResetPasswordEmail(String toEmail) {

        String resetLink = frontendUrl + "/career-reset-password.html";

        String subject = "Vision Path - Password Reset";

        String content =
                "Hello,<br><br>" +
                "We received a request to reset your Vision Path password.<br><br>" +
                "Click this link to reset your password:<br>" +
                "<a href='" + resetLink + "'>Reset Password</a><br><br>" +
                "If you did not request this, please ignore this email.<br><br>" +
                "Thank You,<br>Vision Path Team";

        sendEmail(toEmail, subject, content);
    }

    private void sendEmail(String toEmail, String subject, String htmlContent) {

        String url = "https://api.brevo.com/v3/smtp/email";

        String body = """
                {
                  "sender": {
                    "name": "Vision Path",
                    "email": "%s"
                  },
                  "to": [
                    {
                      "email": "%s"
                    }
                  ],
                  "subject": "%s",
                  "htmlContent": "%s"
                }
                """.formatted(
                senderEmail,
                toEmail,
                subject,
                htmlContent.replace("\"", "\\\"")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", brevoApiKey);

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(url, request, String.class);
    }
}
