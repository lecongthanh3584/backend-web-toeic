package com.backend.spring.services.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailSender;

    public void sendEmail(String to, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.setRecipients(MimeMessage.RecipientType.TO, new InternetAddress[]{new InternetAddress(to)});
            message.setFrom(new InternetAddress(emailSender));
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");

            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
