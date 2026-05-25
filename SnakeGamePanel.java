import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGamePanel extends JPanel implements ActionListener, KeyListener {

    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private enum FoodType {
        APPLE,
        ORANGE,
        STAR
    }

    private JFrame frame;

    private int boardWidth;
    private int boardHeight;
    private int tileSize = 25;

    private Tile snakeHead;
    private ArrayList<Tile> snakeBody;

    private Tile food;
    private FoodType currentFoodType;
    private Random random;

    private Timer gameLoop;
    private int velocityX;
    private int velocityY;

    private boolean gameOver = false;
    private boolean paused = false;
    private boolean scoreSaved = false;

    private DatabaseSnakeGame db;

    private int userId;
    private String username;

    private int score = 0;
    private int level = 1;
    private String difficulty;

    private long startTime;

    public SnakeGamePanel(int boardWidth, int boardHeight) {
        this(null, boardWidth, boardHeight, 1, "Player", "Medium");
    }

    public SnakeGamePanel(int boardWidth, int boardHeight, int userId, String username) {
        this(null, boardWidth, boardHeight, userId, username, "Medium");
    }

    public SnakeGamePanel(int boardWidth, int boardHeight, int userId, String username, String difficulty) {
        this(null, boardWidth, boardHeight, userId, username, difficulty);
    }

    public SnakeGamePanel(JFrame frame, int boardWidth, int boardHeight, int userId, String username, String difficulty) {
        this.frame = frame;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.userId = userId;
        this.username = username;
        this.difficulty = difficulty;

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        random = new Random();

        startNewGame();
    }

    private void startNewGame() {
        if (gameLoop != null) {
            gameLoop.stop();
        }

        if (db != null) {
            db.disconnect();
        }

        db = new DatabaseSnakeGame();
        db.connect();

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();

        food = new Tile(10, 10);
        chooseFoodType();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        score = 0;
        level = 1;

        gameOver = false;
        paused = false;
        scoreSaved = false;

        startTime = System.currentTimeMillis();

        int delay = getStartingDelay();

        gameLoop = new Timer(delay, this);
        gameLoop.start();

        requestFocusInWindow();
        repaint();
    }

    private int getStartingDelay() {
        if (difficulty.equalsIgnoreCase("Easy")) {
            return 130;
        } else if (difficulty.equalsIgnoreCase("Hard")) {
            return 75;
        } else {
            return 100;
        }
    }

    private int getDelayForCurrentLevel() {
        int baseDelay = getStartingDelay();

        if (level == 1) {
            return baseDelay;
        } else if (level == 2) {
            return Math.max(baseDelay - 20, 45);
        } else {
            return Math.max(baseDelay - 40, 35);
        }
    }

    private void chooseFoodType() {
        int chance = random.nextInt(100);

        if (chance < 60) {
            currentFoodType = FoodType.APPLE;
        } else if (chance < 90) {
            currentFoodType = FoodType.ORANGE;
        } else {
            currentFoodType = FoodType.STAR;
        }
    }

    private int getFoodPoints() {
        if (currentFoodType == FoodType.ORANGE) {
            return 2;
        } else if (currentFoodType == FoodType.STAR) {
            return 3;
        } else {
            return 1;
        }
    }

    private String getFoodName() {
        if (currentFoodType == FoodType.ORANGE) {
            return "Orange";
        } else if (currentFoodType == FoodType.STAR) {
            return "Star";
        } else {
            return "Apple";
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        drawGrid(g);
        drawFood(g);
        drawSnake(g);
        drawTopDisplay(g);
        drawInstructions(g);

        if (paused && !gameOver) {
            drawPausedMessage(g);
        }

        if (gameOver) {
            drawGameOverMessage(g);
        }
    }

    private void drawGrid(Graphics g) {
        for (int i = 0; i < boardWidth / tileSize; i++) {
            g.setColor(Color.DARK_GRAY);
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }
    }

    private void drawFood(Graphics g) {
        int x = food.x * tileSize;
        int y = food.y * tileSize;

        if (currentFoodType == FoodType.APPLE) {
            g.setColor(Color.RED);
            g.fillOval(x + 2, y + 2, tileSize - 4, tileSize - 4);
        } else if (currentFoodType == FoodType.ORANGE) {
            g.setColor(Color.ORANGE);
            g.fillOval(x + 2, y + 2, tileSize - 4, tileSize - 4);
        } else {
            g.setColor(Color.YELLOW);
            drawStar(g, x + tileSize / 2, y + tileSize / 2, 10, 5);
        }
    }

    private void drawStar(Graphics g, int centerX, int centerY, int outerRadius, int innerRadius) {
        int points = 10;
        int[] xPoints = new int[points];
        int[] yPoints = new int[points];

        for (int i = 0; i < points; i++) {
            double angle = Math.PI / 2 + i * Math.PI / 5;
            int radius = (i % 2 == 0) ? outerRadius : innerRadius;

            xPoints[i] = centerX + (int) (Math.cos(angle) * radius);
            yPoints[i] = centerY - (int) (Math.sin(angle) * radius);
        }

        g.fillPolygon(xPoints, yPoints, points);
    }

    private void drawSnake(Graphics g) {
        g.setColor(Color.GREEN);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        for (Tile snakePart : snakeBody) {
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }
    }

    private void drawTopDisplay(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.setColor(Color.WHITE);

        g.drawString("Player: " + username, 20, 30);
        g.drawString("Level: " + level, boardWidth / 2 - 45, 30);
        g.drawString("Score: " + score, boardWidth - 135, 30);
        g.drawString("Difficulty: " + difficulty, 20, 55);

        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Food: " + getFoodName() + " (+" + getFoodPoints() + ")", boardWidth - 170, 55);
    }

    private void drawInstructions(Graphics g) {
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.setColor(Color.LIGHT_GRAY);

        g.drawString("Arrow Keys = Move", 20, boardHeight - 65);
        g.drawString("Space = Pause/Resume", 20, boardHeight - 45);
        g.drawString("R = Reset/New Game", 20, boardHeight - 25);
        g.drawString("M = Return to Menu", boardWidth - 170, boardHeight - 25);
    }

    private void drawPausedMessage(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 42));
        g.setColor(Color.YELLOW);

        String pauseText = "Paused";
        FontMetrics metrics = g.getFontMetrics();

        int x = (boardWidth - metrics.stringWidth(pauseText)) / 2;
        int y = boardHeight / 2;

        g.drawString(pauseText, x, y);
    }

    private void drawGameOverMessage(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 45));
        g.setColor(Color.RED);

        String gameOverText = "Game Over";
        FontMetrics metrics = g.getFontMetrics();

        int x = (boardWidth - metrics.stringWidth(gameOverText)) / 2;
        int y = boardHeight / 2 - 70;

        g.drawString(gameOverText, x, y);

        g.setFont(new Font("Arial", Font.BOLD, 28));
        String finalScoreText = "Final Score: " + score;
        metrics = g.getFontMetrics();

        int scoreX = (boardWidth - metrics.stringWidth(finalScoreText)) / 2;
        int scoreY = boardHeight / 2 - 25;

        g.drawString(finalScoreText, scoreX, scoreY);

        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.WHITE);

        String resetText = "Press R to start a new game";
        metrics = g.getFontMetrics();

        int resetX = (boardWidth - metrics.stringWidth(resetText)) / 2;
        int resetY = boardHeight / 2 + 25;

        g.drawString(resetText, resetX, resetY);

        String menuText = "Press M to return to menu";
        metrics = g.getFontMetrics();

        int menuX = (boardWidth - metrics.stringWidth(menuText)) / 2;
        int menuY = boardHeight / 2 + 55;

        g.drawString(menuText, menuX, menuY);
    }

    private void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);

        while (food.x == snakeHead.x && food.y == snakeHead.y) {
            food.x = random.nextInt(boardWidth / tileSize);
            food.y = random.nextInt(boardHeight / tileSize);
        }

        for (Tile snakePart : snakeBody) {
            if (food.x == snakePart.x && food.y == snakePart.y) {
                placeFood();
                return;
            }
        }
    }

    private boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    private void move() {
        if (velocityX == 0 && velocityY == 0) {
            return;
        }

        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));

            score += getFoodPoints();

            updateLevel();
            chooseFoodType();
            placeFood();
        }

        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);

            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile previousPart = snakeBody.get(i - 1);
                snakePart.x = previousPart.x;
                snakePart.y = previousPart.y;
            }
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        for (Tile snakePart : snakeBody) {
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        if (snakeHead.x < 0 ||
                snakeHead.x >= boardWidth / tileSize ||
                snakeHead.y < 0 ||
                snakeHead.y >= boardHeight / tileSize) {
            gameOver = true;
        }
    }

    private void updateLevel() {
        int oldLevel = level;

        if (score >= 15) {
            level = 3;
        } else if (score >= 7) {
            level = 2;
        } else {
            level = 1;
        }

        if (level != oldLevel) {
            updateGameSpeed();
        }
    }

    private void updateGameSpeed() {
        gameLoop.setDelay(getDelayForCurrentLevel());
    }

    private void saveFinalScore() {
        if (scoreSaved) {
            return;
        }

        int durationSec = (int) ((System.currentTimeMillis() - startTime) / 1000);

        db.saveScore(userId, score, level, difficulty, durationSec);

        scoreSaved = true;

        System.out.println("Saved score for user ID: " + userId);
        System.out.println("Username: " + username);
        System.out.println("Score: " + score);
        System.out.println("Level: " + level);
        System.out.println("Difficulty: " + difficulty);
        System.out.println("Duration: " + durationSec + " seconds");
    }

    private void returnToMenu() {
        if (gameLoop != null) {
            gameLoop.stop();
        }

        if (db != null) {
            db.disconnect();
        }

        if (frame == null) {
            return;
        }

        MenuWindowPanel menuPanel = new MenuWindowPanel(frame, boardWidth, boardHeight);

        frame.setContentPane(menuPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!paused && !gameOver) {
            move();
        }

        repaint();

        if (gameOver) {
            gameLoop.stop();
            saveFinalScore();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameOver) {
                paused = !paused;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            startNewGame();
        } else if (e.getKeyCode() == KeyEvent.VK_M) {
            returnToMenu();
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }
}