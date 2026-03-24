@echo off
powershell -ExecutionPolicy Bypass -File "%~dp0build.ps1"
if errorlevel 1 exit /b 1
if exist "%~dp0out\jar\snake-game.jar" (
  java -jar "%~dp0out\jar\snake-game.jar"
) else (
  java -cp "%~dp0out\classes" com.snakegame.SnakeGame
)
