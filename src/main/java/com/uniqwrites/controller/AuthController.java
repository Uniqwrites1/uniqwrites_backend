package com.uniqwrites.controller;

import com.uniqwrites.dto.AuthResponseDTO;
import com.uniqwrites.dto.GoogleLoginRequestDTO;
import com.uniqwrites.dto.LoginRequestDTO;
import com.uniqwrites.dto.PasswordResetTokenRequest;
import com.uniqwrites.dto.SignupRequestDTO;
import com.uniqwrites.model.Role;
import com.uniqwrites.model.User;
import com.uniqwrites.security.JwtUtil;
import com.uniqwrites.service.UserService;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private GoogleAuthController googleAuthController;
    
    @Autowired
    private PasswordResetController passwordResetController;
    
    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        return ResponseEntity.ok().body("Authentication API is working!");
    }

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;    
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO signupRequest) {
        try {
            User user = userService.registerUser(signupRequest);
            // Generate token for auto-login after signup
            String token = jwtUtil.generateToken(user.getEmail());
            
            AuthResponseDTO response = AuthResponseDTO.success(
                token,
                user.getRole().name(),
                user.getName(),
                user.getEmail()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(AuthResponseDTO.error(e.getMessage()));
        }
    }    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            Optional<User> userOpt = userService.authenticateUser(loginRequest);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                String token = jwtUtil.generateToken(user.getEmail());
                
                AuthResponseDTO response = AuthResponseDTO.success(
                    token, 
                    user.getRole().name(), 
                    user.getName(), 
                    user.getEmail()
                );
                
                return ResponseEntity.ok(response);
            } else {
                throw new BadCredentialsException("Invalid credentials");
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(AuthResponseDTO.error("Invalid email or password"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(AuthResponseDTO.error("Authentication error: " + e.getMessage()));
        }
    }      /**
     * This method was removed to resolve mapping conflicts with GoogleAuthController
     * Google authentication is now handled by GoogleAuthController.authenticateWithGoogle
     */
    // NOTE: Google authentication endpoint was moved to GoogleAuthController
    
    /**
     * Handle forgot password requests
     * This method is deprecated. Use PasswordResetController instead.
     */
    // @PostMapping("/forgot-password") - Removed to resolve mapping conflict
    public ResponseEntity<?> forgotPassword(@RequestBody Object forgotPasswordRequest) {
        // This method is now handled by PasswordResetController
        // Kept here for backward compatibility with ApiAuthPathController
        if (forgotPasswordRequest instanceof Map) {
            try {
                Map<String, Object> requestMap = (Map<String, Object>) forgotPasswordRequest;
                String email = (String) requestMap.get("email");
                PasswordResetTokenRequest request = new PasswordResetTokenRequest();
                request.setEmail(email);
                return passwordResetController.forgotPassword(request);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(
                    AuthResponseDTO.error("Failed to process password reset request: " + e.getMessage())
                );
            }
        }
        return ResponseEntity.badRequest().body(
            AuthResponseDTO.error("Invalid password reset request format")
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // For JWT, logout can be handled client-side by discarding the token
        // Optionally, implement token blacklist here
        return ResponseEntity.ok("User logged out");
    }

    @GetMapping("/roles")
    public List<String> getRoles() {
        return Arrays.stream(Role.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@RequestBody SignupRequestDTO studentRequest) {
        try {
            User user = userService.registerStudent(studentRequest);
            return ResponseEntity.status(201).body("Student registered");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register/tutor")
    public ResponseEntity<?> registerTutor(@RequestBody SignupRequestDTO tutorRequest) {
        try {
            User user = userService.registerTutor(tutorRequest);
            return ResponseEntity.status(201).body("Tutor registered (pending approval)");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register/school")
    public ResponseEntity<?> registerSchool(@RequestBody SignupRequestDTO schoolRequest) {
        try {
            User user = userService.registerSchool(schoolRequest);
            return ResponseEntity.status(201).body("School admin registered");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register/sponsor")
    public ResponseEntity<?> registerSponsor(@RequestBody SignupRequestDTO sponsorRequest) {
        try {
            User user = userService.registerSponsor(sponsorRequest);
            return ResponseEntity.status(201).body("Sponsor registered");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Google login handler - delegates to GoogleAuthController
     * This method exists for backward compatibility with path controllers
     * that expect this method to exist in AuthController
     * 
     * @param googleLoginRequest The request containing Google authentication details
     * @return Authentication response with JWT token
     */
    public ResponseEntity<?> googleLogin(@RequestBody Object googleLoginRequest) {
        // Convert Object to GoogleLoginRequestDTO and delegate
        if (googleLoginRequest instanceof Map) {
            try {
                // Handle map conversion for path controllers
                Map<String, Object> requestMap = (Map<String, Object>) googleLoginRequest;
                GoogleLoginRequestDTO dto = new GoogleLoginRequestDTO();
                
                // Extract properties from map
                if (requestMap.containsKey("email")) dto.setEmail((String) requestMap.get("email"));
                if (requestMap.containsKey("name")) dto.setName((String) requestMap.get("name"));
                if (requestMap.containsKey("googleId")) dto.setGoogleId((String) requestMap.get("googleId"));
                if (requestMap.containsKey("role")) dto.setRole((String) requestMap.get("role"));                if (requestMap.containsKey("token")) dto.setToken((String) requestMap.get("token"));
                if (requestMap.containsKey("picture")) dto.setPicture((String) requestMap.get("picture"));
                
                return googleAuthController.authenticateWithGoogle(dto);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(
                    AuthResponseDTO.error("Invalid Google login request format: " + e.getMessage()));
            }
        }
        
        return ResponseEntity.badRequest().body(
            AuthResponseDTO.error("Unsupported Google login request format"));
    }
}
