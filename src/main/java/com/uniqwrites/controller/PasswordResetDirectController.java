package com.uniqwrites.controller;

import com.uniqwrites.dto.ApiResponse;
import com.uniqwrites.dto.PasswordResetRequest;
import com.uniqwrites.dto.PasswordResetTokenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling root-level password reset requests
 * This controller exists to maintain compatibility with frontends that expect
 * the password reset endpoints at the root path rather than under /api/auth
 */
@RestController
public class PasswordResetDirectController {

    @Autowired
    private PasswordResetController passwordResetController;
      /**
     * Request a password reset token
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody PasswordResetTokenRequest request) {
        return passwordResetController.requestPasswordReset(request);
    }
    
    /**
     * Reset a password using a valid token
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
        return passwordResetController.resetPassword(request);
    }
    
    /**
     * Validate a reset token
     */
    @GetMapping("/validate-reset-token")
    public ResponseEntity<?> validateResetToken(@RequestParam("token") String token) {
        return passwordResetController.validateResetToken(token);
    }
}
