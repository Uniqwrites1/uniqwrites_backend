# Google Authentication Integration Guide

This document outlines how the Google OAuth2 authentication is integrated with the Uniqwrites backend.

## Overview

The Uniqwrites backend supports two methods of Google authentication:

1. **Server-Side OAuth2 Flow**:
   - Complete OAuth2 flow handled by Spring Security
   - User is redirected to Google for authentication
   - After successful authentication, user is redirected back with token

2. **Frontend Token Verification**:
   - Frontend handles Google authentication via Google JavaScript SDK
   - Frontend sends Google ID token to backend for verification
   - Backend verifies the token and returns a JWT token

## Configuration

### 1. Environment Variables

The following environment variables need to be set:

```
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
```

### 2. OAuth2 Configuration in application.yml

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
            redirect-uri: "{baseUrl}/oauth2/callback/{registrationId}"
            scope: email,profile
```

## API Endpoints

### 1. Google Login API

**Endpoint:** `POST /google/login` or `POST /api/auth/google/login`  
**Description:** Verifies Google token and creates/updates user in database  
**Request Body:**

```json
{
  "email": "user@example.com",
  "name": "User Name",
  "googleId": "google-user-id",
  "role": "STUDENT", // Optional, defaults to STUDENT
  "token": "google-id-token",
  "picture": "https://profile-picture-url" // Optional
}
```

**Response:**

```json
{
  "success": true,
  "token": "jwt-token",
  "message": "Authentication successful",
  "role": "STUDENT",
  "name": "User Name",
  "email": "user@example.com"
}
```

### 2. OAuth2 Authorization Initiation

**Endpoint:** `GET /oauth2/authorize/google`  
**Description:** Initiates the OAuth2 authorization flow with Google  
**Response:** Redirects to Google login page

### 3. OAuth2 Callback

**Endpoint:** `GET /oauth2/callback/google`  
**Description:** Handles callback from Google after successful authentication  
**Response:** Redirects to frontend with JWT token and user details

## Integration with Frontend

### Method 1: Use Backend OAuth2 Flow

1. Redirect user to `/oauth2/authorize/google` to start OAuth2 flow
2. After successful authentication, user will be redirected to the frontend with JWT token

### Method 2: Use Google JavaScript SDK

1. Initialize Google Sign-In in your frontend application
2. After successful Google authentication, get the ID token
3. Send the token to backend using the `/google/login` endpoint
4. Store the returned JWT token for authenticated requests

## Example Frontend Integration

```javascript
// Using Google JavaScript SDK
function handleGoogleLogin(googleUser) {
  const googleToken = googleUser.getAuthResponse().id_token;
  const profile = googleUser.getBasicProfile();
  
  const userData = {
    email: profile.getEmail(),
    name: profile.getName(),
    googleId: profile.getId(),
    token: googleToken,
    picture: profile.getImageUrl(),
    role: "STUDENT" // or another role based on your app logic
  };

  // Send to backend
  fetch('/api/auth/google/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(userData)
  })
  .then(response => response.json())
  .then(data => {
    if (data.success) {
      // Store JWT token and user details
      localStorage.setItem('authToken', data.token);
      localStorage.setItem('userRole', data.role);
      localStorage.setItem('userName', data.name);
      localStorage.setItem('userEmail', data.email);
    }
  });
}
```

## Security Considerations

1. In production, always verify Google ID tokens on the server side
2. Implement token expiration and refresh mechanisms
3. Restrict allowed domains in Google Developer Console
4. Use HTTPS for all API communications
