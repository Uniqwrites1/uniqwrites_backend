package com.uniqwrites.controller;

import com.uniqwrites.dto.LoginRequestDTO;
import com.uniqwrites.dto.SignupRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that handles requests to the root-level auth paths.
 * This controller exists to capture API requests that don't include the '/api/' prefix
 * and forward them to the proper AuthController. This helps with frontend compatibility.
 */
@RestController
public class AuthPathController {

    @Autowired
    private AuthController authController;

    /**
     * Handle login requests at /login path (without /api/auth prefix)
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        return authController.login(loginRequest);
    }

    /**
     * Handle signup requests at /signup path (without /api/auth prefix)
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO signupRequest) {
        return authController.signup(signupRequest);
    }
    
    /**
     * Handle Google login requests at /google/login path (without /api/auth prefix)
     */
    @PostMapping("/google/login")
    public ResponseEntity<?> googleLogin(@RequestBody Object googleLoginRequest) {
        // Forward to the proper endpoint
        return authController.googleLogin(googleLoginRequest);
    }

    /**
     * Handle forgot password requests at /forgot-password path (without /api/auth prefix)
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Object forgotPasswordRequest) {
        return authController.forgotPassword(forgotPasswordRequest);
    }
}
