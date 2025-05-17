package com.uniqwrites.controller;

import com.uniqwrites.dto.ApiResponse;
import com.uniqwrites.dto.PasswordResetRequest;
import com.uniqwrites.dto.PasswordResetTokenRequest;
import com.uniqwrites.model.User;
import com.uniqwrites.repository.UserRepository;
import com.uniqwrites.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller for password reset functionality
 */
@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmailService emailService;
      @Autowired
    private PasswordEncoder passwordEncoder;
      
    /**
     * Handle legacy forgot-password requests (for backward compatibility)
     * @param request contains the email address for password reset
     * @return response indicating success or failure
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody PasswordResetTokenRequest request) {
        // Delegate to the new endpoint implementation
        return requestPasswordReset(request);
    }
      
    /**
     * Request a password reset token
     * @param request contains the email address for password reset
     * @return response indicating success or failure
     */
    @PostMapping("/password/request-reset")
    public ResponseEntity<?> requestPasswordReset(@RequestBody PasswordResetTokenRequest request) {
        String email = request.getEmail();
        logger.info("Password reset requested for: {}", email);
        
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isEmpty()) {
            // Don't reveal that the email doesn't exist
            return ResponseEntity.ok(new ApiResponse(true, 
                "If your email exists in our system, you will receive a password reset link shortly."));
        }
        
        User user = userOptional.get();
        
        // Generate a reset token
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1)); // Token valid for 1 hour
        
        userRepository.save(user);
        
        // Send the password reset email
        try {
            emailService.sendPasswordResetEmail(email, resetToken, user.getName());
            return ResponseEntity.ok(new ApiResponse(true, 
                "If your email exists in our system, you will receive a password reset link shortly."));
        } catch (Exception e) {
            logger.error("Error sending password reset email", e);
            return ResponseEntity.badRequest().body(new ApiResponse(false, 
                "Error sending password reset email. Please try again later."));
        }
    }
    
    /**
     * Reset a password using a valid token
     * @param request contains the token and new password
     * @return response indicating success or failure
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
        String token = request.getToken();
        String newPassword = request.getPassword();
        
        logger.info("Password reset attempt with token: {}", token);
        
        Optional<User> userOptional = userRepository.findByResetToken(token);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, 
                "Invalid password reset token. Please request a new password reset."));
        }
        
        User user = userOptional.get();
        
        // Check if the token has expired
        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, 
                "Password reset token has expired. Please request a new password reset."));
        }
        
        // Update the password
        user.setPassword(passwordEncoder.encode(newPassword));
        
        // Clear the reset token
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        
        userRepository.save(user);
        
        logger.info("Password reset successful for user: {}", user.getEmail());
        
        return ResponseEntity.ok(new ApiResponse(true, 
            "Your password has been reset successfully. You can now log in with your new password."));
    }
    
    /**
     * Validate a reset token without actually resetting the password
     * @param token the reset token to validate
     * @return response indicating whether the token is valid
     */
    @GetMapping("/validate-reset-token")
    public ResponseEntity<?> validateResetToken(@RequestParam("token") String token) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, 
                "Invalid password reset token."));
        }
        
        User user = userOptional.get();
        
        // Check if the token has expired
        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, 
                "Password reset token has expired."));
        }
        
        return ResponseEntity.ok(new ApiResponse(true, "Valid reset token."));
    }
}
