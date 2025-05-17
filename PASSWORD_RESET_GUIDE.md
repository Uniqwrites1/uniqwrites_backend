# Password Reset Flow

This document outlines the password reset flow implemented in the Uniqwrites backend.

## Overview

The password reset process consists of the following steps:

1. User requests a password reset by providing their email address
2. System generates a unique reset token and sends it to the user's email
3. User clicks the link in the email and is directed to a reset password page
4. User enters a new password
5. System validates the token and updates the user's password

## API Endpoints

### Request Password Reset

**Endpoint:** `POST /api/auth/forgot-password` or `POST /forgot-password`

**Request Body:**
```json
{
  "email": "user@example.com"
}
```

**Response:**
```json
{
  "success": true,
  "message": "If your email exists in our system, you will receive a password reset link shortly."
}
```

### Validate Reset Token

**Endpoint:** `GET /api/auth/validate-reset-token` or `GET /validate-reset-token`

**Query Parameters:**
- `token`: The password reset token

**Response (valid token):**
```json
{
  "success": true,
  "message": "Valid reset token."
}
```

**Response (invalid token):**
```json
{
  "success": false,
  "message": "Invalid password reset token."
}
```

### Reset Password

**Endpoint:** `POST /api/auth/reset-password` or `POST /reset-password`

**Request Body:**
```json
{
  "token": "reset-token",
  "password": "newPassword123"
}
```

**Response (success):**
```json
{
  "success": true,
  "message": "Your password has been reset successfully. You can now log in with your new password."
}
```

**Response (failure):**
```json
{
  "success": false,
  "message": "Invalid password reset token. Please request a new password reset."
}
```

## Implementation Details

### Password Reset Token

- The reset token is a UUID generated when the user requests a password reset
- The token is stored in the user's record in the database
- Tokens expire after 1 hour (configurable)

### Email Templates

The password reset email uses a Thymeleaf template located at:
`src/main/resources/templates/password-reset-email.html`

The template includes:
- Personal greeting with the user's name
- Reset password button/link
- Security note about token expiration
- Fallback text link for email clients that block HTML content

### Security Considerations

1. **Token Expiration**: Reset tokens expire after 1 hour for security
2. **One-Time Use**: Tokens are cleared after successful password reset
3. **Email Privacy**: System provides same response regardless of whether email exists
4. **Password Encryption**: New passwords are encrypted before storage
5. **HTTPS**: All password reset endpoints should be accessed over HTTPS in production

## Frontend Integration

The frontend should implement the following pages:

1. **Forgot Password Page**: Form to collect user's email address
2. **Reset Password Page**: Form to collect new password, accessed via email link
3. **Confirmation Page**: Display success message after password reset

### Example Flow

1. User clicks "Forgot Password" link on login page
2. User enters email address and submits form
3. Frontend calls `/api/auth/forgot-password` endpoint
4. Backend generates token and sends email
5. User receives email and clicks reset link
6. Frontend loads reset password page with token from URL
7. Frontend validates token with `/api/auth/validate-reset-token` endpoint
8. User enters new password and submits form
9. Frontend calls `/api/auth/reset-password` endpoint
10. User is redirected to login page with success message
