package com.uniqwrites.controller;

import com.uniqwrites.dto.AuthResponseDTO;
import com.uniqwrites.dto.GoogleLoginRequestDTO;
import com.uniqwrites.model.User;
import com.uniqwrites.model.Role;
import com.uniqwrites.repository.UserRepository;
import com.uniqwrites.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * Controller for handling Google authentication
 */
@RestController
@RequestMapping("/api/auth")
public class GoogleAuthController {

    private static final Logger logger = LoggerFactory.getLogger(GoogleAuthController.class);
    
    @Autowired
    private JwtTokenProvider tokenProvider;
      @Autowired
    private UserRepository userRepository;
      @Value("${spring.security.oauth2.client.registration.google.client-id:dummy-client-id}")
    private String googleClientId;
    
    @Autowired
    private RestTemplate restTemplate;
      /**
     * Handle Google login requests
     * This endpoint receives Google tokens from the frontend and verifies them
     * NOTE: This is the official implementation for Google authentication
     * The endpoint is changed to /google/authenticate to avoid conflicts with AuthController
     */
    @PostMapping("/google/authenticate")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody GoogleLoginRequestDTO request) {
        try {
            logger.info("Processing Google authentication request for: {}", request.getEmail());            // Verify the Google token - in production, you would use Google's API
            // to validate the token. For now, we'll implement a simple verification
            if (!verifyGoogleToken(request.getToken(), request.getEmail())) {
                logger.error("Invalid Google token for: {}", request.getEmail());
                return ResponseEntity.badRequest().body(new AuthResponseDTO(false, null, 
                    "Invalid Google authentication token", null, null, null));
            }
            
            // First try to find by googleId if provided
            Optional<User> userOptional = Optional.empty();
            if (request.getGoogleId() != null && !request.getGoogleId().isEmpty()) {
                userOptional = userRepository.findByGoogleId(request.getGoogleId());
            }
            
            // If not found by googleId, try by email
            if (userOptional.isEmpty()) {
                userOptional = userRepository.findByEmail(request.getEmail());
            }
            
            User user;
            
            if (userOptional.isPresent()) {                // Existing user - update Google information
                user = userOptional.get();
                user.setGoogleId(request.getGoogleId());
                user.setImageUrl(request.getPicture());
                user.setActive(true);
                userRepository.save(user);  // Save the updated user information
                logger.info("Existing user logged in with Google: {}", request.getEmail());
            } else {
                // Create a new user if one doesn't exist
                user = new User();
                user.setEmail(request.getEmail());
                user.setName(request.getName());
                
                // Use the provided role or default to STUDENT
                if (request.getRole() != null) {
                    try {
                        user.setRole(Role.valueOf(request.getRole().toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        user.setRole(Role.STUDENT);
                    }
                } else {
                    user.setRole(Role.STUDENT);
                }
                
                user.setActive(true);
                user.setGoogleId(request.getGoogleId());
                
                userRepository.save(user);
                logger.info("New user registered via Google: {}", request.getEmail());
            }
            
            // Generate JWT token
            String token = tokenProvider.generateTokenFromOAuth2(
                user.getEmail(),
                user.getName(),
                user.getRole().name()
            );
            
            // Return successful authentication response
            return ResponseEntity.ok(AuthResponseDTO.success(
                token,
                user.getRole().name(),
                user.getName(),
                user.getEmail()
            ));
            
        } catch (Exception e) {
            logger.error("Error during Google authentication", e);
            return ResponseEntity.badRequest().body(AuthResponseDTO.error(
                "Google authentication failed: " + e.getMessage()
            ));
        }    }
    
    /**
     * Verify the Google token
     * In production, this should call Google's tokeninfo endpoint
     * 
     * @param token The Google ID token to verify
     * @param email The email associated with the token
     * @return true if token is valid, false otherwise
     */
    private boolean verifyGoogleToken(String token, String email) {
        if (token == null || token.isEmpty()) {
            return false;
        }
          try {
            // In production, you would validate the token with Google's API
            /* Uncomment for production use:
            String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + token;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null) {
                // Verify the token is not expired
                if (response.containsKey("exp") && System.currentTimeMillis() / 1000 > Long.parseLong(response.get("exp").toString())) {
                    logger.error("Token expired");
                    return false;
                }
                
                // Verify the audience matches your client ID
                if (!googleClientId.equals(response.get("aud"))) {
                    logger.error("Token audience mismatch");
                    return false;
                }
                
                // Verify the email
                if (email != null && !email.equals(response.get("email"))) {
                    logger.error("Token email mismatch");
                    return false;
                }
                
                return true;
            }
            */
            
            // For development/testing, we'll do a simple validation
            // In production, remove this and uncomment the above code
            if ("mock_google_token".equals(token) || token.startsWith("mock_google_token_")) {
                logger.info("Development mode: Accepting mock token for {}", email);
                return true;
            }
            
            // TODO: Implement proper token verification for production
            // Here you would check:
            // 1. If the token is properly signed
            // 2. If the token is not expired
            // 3. If the audience matches your client ID
            // 4. If the email in the token matches the email provided
            
            logger.warn("Token validation not fully implemented yet");
            return true; // For development only! Remove in production!
            
        } catch (Exception e) {
            logger.error("Error validating Google token", e);
            return false;
        }
    }

    /**
     * Alternative endpoint for Google login
     * This maintains compatibility with the AuthController's authentication flow
     */
    public ResponseEntity<?> handleGoogleLogin(@RequestBody GoogleLoginRequestDTO request) {
        return authenticateWithGoogle(request);
    }
}
// Enum for user roles
// public enum Role {
//     ADMIN,
//     USER,
//     STUDENT
// }
