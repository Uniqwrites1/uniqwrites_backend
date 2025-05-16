# Backend API Integration Test

This PowerShell script tests all the API endpoints to verify they are working correctly.

Write-Host "Starting Uniqwrites API Test" -ForegroundColor Cyan
Write-Host "=============================" -ForegroundColor Cyan

# Define API URLs
$baseUrl = "http://localhost:8080"
$apiAuthUrl = "$baseUrl/api/auth"
$directAuthUrl = $baseUrl

# Test endpoints function
function Test-ApiEndpoint {
    param (
        [string]$Method,
        [string]$Endpoint,
        [string]$Description,
        [object]$Payload = $null
    )
    
    Write-Host "`n🔍 Testing $Description..." -ForegroundColor Yellow
    
    try {
        $headers = @{
            "Content-Type" = "application/json"
            "Origin" = "http://localhost:5173"
        }
        
        if ($Payload) {
            $bodyJson = $Payload | ConvertTo-Json
            $response = Invoke-WebRequest -Method $Method -Uri $Endpoint -Body $bodyJson -Headers $headers -ErrorAction Stop
        }
        else {
            $response = Invoke-WebRequest -Method $Method -Uri $Endpoint -Headers $headers -ErrorAction Stop
        }
        
        Write-Host "✅ SUCCESS: $Description ($($response.StatusCode))" -ForegroundColor Green
        return $response
    }
    catch {
        Write-Host "❌ FAILED: $Description - $($_.Exception.Message)" -ForegroundColor Red
        return $null
    }
}

# 1. Test Direct Auth Endpoints
Write-Host "`nTesting Direct Authentication Endpoints" -ForegroundColor Cyan
Write-Host "--------------------------------------" -ForegroundColor Cyan

$loginData = @{
    email = "test@example.com"
    password = "password123"
}
Test-ApiEndpoint -Method "POST" -Endpoint "$directAuthUrl/login" -Description "Direct Login Endpoint" -Payload $loginData

$signupData = @{
    name = "New Test User"
    email = "newtest@example.com"
    password = "password123"
    role = "STUDENT"
}
Test-ApiEndpoint -Method "POST" -Endpoint "$directAuthUrl/signup" -Description "Direct Signup Endpoint" -Payload $signupData

$googleData = @{
    token = "sample_google_token" 
    email = "google@example.com"
}
Test-ApiEndpoint -Method "POST" -Endpoint "$directAuthUrl/google/login" -Description "Direct Google Login Endpoint" -Payload $googleData

$forgotData = @{
    email = "test@example.com"
}
Test-ApiEndpoint -Method "POST" -Endpoint "$directAuthUrl/forgot-password" -Description "Direct Forgot Password Endpoint" -Payload $forgotData

# 2. Test API Prefixed Auth Endpoints
Write-Host "`nTesting API Prefixed Authentication Endpoints" -ForegroundColor Cyan
Write-Host "------------------------------------------" -ForegroundColor Cyan

Test-ApiEndpoint -Method "POST" -Endpoint "$apiAuthUrl/login" -Description "API Login Endpoint" -Payload $loginData
Test-ApiEndpoint -Method "POST" -Endpoint "$apiAuthUrl/signup" -Description "API Signup Endpoint" -Payload $signupData

# 3. Test CORS Test Endpoints
Write-Host "`nTesting CORS Functionality" -ForegroundColor Cyan
Write-Host "-----------------------" -ForegroundColor Cyan

Test-ApiEndpoint -Method "GET" -Endpoint "$baseUrl/api/test/cors-test" -Description "CORS Test Endpoint"
Test-ApiEndpoint -Method "GET" -Endpoint "$baseUrl/api/test/public" -Description "Public Test Endpoint"
Test-ApiEndpoint -Method "POST" -Endpoint "$baseUrl/api/test/echo" -Description "Echo Endpoint" -Payload @{ message = "Test message" }

# Summary
Write-Host "`nAPI Testing Summary" -ForegroundColor Cyan
Write-Host "=================" -ForegroundColor Cyan
Write-Host "If all tests passed, your API is correctly configured for frontend integration."
Write-Host "If any tests failed, please check the error messages and fix the issues."
Write-Host "`nRemember to restart your frontend application to apply any configuration changes."
