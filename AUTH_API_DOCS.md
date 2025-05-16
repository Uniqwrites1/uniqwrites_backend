# Authentication API Documentation

## Response Format

All authentication endpoints return responses in a standardized format:

```json
{
  "success": true|false,
  "token": "JWT_TOKEN_STRING",
  "message": "Human-readable message",
  "role": "USER_ROLE",
  "name": "User's full name",
  "email": "user@example.com"
}
```

### Fields Explanation:

- `success`: Boolean indicating if the request was successful
- `token`: JWT token for authenticated requests (only on successful login/signup)
- `message`: Human-readable message describing the result
- `role`: User role (e.g., "STUDENT", "TEACHER", "ADMIN")
- `name`: User's full name
- `email`: User's email address

## Error Responses

Error responses follow the same format, but with `success: false` and no token:

```json
{
  "success": false,
  "token": null,
  "message": "Error description",
  "role": null,
  "name": null,
  "email": null
}
```

## Frontend Integration

### Storing the Token

After successful authentication:

```javascript
// Example using axios
axios.post('/login', credentials)
  .then(response => {
    const { token, role, name, email } = response.data;
    
    // Store in localStorage or secure storage
    localStorage.setItem('authToken', token);
    localStorage.setItem('userRole', role);
    localStorage.setItem('userName', name);
    localStorage.setItem('userEmail', email);
    
    // Set for future API calls
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  })
  .catch(error => {
    console.error('Authentication failed:', error.response?.data?.message || 'Unknown error');
  });
```

### Handling Errors

Check the `success` field to determine if the operation succeeded:

```javascript
api.post('/signup', userData)
  .then(response => {
    if (response.data.success) {
      // Success - redirect or show success message
      console.log(response.data.message);
    } else {
      // Server returned a failure response
      console.error(response.data.message);
    }
  })
  .catch(error => {
    // Network error or server error
    console.error('Signup failed:', error);
  });
```
