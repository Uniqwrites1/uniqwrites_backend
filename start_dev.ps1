# PowerShell script to start the application with development profile
$host.UI.RawUI.WindowTitle = "Uniqwrites Backend Server (Development Mode)"
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host "  UNIQWRITES BACKEND SERVER - DEVELOPMENT MODE      " -ForegroundColor Cyan
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host ""

# Set environment variables for development
$env:GOOGLE_CLIENT_ID = "dummy-client-id"
$env:GOOGLE_CLIENT_SECRET = "dummy-client-secret"
$env:JWT_SECRET = "uniqwrites-jwt-secret-key-for-token-generation-and-validation"
$env:JWT_EXPIRATION_MS = "86400000"
$env:MAIL_HOST = "smtp.gmail.com"
$env:MAIL_PORT = "587"
$env:MAIL_USERNAME = "noreply@uniqwrites.com" 
$env:MAIL_PASSWORD = "dummy-password"
$env:DB_URL = "jdbc:postgresql://localhost:5432/uniqwrites"
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "postgres"

Write-Host "Environment variables set for development mode." -ForegroundColor Green
Write-Host ""

Write-Host "Starting application with development profile..." -ForegroundColor Yellow
Write-Host "API will be available at: http://localhost:8080" -ForegroundColor Cyan
Write-Host ""

# Start the application with the dev profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# If the application exits, keep the window open
Write-Host "Press any key to continue..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
