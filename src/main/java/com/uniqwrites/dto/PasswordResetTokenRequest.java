package com.uniqwrites.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for password reset token requests
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetTokenRequest {
    private String email;
}
