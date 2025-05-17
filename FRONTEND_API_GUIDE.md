# Frontend API Integration Guide

This guide provides instructions for properly configuring the frontend API client to work with the Uniqwrites backend.

## API Base URL

The backend is running at: `http://localhost:8080`

> **New Feature:** Google OAuth2 Authentication has been implemented! See the [Google Authentication](#google-authentication) section below.

## Authentication Endpoints

The backend supports three patterns for authentication endpoints to be flexible with frontend implementations:

### Option 1: Direct Paths

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

### Option 2: API Prefixed Paths (NEW)

```javascript
// Example configuration for axios
const apiClient = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true // Important for CORS with credentials
});

// Auth endpoints with /api prefix
const login = (credentials) => apiClient.post('/api/login', credentials);
const signup = (userData) => apiClient.post('/api/signup', userData);
const googleLogin = (tokenData) => apiClient.post('/api/google/login', tokenData);
const forgotPassword = (email) => apiClient.post('/api/forgot-password', { email });
```

### Option 3: API Auth Prefixed Paths

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

## Google Authentication

The backend now supports Google OAuth2 authentication. There are two ways to integrate with Google authentication:

### Method 1: Frontend Google Authentication (Recommended)

This method uses the Google JavaScript SDK on the frontend and sends the token to the backend for verification:

1. **Install Google Sign-In SDK**
   ```html
   <script src="https://accounts.google.com/gsi/client" async defer></script>
   ```

2. **Initialize Google Sign-In Button**
   ```html
   <div id="g_id_onload"
        data-client_id="YOUR_GOOGLE_CLIENT_ID"
        data-callback="handleCredentialResponse">
   </div>
   <div class="g_id_signin" data-type="standard"></div>
   ```

3. **Handle Google Authentication Response**
   ```javascript
   function handleCredentialResponse(response) {
     // Get the Google ID token
     const googleToken = response.credential;
     
     // Decode the token to get user info
     const payload = parseJwt(googleToken);
     
     // Send to backend
     const googleData = {
       email: payload.email,
       name: payload.name,
       googleId: payload.sub,
       token: googleToken,
       picture: payload.picture,
       role: "STUDENT" // Or get from user selection
     };
     
     apiClient.post('/google/login', googleData)
       .then(response => {
         if (response.data.success) {
           // Store authentication data
           localStorage.setItem('authToken', response.data.token);
           localStorage.setItem('userRole', response.data.role);
           localStorage.setItem('userName', response.data.name);
           localStorage.setItem('userEmail', response.data.email);
           
           // Redirect to dashboard
           window.location.href = '/dashboard';
         }
       })
       .catch(error => {
         console.error('Google authentication failed:', error);
       });
   }
   
   // Helper function to decode JWT tokens
   function parseJwt(token) {
     const base64Url = token.split('.')[1];
     const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
     return JSON.parse(window.atob(base64));
   }
   ```

### Method 2: Backend OAuth2 Flow

This method redirects users to Google for authentication:

```javascript
// Redirect to Google login
function loginWithGoogle() {
  window.location.href = 'http://localhost:8080/oauth2/authorize/google';
}
```

After successful authentication, users will be redirected to the frontend with JWT token parameters.

For more details, see the comprehensive [Google Authentication Guide](GOOGLE_AUTH_GUIDE.md).
