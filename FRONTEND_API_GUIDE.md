# Frontend API Integration Guide

This guide provides instructions for properly configuring the frontend API client to work with the Uniqwrites backend.

## API Base URL

The backend is running at: `http://localhost:8080`

## Authentication Endpoints

The backend supports two patterns for authentication endpoints:

### Option 1: Direct Paths (Recommended)

```javascript
// Example configuration for axios
const apiClient = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true // Important for CORS with credentials
});

// Auth endpoints
const login = (credentials) => apiClient.post('/login', credentials);
const signup = (userData) => apiClient.post('/signup', userData);
const googleLogin = (tokenData) => apiClient.post('/google/login', tokenData);
const forgotPassword = (email) => apiClient.post('/forgot-password', { email });
```

### Option 2: API Prefixed Paths

```javascript
// Example configuration for axios
const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api/auth',
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true // Important for CORS with credentials
});

// Auth endpoints
const login = (credentials) => apiClient.post('/login', credentials);
const signup = (userData) => apiClient.post('/signup', userData);
const googleLogin = (tokenData) => apiClient.post('/google/login', tokenData);
const forgotPassword = (email) => apiClient.post('/forgot-password', { email });
```

## Common Issues & Solutions

### CORS Issues

If you're getting CORS errors:

1. Make sure your frontend is running on `http://localhost:5173` (Vite) or `http://localhost:3000` (React)
2. Ensure `withCredentials: true` is set in your axios config
3. Verify you're using the correct API URL (check browser network tab for the actual request URL)

### Authentication Problems

If authentication is failing:

1. Check the response in the browser's network tab
2. Make sure your payload format matches what the backend expects:

```javascript
// Login format
{
  "email": "user@example.com",
  "password": "yourpassword" 
}

// Signup format
{
  "name": "Full Name",
  "email": "user@example.com",
  "password": "yourpassword",
  "role": "STUDENT" // or "TEACHER", "PARENT", etc.
}
```

### Token Handling

After successful login, store the JWT token and user info:

```javascript
apiClient.post('/login', credentials)
  .then(response => {
    // Auth response format now includes multiple fields
    const { success, token, message, role, name, email } = response.data;
    
    if (success) {
      // Store authentication data
      localStorage.setItem('authToken', token);
      localStorage.setItem('userRole', role);
      localStorage.setItem('userName', name);
      localStorage.setItem('userEmail', email);
      
      // Configure future requests to use this token
      apiClient.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    } else {
      // Handle error message
      console.error(message);
    }
  })
```

## Testing Your Configuration

Use the file `direct_path_test.http` in the backend project to test API endpoints directly.

## Protected Endpoints

Protected endpoints require authentication with a valid JWT token:

```javascript
// Example of accessing a protected endpoint
const fetchDashboard = () => {
  const token = localStorage.getItem('authToken');
  return apiClient.get('/api/dashboard', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
};
```
