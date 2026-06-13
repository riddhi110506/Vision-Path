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

    @Value("${backend.url}")
    private String backendUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendVerificationEmail(String toEmail, String username, String token) {

        String verifyLink = backendUrl + "/api/verify-email?token=" + token;

        String subject = "Verify Your Vision Path Email";

        String content =
                "Hello " + username + ",<br><br>" +
                "Welcome to Vision Path!<br><br>" +
                "Please verify your email by clicking below:<br><br>" +
                "<a href='" + verifyLink + "'>Verify Email</a><br><br>" +
                "After verification, you can login to Vision Path.<br><br>" +
                "Thank You,<br>Vision Path Team";

        sendEmail(toEmail, subject, content);
    }

    public void sendResetPasswordEmail(String toEmail) {

        String subject = "Vision Path - Password Reset";

        String content =
                "Hello,<br><br>" +
                "We received a request to reset your Vision Path password.<br><br>" +
                "Please open the Forgot Password page and set a new password using this Email ID.<br><br>" +
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
