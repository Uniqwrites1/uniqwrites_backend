package com.uniqwrites.controller;

import com.uniqwrites.dto.AuthResponseDTO;
import com.uniqwrites.dto.LoginRequestDTO;
import com.uniqwrites.dto.SignupRequestDTO;
import com.uniqwrites.model.Role;
import com.uniqwrites.model.User;
import com.uniqwrites.security.JwtUtil;
import com.uniqwrites.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        return ResponseEntity.ok().body("Authentication API is working!");
    }

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;    @PostMapping("/signup")
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
    }    @PostMapping("/login")
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
    }
      /**
     * Handle Google login requests
     */
    @PostMapping("/google/login")
    public ResponseEntity<?> googleLogin(@RequestBody Object googleLoginRequest) {
        try {
            // This is a placeholder - implement actual Google login logic
            // For now, create a mock success response
            AuthResponseDTO response = new AuthResponseDTO();
            response.setSuccess(true);
            response.setToken("mock_token_for_google_login");
            response.setMessage("Google login successful");
            response.setRole("STUDENT");
            response.setName("Google User");
            response.setEmail("google_user@example.com");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(AuthResponseDTO.error("Google authentication failed: " + e.getMessage()));
        }
    }
    
    /**
     * Handle forgot password requests
     */    @PostMapping("/forgot-password") 
    public ResponseEntity<?> forgotPassword(@RequestBody Object forgotPasswordRequest) {
        try {
            // This is a placeholder - implement actual forgot password logic
            return ResponseEntity.ok(
                new AuthResponseDTO(
                    true, 
                    null, 
                    "Password reset email sent", 
                    null, 
                    null, 
                    null
                )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                AuthResponseDTO.error("Failed to process password reset request: " + e.getMessage())
            );
        }
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
}
