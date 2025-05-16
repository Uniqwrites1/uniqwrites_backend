// CORS Test Helper for Vite Frontend
// Save this file in your Vite frontend project and import it where needed

/**
 * Test CORS connectivity with the backend
 * @returns {Promise<Object>} The result of the CORS test
 */
export async function testCORSConnectivity() {
  try {
    // Test the CORS-test endpoint
    const response = await fetch('http://localhost:8080/api/test/cors-test', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      credentials: 'include' // Important for testing credentials
    });
    
    const data = await response.json();
    console.log('✅ CORS Test Successful:', data);
    return {
      success: true,
      message: 'CORS is working correctly!',
      data
    };
  } catch (error) {
    console.error('❌ CORS Test Failed:', error);
    return {
      success: false,
      message: 'CORS test failed',
      error: error.message
    };
  }
}

/**
 * Test authentication with the backend
 * @param {string} email - User email
 * @param {string} password - User password
 * @returns {Promise<Object>} The result of the authentication test
 */
export async function testAuthentication(email, password) {
  try {
    const response = await fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      credentials: 'include',
      body: JSON.stringify({ email, password })
    });
    
    const data = await response.json();
    console.log('Auth Test Result:', data);
    return {
      success: response.ok,
      message: response.ok ? 'Authentication successful' : 'Authentication failed',
      data
    };
  } catch (error) {
    console.error('Auth Test Error:', error);
    return {
      success: false,
      message: 'Authentication test failed',
      error: error.message
    };
  }
}

/**
 * Example usage in a Vue component:
 * 
 * <script setup>
 * import { testCORSConnectivity, testAuthentication } from './corsTestHelper';
 * import { ref, onMounted } from 'vue';
 * 
 * const corsStatus = ref('Not tested');
 * const authStatus = ref('Not tested');
 * 
 * onMounted(async () => {
 *   const corsResult = await testCORSConnectivity();
 *   corsStatus.value = corsResult.success ? 'Working' : 'Failed';
 * });
 * 
 * async function testLogin() {
 *   const authResult = await testAuthentication('test@example.com', 'password123');
 *   authStatus.value = authResult.success ? 'Working' : 'Failed';
 * }
 * </script>
 */
