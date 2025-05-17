package com.uniqwrites.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for password reset requests
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {
    private String token;
    private String password;
}
