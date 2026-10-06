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
    // SNAKE MOVEMENT
    // ==========================================

    private int directionX = 1;
    private int directionY = 0;


    // ==========================================
    // GAME LOOP
    // ==========================================

    private Timer timer;


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


        // Initial food
        food = new Point(10, 5);


        // Game loop
        if (timer != null) {
            timer.stop();
        }

        timer = new Timer(180, this);

        timer.start();
    }


    // ==========================================
    // GAME LOOP
    // ==========================================

    @Override
    public void actionPerformed(ActionEvent e) {

        moveSnake();

        repaint();
    }


    // ==========================================
    // SNAKE MOVEMENT
    // ==========================================

    private void moveSnake() {

        Point head = snake.get(0);


        // Calculate new head position
        int newX =
                head.x + directionX;

        int newY =
                head.y + directionY;


        // ======================================
        // KEEP SNAKE INSIDE THE BLOCK
        // ======================================

        if (
                newX < 0 ||
                newX >= COLS ||
                newY < 0 ||
                newY >= ROWS
        ) {

            // Do not move outside the board
            return;
        }


        Point newHead =
                new Point(newX, newY);


        // Add new head
        snake.add(0, newHead);


        // ======================================
        // FOOD COLLECTION
        // ======================================

        if (newHead.equals(food)) {

            // Snake grows
            generateFood();

        } else {

            // Remove tail
            snake.remove(
                    snake.size() - 1
            );
        }
    }


    // ==========================================
    // FOOD GENERATION
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
    // DRAW GAME BOARD
    // ==========================================

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 =
                (Graphics2D) g;


        // ======================================
        // DRAW BORDER
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
        // DRAW FOOD
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
    }


    // ==========================================
    // KEYBOARD CONTROL
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

            // Cannot directly move DOWN
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

            // Cannot directly move UP
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

            // Cannot directly move LEFT
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

            // Cannot directly move RIGHT
            // when moving LEFT
            if (directionX != -1) {

                directionX = 1;
                directionY = 0;
            }
        }
    }


    // ==========================================
    // KEYBOARD METHODS
    // ==========================================

    @Override
    public void keyTyped(KeyEvent e) {
    }


    @Override
    public void keyReleased(KeyEvent e) {
    }


    // ==========================================
    // MAIN METHOD
    // ==========================================

    public static void main(String[] args) {

        JFrame frame =
                new JFrame(
                        "Snake Game - Week 5"
                );


        SnakeGame game =
                new SnakeGame();


        frame.add(game);


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