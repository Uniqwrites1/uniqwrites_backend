# Uniqwrites Backend API Testing Guide

This document provides instructions for testing the Uniqwrites Backend API endpoints as required in the specifications.

## Prerequisites

1. Server is running (typically at http://localhost:8080)
2. Postman, VS Code with REST Client, or another HTTP client for testing
3. A postgres database configured according to .env file settings
4. Frontend application running on either http://localhost:3000 (React) or http://localhost:5173 (Vite)

## Authentication Flow

1. First register users with different roles (admin, teacher, parent)
2. Login to get JWT tokens for each role
3. Use the tokens for subsequent authenticated requests

## Testing the Required Endpoints

### 1. Authentication Endpoints

- **Register User**: `POST /auth/register`
- **Login**: `POST /auth/login`

### 2. Form Submission Endpoints

- **Parent Request Form**: `POST /parent/requests`
- **Teacher Application**: `POST /teacher/apply`
- **School Request Form**: `POST /school/request`

### 3. Dashboard Access

- **Dashboard Data**: `GET /dashboard` (role-specific content)

### 4. Job Applications

- **Apply to Job**: `POST /jobs/{id}/apply` 

### 5. Feedback

- **Feedback Submission**: `POST /feedback`

### 6. Extra APIs

- **Blog Management**: 
  - Create: `POST /admin/blog`
  - Delete: `DELETE /admin/blog/{id}`
- **Teacher Resources**:
  - Upload: `POST /teacher/resources`
  - View: `GET /parent/resources/{teacherId}`

## CORS Configuration

The backend is configured to support Cross-Origin Resource Sharing (CORS) for frontend applications running on:
- http://localhost:3000 (React development server)
- http://localhost:5173 (Vite development server)

### CORS Features

1. **Multiple Origin Support**: Both React and Vite development servers are supported.
2. **Preflight Handling**: OPTIONS requests are properly handled.
3. **Credentials**: Supports credentials for authentication.
4. **Exposed Headers**: Authorization headers are exposed to the frontend.
5. **HTTP Methods**: Supports GET, POST, PUT, DELETE, PATCH, OPTIONS, and HEAD.

### Direct Path Support

The backend is designed to handle both prefixed (`/api/auth/*`) and direct (`/*`) auth endpoints:

1. **Direct Paths**:
   - `/login` → Handled by `AuthPathController` and forwarded to `AuthController`
   - `/signup` → Same as above
   - `/google/login` → Same as above
   - `/forgot-password` → Same as above

2. **API Paths**:
   - `/api/auth/login`
   - `/api/auth/signup`
   - `/api/auth/google/login`
   - `/api/auth/forgot-password`

This dual routing ensures compatibility with frontend applications regardless of whether they use the `/api/auth/` prefix or direct paths.

### Testing CORS

A dedicated `cors_test.http` file is provided to test CORS functionality:
- Test endpoint: `GET /api/test/cors-test`
- Echo endpoint: `POST /api/test/echo`
- Login endpoint: `POST /api/auth/login`

To test CORS with your frontend:
1. Start the backend server
2. Start your frontend on port 5173 (Vite) or 3000 (React)
3. Make API requests from the frontend to the backend
4. Check browser console for any CORS-related errors

## Testing with HTTP Files

This project includes several HTTP files for API testing:

- `api.http`: Main API testing file with all required endpoints
- `api_endpoints_test.http`: More comprehensive test cases
- `api_test.http`: Additional test cases

To use these files:
1. Open them in VS Code with REST Client extension
2. Execute requests one by one following the authentication flow
3. Store tokens from login responses in the variables at the top

## Security Testing

Make sure to test:
- Access to protected endpoints without tokens (should fail)
- Access to role-specific endpoints with tokens of different roles (should fail)
- CORS configuration (frontend should be able to make requests)
- Password hashing (passwords should be stored as BCrypt hashes)

## Complete Test Checklist

- [ ] Register users with different roles
- [ ] Login and obtain valid JWT tokens
- [ ] Submit parent request form
- [ ] Submit teacher application 
- [ ] Submit school request form
- [ ] Access dashboard with appropriate role
- [ ] Apply to teaching job
- [ ] Submit feedback
- [ ] Test admin blog management
- [ ] Upload and retrieve teacher resources
- [ ] Test all error scenarios and unauthorized access
- [ ] Verify CORS configuration works with frontend
