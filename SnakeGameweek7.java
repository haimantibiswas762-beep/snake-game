/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    // ==========================================
    // GAME SETTINGS
    // ==========================================

    private final int CELL_SIZE = 40;
    private final int ROWS = 10;
    private final int COLS = 15;

    private final int WIDTH = COLS * CELL_SIZE;
    private final int HEIGHT = ROWS * CELL_SIZE;


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

    private Timer timer;

    // Score Counter
    private int score = 0;

    // Game Timer
    private int gameTime = 0;
    private Timer gameTimer;


    // ==========================================
    // SPEED CONTROL
    // ==========================================

    private int gameSpeed = 180;


    // ==========================================
    // UI COMPONENTS
    // ==========================================

    private JLabel scoreLabel;
    private JLabel timeLabel;

    private JComboBox<String> speedBox;
    private JButton restartButton;


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

        gameOver = false;

        // Reset score
        score = 0;

        // Reset game timer
        gameTime = 0;

        // Initial food
        food = new Point(10, 5);


        // ======================================
        // MOVEMENT TIMER
        // ======================================

        if (timer != null) {
            timer.stop();
        }

        timer = new Timer(gameSpeed, this);

        timer.start();


        // ======================================
        // GAME TIME TIMER
        // ======================================

        if (gameTimer != null) {
            gameTimer.stop();
        }

        gameTimer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (!gameOver) {

                    gameTime++;

                    updateLabels();
                }
            }
        });

        gameTimer.start();


        updateLabels();

        requestFocusInWindow();
    }


    // ==========================================
    // UPDATE SCORE AND TIME
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
    }


    // ==========================================
    // GAME UPDATE
    // ==========================================

    @Override
    public void actionPerformed(ActionEvent e) {

        if (!gameOver) {

            moveSnake();

            checkCollision();

            repaint();
        }
    }


    // ==========================================
    // MOVE SNAKE
    // ==========================================

    private void moveSnake() {

        Point head = snake.get(0);

        int newX =
                head.x + directionX;

        int newY =
                head.y + directionY;

        Point newHead =
                new Point(newX, newY);


        // Add new head
        snake.add(0, newHead);


        // ======================================
        // FOOD COLLISION
        // ======================================

        if (newHead.equals(food)) {

            // Increase score
            score++;

            updateLabels();

            generateFood();

        } else {

            // Remove tail
            snake.remove(
                    snake.size() - 1
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
                    (int) (
                            Math.random() * COLS
                    );

            y =
                    (int) (
                            Math.random() * ROWS
                    );

        } while (
                snake.contains(
                        new Point(x, y)
                )
        );

        food =
                new Point(x, y);
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

        repaint();
    }


    // ==========================================
    // DRAW GAME
    // ==========================================

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 =
                (Graphics2D) g;


        // ======================================
        // DRAW GAME BLOCK / BORDER
        // ======================================

        g2.setColor(Color.BLACK);

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

            }


            // ==================================
            // SNAKE BODY
            // ==================================

            else {

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


        // ======================================
        // GAME OVER INSIDE THE BLOCK
        // ======================================

        if (gameOver) {

            // Semi-transparent white area
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


            // GAME OVER text
            g2.setColor(
                    Color.RED
            );

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            50
                    )
            );


            String text =
                    "GAME OVER";


            FontMetrics metrics =
                    g2.getFontMetrics();


            int textWidth =
                    metrics.stringWidth(text);

            int textHeight =
                    metrics.getHeight();


            // Exact center of game block
            int x =
                    (WIDTH - textWidth) / 2;

            int y =
                    (HEIGHT - textHeight) / 2
                    + metrics.getAscent();


            g2.drawString(
                    text,
                    x,
                    y
            );
        }
    }


    // ==========================================
    // INVALID MOVEMENT DETECTION
    // ==========================================

    @Override
    public void keyPressed(KeyEvent e) {

        int key =
                e.getKeyCode();


        // ======================================
        // UP
        // ======================================

        if (
                key == KeyEvent.VK_UP
        ) {

            // Cannot go directly DOWN
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

            // Cannot go directly UP
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

            // Cannot go directly LEFT
            // when moving RIGHT
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

            // Cannot go directly RIGHT
            // when moving LEFT
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
    public void keyTyped(KeyEvent e) {
    }


    @Override
    public void keyReleased(KeyEvent e) {
    }


    // ==========================================
    // SET GAME SPEED
    // ==========================================

    private void setGameSpeed(int speed) {

        gameSpeed = speed;

        if (timer != null) {

            timer.setDelay(gameSpeed);
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
                                15,
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
        // GAME TIME
        // ======================================

        timeLabel =
                new JLabel(
                        "Time: 0 sec"
                );


        // ======================================
        // SPEED CONTROL
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
                                speedBox.getSelectedItem();


                        if (
                                speed.equals(
                                        "Slow"
                                )
                        ) {

                            setGameSpeed(250);

                        } else if (
                                speed.equals(
                                        "Normal"
                                )
                        ) {

                            setGameSpeed(180);

                        } else if (
                                speed.equals(
                                        "Fast"
                                )
                        ) {

                            setGameSpeed(100);
                        }
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
        // ADD COMPONENTS
        // ======================================

        panel.add(scoreLabel);

        panel.add(timeLabel);

        panel.add(speedLabel);

        panel.add(speedBox);

        panel.add(restartButton);


        return panel;
    }


    // ==========================================
    // MAIN METHOD
    // ==========================================

    public static void main(String[] args) {

        JFrame frame =
                new JFrame(
                        "Snake Game - Week 7"
                );


        SnakeGame game =
                new SnakeGame();


        // ======================================
        // TOP INFORMATION PANEL
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


        frame.setLocationRelativeTo(null);


        frame.setVisible(true);


        game.requestFocusInWindow();
    }
}