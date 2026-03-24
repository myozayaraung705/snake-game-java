$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$srcDir = Join-Path $projectRoot "src/main/java"
$outDir = Join-Path $projectRoot "out"
$classesDir = Join-Path $outDir "classes"
$jarDir = Join-Path $outDir "jar"
$jarPath = Join-Path $jarDir "snake-game.jar"
$manifestPath = Join-Path $outDir "manifest.txt"

if (Test-Path $outDir) {
    Remove-Item -Recurse -Force $outDir
}

New-Item -ItemType Directory -Force -Path $classesDir | Out-Null
New-Item -ItemType Directory -Force -Path $jarDir | Out-Null

$javaFiles = Get-ChildItem -Path $srcDir -Filter *.java -Recurse | ForEach-Object { $_.FullName }
if ($javaFiles.Count -eq 0) {
    throw "No Java source files were found in $srcDir"
}

$javacPath = (Get-Command javac).Source
$javaBinDir = Split-Path -Parent $javacPath
$jarToolPath = Join-Path $javaBinDir "jar.exe"
$canBuildJar = Test-Path $jarToolPath

javac -d $classesDir $javaFiles

if ($canBuildJar) {
    @"
Main-Class: com.snakegame.SnakeGame
"@ | Set-Content -Path $manifestPath -Encoding ascii
    & $jarToolPath cfm $jarPath $manifestPath -C $classesDir .
    Write-Host "Build complete: classes + $jarPath"
} else {
    Write-Host "Build complete: classes only (jar tool not found on this machine)."
}
