package com.uniqwrites.controller;

import com.uniqwrites.dto.GoogleLoginRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling root-level Google login requests
 * This controller exists to maintain compatibility with frontends that expect
 * the Google auth endpoint at the root path rather than under /api/auth
 */
@RestController
@RequestMapping("/api/oauth") // Different path to avoid conflict
public class GoogleLoginController {

    @Autowired
    private GoogleAuthController googleAuthController;
      /**
     * Delegate to the main GoogleAuthController
     */    @PostMapping("/google/login")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginRequestDTO request) {
        return googleAuthController.authenticateWithGoogle(request);
    }
}
