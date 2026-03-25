# Snake Game (Java + Web)

A classic Snake game built with Java Swing and a browser-playable web version.

## Features

- Smooth keyboard controls (arrow keys)
- Score and high score (in-session)
- Collision detection with wall and snake body
- Restart support (`R` or `Space`)
- Browser-playable version on GitHub Pages

## Project Structure

- `src/main/java/com/snakegame/SnakeGame.java` - app entry point
- `src/main/java/com/snakegame/SnakePanel.java` - game logic and rendering
- `build.ps1` - build script (compiles + packages JAR)
- `run.ps1` / `run.bat` - run scripts
- `.github/workflows/deploy-pages.yml` - auto deploy to GitHub Pages
- `site/index.html` + `site/snake-web.js` - web-playable game

## Run Locally (Windows PowerShell)

```powershell
./build.ps1
./run.ps1
```

Or:

```bat
run.bat
```

## Build Output

- Class files: `out/classes`
- JAR file (if `jar` tool is available): `out/jar/snake-game.jar`

## Controls

- `Up/Down/Left/Right` - move snake
- `R` or `Space` - restart after game over
- Web also supports `W/A/S/D`


