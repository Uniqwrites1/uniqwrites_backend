# PowerShell script to start the application with environment variables from .env file
$host.UI.RawUI.WindowTitle = "Uniqwrites Backend Server"
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host "  UNIQWRITES BACKEND SERVER                         " -ForegroundColor Cyan
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host ""

# Load variables from .env file
$envFile = "$PSScriptRoot\.env"
Write-Host "Loading environment variables from $envFile" -ForegroundColor Yellow
if (Test-Path $envFile) {
    Get-Content $envFile | ForEach-Object {
        if (-not $_.StartsWith("#") -and $_.Contains("=")) {
            $key, $value = $_ -split "=", 2
            $key = $key.Trim()
            $value = $value.Trim()
            [Environment]::SetEnvironmentVariable($key, $value, "Process")
            Write-Host "  Set $key" -ForegroundColor Gray
        }
    }
    
    # Add Google OAuth credentials (modify with your actual values)
    $env:GOOGLE_CLIENT_ID = "dummy-client-id"  # Replace with your actual Google client ID
    $env:GOOGLE_CLIENT_SECRET = "dummy-client-secret"  # Replace with your actual Google client secret
    Write-Host "  Set GOOGLE_CLIENT_ID" -ForegroundColor Gray
    Write-Host "  Set GOOGLE_CLIENT_SECRET" -ForegroundColor Gray
    
    Write-Host "Environment variables loaded successfully." -ForegroundColor Green
} else {
    Write-Host "Error: .env file not found at $envFile" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Starting application..." -ForegroundColor Yellow
Write-Host "API will be available at: http://localhost:8080" -ForegroundColor Cyan
Write-Host ""

# Start the application
mvn spring-boot:run

# If the application exits, keep the window open
Write-Host "Application stopped. Press any key to exit..." -ForegroundColor Yellow
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
