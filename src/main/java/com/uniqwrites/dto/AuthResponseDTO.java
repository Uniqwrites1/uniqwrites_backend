package com.uniqwrites.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for authentication operations like login and signup
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private boolean success;
    private String token;
    private String message;
    private String role;
    private String name;
    private String email;
    
    /**
     * Create a success response with authentication token
     */
    public static AuthResponseDTO success(String token, String role, String name, String email) {
        return new AuthResponseDTO(true, token, "Authentication successful", role, name, email);
    }
    
    /**
     * Create an error response with a message
     */
    public static AuthResponseDTO error(String message) {
        return new AuthResponseDTO(false, null, message, null, null, null);
    }
}
