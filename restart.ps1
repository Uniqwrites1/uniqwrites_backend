# PowerShell script to restart the Spring Boot application
$host.UI.RawUI.WindowTitle = "Uniqwrites Backend Server"
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "  UNIQWRITES BACKEND SERVER RESTART  " -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

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
        Write-Host "   Press Ctrl+C to stop the server" -ForegroundColor Cyan
        Write-Host ""
        java -jar $jarPath
    } else {
        Write-Host "   Build failed. Please check the Maven build logs." -ForegroundColor Red
    }
}
