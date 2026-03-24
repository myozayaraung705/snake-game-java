const canvas = document.getElementById("game");
const ctx = canvas.getContext("2d");
const startBtn = document.getElementById("startBtn");
const scoreLabel = document.getElementById("scoreLabel");

const tileSize = 20;
const tiles = canvas.width / tileSize;

let snake = [];
let direction = { x: 1, y: 0 };
let nextDirection = { x: 1, y: 0 };
let food = { x: 10, y: 10 };
let score = 0;
let highScore = Number(localStorage.getItem("snakeHighScore") || 0);
let running = false;
let timer = null;

function resetGame() {
  snake = [
    { x: 12, y: 13 },
    { x: 11, y: 13 },
    { x: 10, y: 13 }
  ];
  direction = { x: 1, y: 0 };
  nextDirection = { x: 1, y: 0 };
  score = 0;
  running = true;
  spawnFood();
  updateScoreLabel();
  if (timer) {
    clearInterval(timer);
  }
  timer = setInterval(tick, 100);
  draw();
}

function updateScoreLabel() {
  scoreLabel.textContent = `Score: ${score} | High: ${highScore}`;
}

function spawnFood() {
  do {
    food = {
      x: Math.floor(Math.random() * tiles),
      y: Math.floor(Math.random() * tiles)
    };
  } while (snake.some((segment) => segment.x === food.x && segment.y === food.y));
}

function tick() {
  if (!running) {
    return;
  }

  direction = nextDirection;
  const head = snake[0];
  const nextHead = { x: head.x + direction.x, y: head.y + direction.y };

  const outOfBounds =
    nextHead.x < 0 || nextHead.y < 0 || nextHead.x >= tiles || nextHead.y >= tiles;
  const hitSelf = snake.some((s) => s.x === nextHead.x && s.y === nextHead.y);

  if (outOfBounds || hitSelf) {
    running = false;
    if (score > highScore) {
      highScore = score;
      localStorage.setItem("snakeHighScore", String(highScore));
    }
    updateScoreLabel();
    draw();
    drawOverlay("Game Over", "Press Space or Start / Restart");
    return;
  }

  snake.unshift(nextHead);
  if (nextHead.x === food.x && nextHead.y === food.y) {
    score += 1;
    spawnFood();
  } else {
    snake.pop();
  }

  updateScoreLabel();
  draw();
}

function drawGrid() {
  ctx.strokeStyle = "#1f2937";
  ctx.lineWidth = 1;
  for (let i = 0; i <= tiles; i += 1) {
    const p = i * tileSize;
    ctx.beginPath();
    ctx.moveTo(p, 0);
    ctx.lineTo(p, canvas.height);
    ctx.stroke();
    ctx.beginPath();
    ctx.moveTo(0, p);
    ctx.lineTo(canvas.width, p);
    ctx.stroke();
  }
}

function draw() {
  ctx.fillStyle = "#0f172a";
  ctx.fillRect(0, 0, canvas.width, canvas.height);
  drawGrid();

  ctx.fillStyle = "#ef4444";
  ctx.beginPath();
  ctx.arc(
    food.x * tileSize + tileSize / 2,
    food.y * tileSize + tileSize / 2,
    tileSize / 2 - 2,
    0,
    Math.PI * 2
  );
  ctx.fill();

  snake.forEach((segment, index) => {
    ctx.fillStyle = index === 0 ? "#4ade80" : "#22c55e";
    ctx.fillRect(
      segment.x * tileSize + 1,
      segment.y * tileSize + 1,
      tileSize - 2,
      tileSize - 2
    );
  });
}

function drawOverlay(title, subtitle) {
  ctx.fillStyle = "rgba(0, 0, 0, 0.55)";
  ctx.fillRect(0, 0, canvas.width, canvas.height);
  ctx.fillStyle = "#ffffff";
  ctx.textAlign = "center";
  ctx.font = "bold 46px Arial";
  ctx.fillText(title, canvas.width / 2, canvas.height / 2 - 10);
  ctx.font = "20px Arial";
  ctx.fillText(subtitle, canvas.width / 2, canvas.height / 2 + 28);
}

function setDirection(x, y) {
  if (x === -direction.x && y === -direction.y) {
    return;
  }
  nextDirection = { x, y };
}

document.addEventListener("keydown", (event) => {
  if (event.key === "ArrowUp" || event.key.toLowerCase() === "w") setDirection(0, -1);
  if (event.key === "ArrowDown" || event.key.toLowerCase() === "s") setDirection(0, 1);
  if (event.key === "ArrowLeft" || event.key.toLowerCase() === "a") setDirection(-1, 0);
  if (event.key === "ArrowRight" || event.key.toLowerCase() === "d") setDirection(1, 0);
  if (event.code === "Space" && !running) resetGame();
});

startBtn.addEventListener("click", resetGame);

resetGame();
