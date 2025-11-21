package org.example.springjwt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    // Inject the Gmail username from application.properties
    @Value("${spring.mail.username}")
    private String username;

    // Inject the Gmail password or app password
    @Value("${spring.mail.password}")
    private String password;

    /**
     * Configure and return a JavaMailSender bean.
     * Spring Boot will use this bean whenever you send emails.
     *
     * Gmail SMTP details:
     * host -> smtp.gmail.com
     * port -> 587 (TLS)
     * auth  -> true
     * starttls -> true
     *
     * The username and password come from properties file.
     */
    @Bean
    public JavaMailSender getJavaMailSender() {

        // Main mail sender implementation
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Gmail SMTP server details
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        // Credentials injected via @Value
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        // Additional mail properties
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");  // SMTP protocol
        props.put("mail.smtp.auth", "true");           // Enable authentication
        props.put("mail.smtp.starttls.enable", "true");// Enable TLS encryption
        props.put("mail.debug", "true");               // Enable logs (useful for debugging)

        return mailSender;
    }
}
