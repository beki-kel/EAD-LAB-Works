package com.alenedaj.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String verificationCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px;'>" +
                "<h2 style='color: #333;'>Email Verification</h2>" +
                "<p>Thank you for signing up! Please use the following code to verify your email:</p>" +
                "<h3 style='background: #f4f4f4; padding: 10px; display: inline-block;'>" + verificationCode + "</h3>" +
                "<p>If you did not request this, please ignore this email.</p>" +
                "<p style='color: #888;'>Alenedaj Team</p>" +
                "</div>";

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
