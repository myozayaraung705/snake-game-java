# Snake Game (Java Swing)

A classic Snake game built with Java Swing.

## Features

- Smooth keyboard controls (arrow keys)
- Score and high score (in-session)
- Collision detection with wall and snake body
- Restart support (`R` or `Space`)

## Project Structure

- `src/main/java/com/snakegame/SnakeGame.java` - app entry point
- `src/main/java/com/snakegame/SnakePanel.java` - game logic and rendering
- `build.ps1` - build script (compiles + packages JAR)
- `run.ps1` / `run.bat` - run scripts
- `.github/workflows/deploy-pages.yml` - auto deploy to GitHub Pages

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

## Deployment

After pushing this project to GitHub on branch `main`, GitHub Actions will:

1. Build the JAR
2. Publish a GitHub Pages site with:
   - `index.html`
   - downloadable `snake-game.jar`

When deployment finishes, your game page will be available at:

`https://<your-github-username>.github.io/<repository-name>/`
