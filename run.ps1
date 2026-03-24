$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$jarPath = Join-Path $projectRoot "out/jar/snake-game.jar"
$classesDir = Join-Path $projectRoot "out/classes"

if (-not (Test-Path $classesDir)) {
    & (Join-Path $projectRoot "build.ps1")
}

if (Test-Path $jarPath) {
    java -jar $jarPath
} else {
    java -cp $classesDir com.snakegame.SnakeGame
}
