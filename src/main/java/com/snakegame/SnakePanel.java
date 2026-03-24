package com.snakegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class SnakePanel extends JPanel {
    private static final int TILE_SIZE = 20;
    private static final int GRID_WIDTH = 30;
    private static final int GRID_HEIGHT = 30;
    private static final int PANEL_WIDTH = GRID_WIDTH * TILE_SIZE;
    private static final int PANEL_HEIGHT = GRID_HEIGHT * TILE_SIZE;
    private static final int TICK_MS = 120;

    private final Random random = new Random();
    private final LinkedList<Point> snake = new LinkedList<>();
    private final Timer timer;

    private Direction direction = Direction.RIGHT;
    private Direction nextDirection = Direction.RIGHT;
    private Point food;
    private boolean running;
    private int score;
    private int highScore;

    public SnakePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(new Color(18, 18, 18));
        setFocusable(true);

        timer = new Timer(TICK_MS, this::tick);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> trySetDirection(Direction.UP);
                    case KeyEvent.VK_DOWN -> trySetDirection(Direction.DOWN);
                    case KeyEvent.VK_LEFT -> trySetDirection(Direction.LEFT);
                    case KeyEvent.VK_RIGHT -> trySetDirection(Direction.RIGHT);
                    case KeyEvent.VK_R -> {
                        if (!running) {
                            startNewGame();
                        }
                    }
                    default -> {
                        // Ignore other keys
                    }
                }
            }
        });

        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        inputMap.put(KeyStroke.getKeyStroke("SPACE"), "restart");
        actionMap.put("restart", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running) {
                    startNewGame();
                }
            }
        });

        startNewGame();
    }

    private void startNewGame() {
        snake.clear();
        int centerX = GRID_WIDTH / 2;
        int centerY = GRID_HEIGHT / 2;
        snake.add(new Point(centerX, centerY));
        snake.add(new Point(centerX - 1, centerY));
        snake.add(new Point(centerX - 2, centerY));
        direction = Direction.RIGHT;
        nextDirection = Direction.RIGHT;
        score = 0;
        running = true;
        spawnFood();
        timer.start();
        requestFocusInWindow();
        repaint();
    }

    private void tick(ActionEvent event) {
        if (!running) {
            return;
        }

        direction = nextDirection;
        Point head = snake.getFirst();
        Point nextHead = new Point(head.x + direction.dx, head.y + direction.dy);

        if (isOutOfBounds(nextHead) || hitsSnake(nextHead)) {
            running = false;
            timer.stop();
            if (score > highScore) {
                highScore = score;
            }
            Toolkit.getDefaultToolkit().beep();
            repaint();
            return;
        }

        snake.addFirst(nextHead);
        if (nextHead.equals(food)) {
            score++;
            spawnFood();
        } else {
            snake.removeLast();
        }

        repaint();
    }

    private boolean isOutOfBounds(Point p) {
        return p.x < 0 || p.y < 0 || p.x >= GRID_WIDTH || p.y >= GRID_HEIGHT;
    }

    private boolean hitsSnake(Point point) {
        for (Point segment : snake) {
            if (segment.equals(point)) {
                return true;
            }
        }
        return false;
    }

    private void spawnFood() {
        Point candidate;
        do {
            candidate = new Point(random.nextInt(GRID_WIDTH), random.nextInt(GRID_HEIGHT));
        } while (hitsSnake(candidate));
        food = candidate;
    }

    private void trySetDirection(Direction newDirection) {
        if (newDirection.isOpposite(direction)) {
            return;
        }
        nextDirection = newDirection;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawGrid(g);
        drawFood(g);
        drawSnake(g);
        drawHud(g);

        if (!running) {
            drawGameOver(g);
        }
    }

    private void drawGrid(Graphics g) {
        g.setColor(new Color(32, 32, 32));
        for (int x = 0; x <= PANEL_WIDTH; x += TILE_SIZE) {
            g.drawLine(x, 0, x, PANEL_HEIGHT);
        }
        for (int y = 0; y <= PANEL_HEIGHT; y += TILE_SIZE) {
            g.drawLine(0, y, PANEL_WIDTH, y);
        }
    }

    private void drawFood(Graphics g) {
        g.setColor(new Color(224, 72, 72));
        g.fillOval(food.x * TILE_SIZE + 2, food.y * TILE_SIZE + 2, TILE_SIZE - 4, TILE_SIZE - 4);
    }

    private void drawSnake(Graphics g) {
        for (int i = 0; i < snake.size(); i++) {
            Point part = snake.get(i);
            if (i == 0) {
                g.setColor(new Color(84, 220, 116));
            } else {
                g.setColor(new Color(56, 176, 92));
            }
            g.fillRect(part.x * TILE_SIZE + 1, part.y * TILE_SIZE + 1, TILE_SIZE - 2, TILE_SIZE - 2);
        }
    }

    private void drawHud(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g.drawString("Score: " + score, 12, 20);
        g.drawString("High: " + highScore, 120, 20);
    }

    private void drawGameOver(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Segoe UI", Font.BOLD, 36));
        String gameOver = "Game Over";
        int gameOverX = (PANEL_WIDTH - g.getFontMetrics().stringWidth(gameOver)) / 2;
        g.drawString(gameOver, gameOverX, PANEL_HEIGHT / 2 - 10);

        g.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        String restart = "Press R or Space to Restart";
        int restartX = (PANEL_WIDTH - g.getFontMetrics().stringWidth(restart)) / 2;
        g.drawString(restart, restartX, PANEL_HEIGHT / 2 + 30);
    }

    private enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        private final int dx;
        private final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        private boolean isOpposite(Direction other) {
            return this.dx + other.dx == 0 && this.dy + other.dy == 0;
        }
    }
}
