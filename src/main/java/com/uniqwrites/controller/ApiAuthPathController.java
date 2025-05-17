package com.uniqwrites.controller;

import com.uniqwrites.dto.GoogleLoginRequestDTO;
import com.uniqwrites.dto.LoginRequestDTO;
import com.uniqwrites.dto.PasswordResetTokenRequest;
import com.uniqwrites.dto.SignupRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that handles requests to the API-prefixed auth paths.
 * This controller exists to capture API requests that include the '/api/' prefix
 * and forward them to the proper AuthController. This helps with frontend compatibility.
 */
@RestController
@RequestMapping("/api")
public class ApiAuthPathController {    @Autowired
    private AuthController authController;
    
    @Autowired
    private GoogleAuthController googleAuthController;
    
    @Autowired
    private PasswordResetController passwordResetController;

    /**
     * Handle login requests at /api/login path
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        return authController.login(loginRequest);
    }

    /**
     * Handle signup requests at /api/signup path
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO signupRequest) {
        return authController.signup(signupRequest);
    }    /**
     * Handle Google login requests at /api/google/login path
     */
    @PostMapping("/google/login")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginRequestDTO googleLoginRequest) {
        // Forward directly to GoogleAuthController to avoid delegation chain
        return googleAuthController.authenticateWithGoogle(googleLoginRequest);
    }    /**
     * Handle forgot password requests at /api/forgot-password path
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody PasswordResetTokenRequest request) {
        return passwordResetController.forgotPassword(request);
    }
}