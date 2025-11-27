package org.example.springjwt.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.example.springjwt.entity.User;
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

    // ----------------------------------------
    // SEND EMAIL: Builds HTML + calls EmailService
    // ----------------------------------------
    public void verificationEmail(User user) {

        String subject = "Account Verification";
        String verificationCode = user.getVerificationCode();

        // HTML message for email
        String htmlMessage =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<meta charset='UTF-8'>" +
                        "<title>Verification Code</title>" +
                        "</head>" +
                        "<body style='margin:0; padding:0; font-family:Arial, sans-serif; background:#f4f4f4;'>" +

                        "<div style='max-width:480px; margin:40px auto; background:#ffffff; border-radius:8px; " +
                        "padding:30px; box-shadow:0 4px 12px rgba(0,0,0,0.08);'>" +

                        "<h2 style='margin:0 0 10px; color:#222; font-size:22px; font-weight:600;'>Verify Your Email</h2>" +

                        "<p style='font-size:15px; line-height:1.6; color:#555;'>Use the verification code below to continue:</p>" +

                        "<div style='margin:25px 0; padding:18px; text-align:center; " +
                        "background:#f7f9ff; border:1px solid #dbe3ff; border-radius:6px;'>" +

                        "<span style='font-size:26px; font-weight:700; color:#3b5bdb; letter-spacing:3px;'>" +
                        verificationCode + "</span>" +
                        "</div>" +

                        "<p style='font-size:13px; color:#888;'>If you didn't request this, please ignore this email.</p>" +

                        "</div>" +
                        "</body>" +
                        "</html>";

        // Attempt to send mail
        try {
            sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace(); // for debugging — replace with logger in production
        }
    }
}
