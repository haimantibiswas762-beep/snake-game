/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author LENOVO
 */
package snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    // ==========================================
    // GAME SETTINGS
    // ==========================================

    private final int CELL_SIZE = 40;
    private final int ROWS = 10;
    private final int COLS = 15;

    private final int WIDTH = COLS * CELL_SIZE;
    private final int HEIGHT = ROWS * CELL_SIZE;

    private final int TARGET_SCORE = 10;


    // ==========================================
    // SNAKE AND FOOD
    // ==========================================

    private ArrayList<Point> snake;
    private Point food;


    // ==========================================
    // MOVEMENT
    // ==========================================

    private int directionX = 1;
    private int directionY = 0;


    // ==========================================
    // GAME STATUS
    // ==========================================

    private boolean gameOver = false;
    private boolean paused = false;
    private boolean gameWon = false;

    private Timer timer;

    private int score = 0;

    private int gameTime = 0;
    private Timer gameTimer;


    // ==========================================
    // LEVEL MANAGEMENT
    // ==========================================

    private int level = 1;
    private int gameSpeed = 180;


    // ==========================================
    // HIGH SCORE
    // ==========================================

    private int highScore = 0;

    private final String HIGH_SCORE_FILE =
            "highscore.txt";


    // ==========================================
    // UI COMPONENTS
    // ==========================================

    private JLabel scoreLabel;
    private JLabel timeLabel;
    private JLabel levelLabel;
    private JLabel highScoreLabel;

    private JComboBox<String> speedBox;

    private JButton restartButton;
    private JButton pauseButton;
    private JButton aboutButton;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public SnakeGame() {

        setPreferredSize(
                new Dimension(WIDTH, HEIGHT)
        );

        setBackground(Color.WHITE);

        setFocusable(true);

        addKeyListener(this);

        loadHighScore();

        startGame();
    }


    // ==========================================
    // START GAME
    // ==========================================

    private void startGame() {

        snake = new ArrayList<>();

        // Initial snake position
        snake.add(new Point(4, 5));
        snake.add(new Point(3, 5));
        snake.add(new Point(2, 5));


        // Initial direction = RIGHT
        directionX = 1;
        directionY = 0;


        // Game status
        gameOver = false;
        paused = false;
        gameWon = false;


        // Reset score
        score = 0;


        // Reset level
        level = 1;
        gameSpeed = 180;


        // Reset time
        gameTime = 0;


        // Initial food
        food = new Point(10, 5);


        // ======================================
        // MOVEMENT TIMER
        // ======================================

        if (timer != null) {
            timer.stop();
        }

        timer = new Timer(
                gameSpeed,
                this
        );

        timer.start();


        // ======================================
        // GAME TIME TIMER
        // ======================================

        if (gameTimer != null) {
            gameTimer.stop();
        }

        gameTimer =
                new Timer(
                        1000,
                        new ActionListener() {

                            @Override
                            public void actionPerformed(
                                    ActionEvent e
                            ) {

                                if (
                                        !gameOver &&
                                        !gameWon &&
                                        !paused
                                ) {

                                    gameTime++;

                                    updateLabels();
                                }
                            }
                        }
                );

        gameTimer.start();


        updateLabels();


        if (pauseButton != null) {

            pauseButton.setText(
                    "Pause"
            );
        }


        requestFocusInWindow();
    }


    // ==========================================
    // UPDATE LABELS
    // ==========================================

    private void updateLabels() {

        if (scoreLabel != null) {

            scoreLabel.setText(
                    "Score: " + score
            );
        }


        if (timeLabel != null) {

            timeLabel.setText(
                    "Time: " + gameTime + " sec"
            );
        }


        if (levelLabel != null) {

            levelLabel.setText(
                    "Level: " + level
            );
        }


        if (highScoreLabel != null) {

            highScoreLabel.setText(
                    "High Score: " + highScore
            );
        }
    }


    // ==========================================
    // GAME UPDATE
    // ==========================================

    @Override
    public void actionPerformed(ActionEvent e) {

        if (
                !gameOver &&
                !gameWon &&
                !paused
        ) {

            moveSnake();

            checkCollision();

            repaint();
        }
    }


    // ==========================================
    // MOVE SNAKE
    // ==========================================

    private void moveSnake() {

        Point head =
                snake.get(0);


        int newX =
                head.x + directionX;

        int newY =
                head.y + directionY;


        Point newHead =
                new Point(
                        newX,
                        newY
                );


        // Add new head
        snake.add(
                0,
                newHead
        );


        // ======================================
        // FOOD COLLISION
        // ======================================

        if (
                newHead.equals(food)
        ) {

            score++;

            updateLevel();

            updateLabels();


            // ==================================
            // WIN / TARGET SCORE DETECTION
            // ==================================

            if (
                    score >= TARGET_SCORE
            ) {

                showWin();

                return;
            }


            generateFood();

        } else {

            // Remove tail
            snake.remove(
                    snake.size() - 1
            );
        }
    }


    // ==========================================
    // LEVEL MANAGEMENT
    // ==========================================

    private void updateLevel() {

        if (score >= 10) {

            level = 3;

            gameSpeed = 100;

        } else if (score >= 5) {

            level = 2;

            gameSpeed = 140;

        } else {

            level = 1;

            gameSpeed = 180;
        }


        if (timer != null) {

            timer.setDelay(
                    gameSpeed
            );
        }
    }


    // ==========================================
    // GENERATE FOOD
    // ==========================================

    private void generateFood() {

        int x;
        int y;


        do {

            x =
                    (int)
                    (
                            Math.random()
                            * COLS
                    );

            y =
                    (int)
                    (
                            Math.random()
                            * ROWS
                    );

        } while (
                snake.contains(
                        new Point(x, y)
                )
        );


        food =
                new Point(
                        x,
                        y
                );
    }


    // ==========================================
    // COLLISION DETECTION
    // ==========================================

    private void checkCollision() {

        Point head =
                snake.get(0);


        // ======================================
        // WALL COLLISION
        // ======================================

        if (
                head.x < 0 ||
                head.x >= COLS ||
                head.y < 0 ||
                head.y >= ROWS
        ) {

            showGameOver();

            return;
        }


        // ======================================
        // SNAKE BODY COLLISION
        // ======================================

        for (
                int i = 1;
                i < snake.size();
                i++
        ) {

            if (
                    head.equals(
                            snake.get(i)
                    )
            ) {

                showGameOver();

                return;
            }
        }
    }


    // ==========================================
    // GAME OVER
    // ==========================================

    private void showGameOver() {

        gameOver = true;

        timer.stop();

        gameTimer.stop();

        saveHighScore();

        repaint();
    }


    // ==========================================
    // WIN MESSAGE
    // ==========================================

    private void showWin() {

        gameWon = true;

        timer.stop();

        gameTimer.stop();

        saveHighScore();

        repaint();
    }


    // ==========================================
    // HIGH SCORE - LOAD
    // ==========================================

    private void loadHighScore() {

        try {

            File file =
                    new File(
                            HIGH_SCORE_FILE
                    );


            if (file.exists()) {

                BufferedReader reader =
                        new BufferedReader(
                                new FileReader(file)
                        );


                String line =
                        reader.readLine();


                if (line != null) {

                    highScore =
                            Integer.parseInt(
                                    line
                            );
                }


                reader.close();
            }

        } catch (
                IOException |
                NumberFormatException e
        ) {

            highScore = 0;
        }
    }


    // ==========================================
    // HIGH SCORE - SAVE
    // ==========================================

    private void saveHighScore() {

        if (score > highScore) {

            highScore = score;


            try {

                BufferedWriter writer =
                        new BufferedWriter(
                                new FileWriter(
                                        HIGH_SCORE_FILE
                                )
                        );


                writer.write(
                        String.valueOf(
                                highScore
                        )
                );


                writer.close();


                updateLabels();

            } catch (IOException e) {

                // File storage error
            }
        }
    }


    // ==========================================
    // PAUSE / RESUME
    // ==========================================

    private void togglePause() {

        if (
                gameOver ||
                gameWon
        ) {

            return;
        }


        if (paused) {

            paused = false;

            timer.start();

            gameTimer.start();


            if (pauseButton != null) {

                pauseButton.setText(
                        "Pause"
                );
            }

        } else {

            paused = true;

            timer.stop();

            gameTimer.stop();


            if (pauseButton != null) {

                pauseButton.setText(
                        "Resume"
                );
            }
        }


        requestFocusInWindow();

        repaint();
    }


    // ==========================================
    // ABOUT SECTION
    // ==========================================

    private void showAbout() {

        JOptionPane.showMessageDialog(
                this,
                "Snake Game\n\n"
                + "Week 9 Software Finalization\n"
                + "Target Score: "
                + TARGET_SCORE
                + "\n\n"
                + "Pause / Resume\n"
                + "Different Game Levels\n"
                + "High Score System\n"
                + "Win / Game Over Detection",
                "About",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    // ==========================================
    // DRAW GAME
    // ==========================================

    @Override
    protected void paintComponent(
            Graphics g
    ) {

        super.paintComponent(g);


        Graphics2D g2 =
                (Graphics2D) g;


        // ======================================
        // DRAW GAME BLOCK / BORDER
        // ======================================

        g2.setColor(
                Color.BLACK
        );


        g2.setStroke(
                new BasicStroke(3)
        );


        g2.drawRect(
                1,
                1,
                WIDTH - 3,
                HEIGHT - 3
        );


        // ======================================
        // DRAW GRID
        // ======================================

        g2.setColor(
                new Color(
                        180,
                        180,
                        180
                )
        );


        g2.setStroke(
                new BasicStroke(1)
        );


        // Vertical lines
        for (
                int x = 0;
                x <= WIDTH;
                x += CELL_SIZE
        ) {

            g2.drawLine(
                    x,
                    0,
                    x,
                    HEIGHT
            );
        }


        // Horizontal lines
        for (
                int y = 0;
                y <= HEIGHT;
                y += CELL_SIZE
        ) {

            g2.drawLine(
                    0,
                    y,
                    WIDTH,
                    y
            );
        }


        // ======================================
        // DRAW SNAKE
        // ======================================

        for (
                int i = 0;
                i < snake.size();
                i++
        ) {

            Point part =
                    snake.get(i);


            int x =
                    part.x * CELL_SIZE;

            int y =
                    part.y * CELL_SIZE;


            // ==================================
            // SNAKE HEAD
            // ==================================

            if (i == 0) {

                g2.setColor(
                        Color.GREEN
                );


                g2.fillRoundRect(
                        x + 2,
                        y + 2,
                        CELL_SIZE - 4,
                        CELL_SIZE - 4,
                        12,
                        12
                );


                // Eyes
                g2.setColor(
                        Color.BLACK
                );


                g2.fillOval(
                        x + 10,
                        y + 9,
                        6,
                        6
                );


                g2.fillOval(
                        x + 25,
                        y + 9,
                        6,
                        6
                );


                // Smile
                g2.drawArc(
                        x + 10,
                        y + 16,
                        20,
                        12,
                        200,
                        140
                );

            } else {

                // ==================================
                // SNAKE BODY
                // ==================================

                g2.setColor(
                        Color.GREEN
                );


                g2.fillRect(
                        x + 2,
                        y + 2,
                        CELL_SIZE - 4,
                        CELL_SIZE - 4
                );
            }
        }


        // ======================================
        // DRAW APPLE
        // ======================================

        if (
                !gameWon
        ) {

            int foodX =
                    food.x * CELL_SIZE;

            int foodY =
                    food.y * CELL_SIZE;


            // Apple
            g2.setColor(
                    Color.RED
            );


            g2.fillOval(
                    foodX + 7,
                    foodY + 10,
                    27,
                    25
            );


            // Apple leaf
            g2.setColor(
                    Color.GREEN
            );


            g2.fillOval(
                    foodX + 23,
                    foodY + 3,
                    14,
                    8
            );


            // Apple stem
            g2.setColor(
                    Color.BLACK
            );


            g2.setStroke(
                    new BasicStroke(2)
            );


            g2.drawLine(
                    foodX + 19,
                    foodY + 10,
                    foodX + 21,
                    foodY + 3
            );
        }


        // ======================================
        // PAUSED MESSAGE
        // ======================================

        if (paused) {

            g2.setColor(
                    new Color(
                            255,
                            255,
                            255,
                            210
                    )
            );


            g2.fillRect(
                    0,
                    0,
                    WIDTH,
                    HEIGHT
            );


            g2.setColor(
                    Color.BLUE
            );


            g2.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            45
                    )
            );


            String text =
                    "PAUSED";


            FontMetrics metrics =
                    g2.getFontMetrics();


            int textWidth =
                    metrics.stringWidth(
                            text
                    );


            int textHeight =
                    metrics.getHeight();


            int x =
                    (WIDTH - textWidth)
                    / 2;


            int y =
                    (HEIGHT - textHeight)
                    / 2
                    + metrics.getAscent();


            g2.drawString(
                    text,
                    x,
                    y
            );
        }


        // ======================================
        // WIN MESSAGE
        // ======================================

        if (gameWon) {

            g2.setColor(
                    new Color(
                            255,
                            255,
                            255,
                            220
                    )
            );


            g2.fillRect(
                    0,
                    0,
                    WIDTH,
                    HEIGHT
            );


            g2.setColor(
                    new Color(
                            0,
                            140,
                            0
                    )
            );


            g2.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            42
                    )
            );


            String text =
                    "YOU WIN!";


            FontMetrics metrics =
                    g2.getFontMetrics();


            int textWidth =
                    metrics.stringWidth(
                            text
                    );


            int textHeight =
                    metrics.getHeight();


            int x =
                    (WIDTH - textWidth)
                    / 2;


            int y =
                    (HEIGHT - textHeight)
                    / 2
                    + metrics.getAscent();


            g2.drawString(
                    text,
                    x,
                    y
            );
        }


        // ======================================
        // GAME OVER MESSAGE
        // ======================================

        if (gameOver) {

            g2.setColor(
                    new Color(
                            255,
                            255,
                            255,
                            220
                    )
            );


            g2.fillRect(
                    0,
                    0,
                    WIDTH,
                    HEIGHT
            );


            g2.setColor(
                    Color.RED
            );


            g2.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            45
                    )
            );


            String text =
                    "GAME OVER";


            FontMetrics metrics =
                    g2.getFontMetrics();


            int textWidth =
                    metrics.stringWidth(
                            text
                    );


            int textHeight =
                    metrics.getHeight();


            int x =
                    (WIDTH - textWidth)
                    / 2;


            int y =
                    (HEIGHT - textHeight)
                    / 2
                    + metrics.getAscent();


            g2.drawString(
                    text,
                    x,
                    y
            );
        }
    }


    // ==========================================
    // KEY PRESSED
    // ==========================================

    @Override
    public void keyPressed(
            KeyEvent e
    ) {

        int key =
                e.getKeyCode();


        // ======================================
        // PAUSE / RESUME
        // ======================================

        if (
                key == KeyEvent.VK_P
        ) {

            togglePause();

            return;
        }


        if (
                paused ||
                gameOver ||
                gameWon
        ) {

            return;
        }


        // ======================================
        // UP
        // ======================================

        if (
                key == KeyEvent.VK_UP
        ) {

            if (directionY != 1) {

                directionX = 0;

                directionY = -1;
            }
        }


        // ======================================
        // DOWN
        // ======================================

        else if (
                key == KeyEvent.VK_DOWN
        ) {

            if (directionY != -1) {

                directionX = 0;

                directionY = 1;
            }
        }


        // ======================================
        // LEFT
        // ======================================

        else if (
                key == KeyEvent.VK_LEFT
        ) {

            if (directionX != 1) {

                directionX = -1;

                directionY = 0;
            }
        }


        // ======================================
        // RIGHT
        // ======================================

        else if (
                key == KeyEvent.VK_RIGHT
        ) {

            if (directionX != -1) {

                directionX = 1;

                directionY = 0;
            }
        }
    }


    // ==========================================
    // KEY METHODS
    // ==========================================

    @Override
    public void keyTyped(
            KeyEvent e
    ) {
    }


    @Override
    public void keyReleased(
            KeyEvent e
    ) {
    }


    // ==========================================
    // SET GAME SPEED
    // ==========================================

    private void setGameSpeed(
            int speed
    ) {

        gameSpeed = speed;


        if (timer != null) {

            timer.setDelay(
                    gameSpeed
            );
        }


        requestFocusInWindow();
    }


    // ==========================================
    // CREATE TOP PANEL
    // ==========================================

    private JPanel createTopPanel() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                12,
                                8
                        )
                );


        // ======================================
        // SCORE
        // ======================================

        scoreLabel =
                new JLabel(
                        "Score: 0"
                );


        // ======================================
        // TIME
        // ======================================

        timeLabel =
                new JLabel(
                        "Time: 0 sec"
                );


        // ======================================
        // LEVEL
        // ======================================

        levelLabel =
                new JLabel(
                        "Level: 1"
                );


        // ======================================
        // HIGH SCORE
        // ======================================

        highScoreLabel =
                new JLabel(
                        "High Score: "
                        + highScore
                );


        // ======================================
        // SPEED
        // ======================================

        JLabel speedLabel =
                new JLabel(
                        "Speed:"
                );


        speedBox =
                new JComboBox<>(
                        new String[]{
                                "Slow",
                                "Normal",
                                "Fast"
                        }
                );


        speedBox.setSelectedItem(
                "Normal"
        );


        speedBox.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(
                            ActionEvent e
                    ) {

                        String speed =
                                (String)
                                speedBox
                                .getSelectedItem();


                        if (
                                speed.equals(
                                        "Slow"
                                )
                        ) {

                            setGameSpeed(
                                    250
                            );

                        } else if (
                                speed.equals(
                                        "Normal"
                                )
                        ) {

                            setGameSpeed(
                                    180
                            );

                        } else if (
                                speed.equals(
                                        "Fast"
                                )
                        ) {

                            setGameSpeed(
                                    100
                            );
                        }
                    }
                }
        );


        // ======================================
        // PAUSE BUTTON
        // ======================================

        pauseButton =
                new JButton(
                        "Pause"
                );


        pauseButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(
                            ActionEvent e
                    ) {

                        togglePause();
                    }
                }
        );


        // ======================================
        // RESTART BUTTON
        // ======================================

        restartButton =
                new JButton(
                        "Restart"
                );


        restartButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(
                            ActionEvent e
                    ) {

                        startGame();
                    }
                }
        );


        // ======================================
        // ABOUT BUTTON
        // ======================================

        aboutButton =
                new JButton(
                        "About"
                );


        aboutButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(
                            ActionEvent e
                    ) {

                        showAbout();
                    }
                }
        );


        // ======================================
        // ADD COMPONENTS
        // ======================================

        panel.add(
                scoreLabel
        );

        panel.add(
                timeLabel
        );

        panel.add(
                levelLabel
        );

        panel.add(
                highScoreLabel
        );

        panel.add(
                speedLabel
        );

        panel.add(
                speedBox
        );

        panel.add(
                pauseButton
        );

        panel.add(
                restartButton
        );

        panel.add(
                aboutButton
        );


        return panel;
    }


    // ==========================================
    // MAIN METHOD
    // ==========================================

    public static void main(
            String[] args
    ) {

        JFrame frame =
                new JFrame(
                        "Snake Game - Week 9"
                );


        SnakeGame game =
                new SnakeGame();


        // ======================================
        // TOP PANEL
        // ======================================

        JPanel topPanel =
                game.createTopPanel();


        frame.setLayout(
                new BorderLayout()
        );


        frame.add(
                topPanel,
                BorderLayout.NORTH
        );


        frame.add(
                game,
                BorderLayout.CENTER
        );


        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );


        frame.setResizable(false);


        frame.pack();


        frame.setLocationRelativeTo(
                null
        );


        frame.setVisible(true);


        game.requestFocusInWindow();
    }
}
