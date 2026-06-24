@echo off
REM Run Maven tests (Windows)
mvn test
if %ERRORLEVEL% neq 0 (
  echo Tests failed or Maven not found.
  exit /b %ERRORLEVEL%
)
echo Tests completed successfully.
