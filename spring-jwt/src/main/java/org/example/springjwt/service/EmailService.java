package org.example.springjwt.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    // Spring’s mail sender interface for sending any type of email
    private final JavaMailSender mailSender;

    /**
     * Sends an HTML email (verification mail in your case).
     *
     * @param to      The recipient email address
     * @param subject Subject line of the email
     * @param body    HTML content for the email body
     */
    public void sendVerificationEmail(String to, String subject, String body) throws MessagingException {

        // Create a MIME message object (supports HTML, attachments, inline content, etc.)
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        // Helper class simplifies working with MimeMessage
        // 'true' enables multipart mode → supports HTML + attachments if needed
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        // Set receiver
        helper.setTo(to);

        // Set email subject
        helper.setSubject(subject);

        // Set email body
        // The second parameter 'true' tells Spring this is HTML content
        helper.setText(body, true);

        // Send the email
        mailSender.send(mimeMessage);
    }
}
