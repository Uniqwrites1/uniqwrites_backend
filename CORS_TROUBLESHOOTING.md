# CORS Troubleshooting Guide

This document provides guidance on troubleshooting CORS (Cross-Origin Resource Sharing) issues with the Uniqwrites backend.

## Common CORS Errors

1. **"Access to fetch at 'http://localhost:8080/api/auth/login' from origin 'http://localhost:5173' has been blocked by CORS policy"**
   - This indicates that the server's CORS configuration is not properly set up or has not been applied.

2. **"Response to preflight request doesn't pass access control check"**
   - The OPTIONS request is being blocked, which is required before complex requests.

3. **"The 'Access-Control-Allow-Origin' header has a value that is not equal to the supplied origin"**
   - The server is not configured to accept requests from your frontend's origin.

## Checking If CORS Is Working

1. Open your browser's developer tools (F12 in most browsers)
2. Go to the Network tab
3. Make a request to the backend from your frontend
4. Look for the following headers in the response:
   - `Access-Control-Allow-Origin`: Should contain your frontend's origin
   - `Access-Control-Allow-Methods`: Should list the HTTP methods you need
   - `Access-Control-Allow-Headers`: Should include the headers you're sending
   - `Access-Control-Allow-Credentials`: Should be "true" if you need credentials

## Testing Steps

1. **Test with our special CORS test endpoint:**
   ```javascript
   fetch('http://localhost:8080/api/test/cors-test', {
     method: 'GET',
     headers: {
       'Content-Type': 'application/json'
     },
     credentials: 'include'
   })
   .then(response => response.json())
   .then(data => console.log('Success:', data))
   .catch(error => console.error('Error:', error));
   ```

2. **Check preflight requests:**
   - Make sure OPTIONS requests are handled correctly
   - The server should respond with a 200 OK status
   - Check that the appropriate CORS headers are included in the response

3. **Verify server configuration:**
   - WebConfig.java and SecurityConfig.java should include proper CORS configuration
   - Both components are needed for Spring Security to handle CORS correctly

## Solutions for Common Issues

1. **Server not accepting your origin:**
   - Update the `allowedOrigins` list in both WebConfig.java and SecurityConfig.java
   - Make sure the origin format is exact (http://localhost:5173, not localhost:5173)

2. **Credentials not working:**
   - Set `allowCredentials(true)` in CORS configuration
   - Set `credentials: 'include'` in frontend fetch calls

3. **Headers or methods not allowed:**
   - Update `allowedHeaders` and `allowedMethods` to include all required values
   - For custom headers, be sure to add them explicitly

4. **Spring Security blocking requests:**
   - Ensure `.cors().and()` is configured in SecurityConfig
   - Add explicit handling for OPTIONS requests with `.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()`

## After Making Changes

Remember to restart your Spring Boot application after making configuration changes to CORS settings.

```bash
# PowerShell
cd "path\to\uniqwrites-backend"
.\restart.ps1
```

## Using the CORS Test Helper

We've provided a `corsTestHelper.js` file that you can use in your frontend to test CORS connectivity:

```javascript
import { testCORSConnectivity } from './path/to/corsTestHelper';

// Inside your component
const result = await testCORSConnectivity();
if (result.success) {
  console.log('CORS is working!');
} else {
  console.error('CORS issue detected:', result.error);
}
```
