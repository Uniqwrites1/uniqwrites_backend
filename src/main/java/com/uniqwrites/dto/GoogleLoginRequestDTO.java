package com.uniqwrites.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Google login requests
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleLoginRequestDTO {
    private String email;
    private String name;
    private String googleId;
    private String role;
    private String token; // Google ID token
    private String picture; // Profile picture URL
}
