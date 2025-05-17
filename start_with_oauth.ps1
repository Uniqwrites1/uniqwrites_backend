# PowerShell script to start the Spring Boot application with Google OAuth2 configuration
$host.UI.RawUI.WindowTitle = "Uniqwrites Backend Server with Google OAuth"
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host "  UNIQWRITES BACKEND SERVER WITH GOOGLE OAUTH       " -ForegroundColor Cyan
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host ""

# You would replace these with your actual values
$env:GOOGLE_CLIENT_ID = "your-google-client-id"
$env:GOOGLE_CLIENT_SECRET = "your-google-client-secret"

# Make sure these environment variables are set correctly
if (-not $env:JWT_SECRET) {
    $env:JWT_SECRET = "uniqwrites-jwt-secret-key-for-token-generation-and-validation"
}

if (-not $env:JWT_EXPIRATION_MS) {
    $env:JWT_EXPIRATION_MS = "86400000" # 24 hours in milliseconds
}

Write-Host "1. Stopping any running Spring Boot application..." -ForegroundColor Yellow
Get-Process -Name java -ErrorAction SilentlyContinue | Where-Object { $_.CommandLine -match "uniqwrites" } | ForEach-Object {
    Write-Host "   Stopping process: $($_.Id)" -ForegroundColor Gray
    Stop-Process -Id $_.Id -Force
}

Write-Host "2. Compiling the application..." -ForegroundColor Yellow
mvn compile

Write-Host "3. Starting the Spring Boot application..." -ForegroundColor Yellow
$jarPath = ".\target\uniqwrites-backend-0.0.1-SNAPSHOT.jar"

if (Test-Path $jarPath) {
    Write-Host "   JAR file found. Starting application..." -ForegroundColor Green
    Write-Host ""
    Write-Host "   API will be available at: http://localhost:8080" -ForegroundColor Cyan
    Write-Host "   Google OAuth2 is enabled" -ForegroundColor Cyan
    Write-Host "   Press Ctrl+C to stop the server" -ForegroundColor Cyan
    Write-Host ""
    java -jar $jarPath
} else {
    Write-Host "   JAR file not found. Building the project first..." -ForegroundColor Magenta
    mvn clean package -DskipTests
    if (Test-Path $jarPath) {
        Write-Host "   Build successful. Starting application..." -ForegroundColor Green
        Write-Host ""
        Write-Host "   API will be available at: http://localhost:8080" -ForegroundColor Cyan
        Write-Host "   Google OAuth2 is enabled" -ForegroundColor Cyan
        Write-Host "   Press Ctrl+C to stop the server" -ForegroundColor Cyan
        Write-Host ""
        java -jar $jarPath
    } else {
        Write-Host "   Build failed. Please check the Maven build logs." -ForegroundColor Red
    }
}
