package com.uniqwrites.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;
      @Autowired(required = false)
    private TemplateEngine templateEngine;
    
    @Value("${spring.mail.username:noreply@uniqwrites.com}")
    private String fromEmail;

    /**
     * Send a simple email
     */
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    /**
     * Send an email with string template formatting
     */
    public void sendEmailWithTemplate(String to, String subject, String template, Object... args) {
        String formattedText = String.format(template, args);
        sendEmail(to, subject, formattedText);
    }
    
    /**
     * Send email with HTML template
     *
     * @param to recipient email address
     * @param subject email subject
     * @param templateName name of the Thymeleaf template
     * @param variables variables to be used in the template
     * @throws MessagingException if there is an error sending the email
     */
    public void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> variables) 
            throws MessagingException {
        
        if (templateEngine == null) {
            // Fall back to plain text if template engine is not configured
            sendEmail(to, subject, "Please reset your password using the following link: " 
                    + variables.getOrDefault("resetUrl", ""));
            return;
        }
        
        // Prepare the evaluation context
        Context context = new Context();
        context.setVariables(variables);
        
        // Process the template
        String htmlContent = templateEngine.process(templateName, context);
        
        // Send the email
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, 
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, 
                StandardCharsets.UTF_8.name());
        
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        
        emailSender.send(message);
    }

    /**
     * Send password reset email
     *
     * @param to recipient email address
     * @param resetToken reset token
     * @param name user's name
     * @throws MessagingException if there is an error sending the email
     */
    public void sendPasswordResetEmail(String to, String resetToken, String name) {
        try {
            Map<String, Object> variables = Map.of(
                "name", name,
                "resetToken", resetToken,
                "resetUrl", "http://localhost:5173/reset-password?token=" + resetToken
            );
            
            sendHtmlEmail(to, "Uniqwrites Password Reset", "password-reset-email", variables);
        } catch (MessagingException e) {
            // Fall back to simple email if HTML email fails
            String resetUrl = "http://localhost:5173/reset-password?token=" + resetToken;
            String message = String.format("Hello %s,\n\nYou have requested to reset your password. "
                    + "Please click the link below to set a new password:\n\n%s\n\n"
                    + "If you did not request a password reset, please ignore this email.\n\n"
                    + "Regards,\nUniqwrites Team", name, resetUrl);
            
            sendEmail(to, "Uniqwrites Password Reset", message);
        }
    }
}
