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
import java.util.ArrayList;

public class SnakeGame extends JPanel {

    // ==========================================
    // GAME BOARD SETTINGS
    // ==========================================

    private final int CELL_SIZE = 40;
    private final int ROWS = 10;
    private final int COLS = 15;

    private final int WIDTH = COLS * CELL_SIZE;
    private final int HEIGHT = ROWS * CELL_SIZE;


    // ==========================================
    // SNAKE
    // ==========================================

    private ArrayList<Point> snake;


    // ==========================================
    // FOOD
    // ==========================================

    private Point food;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public SnakeGame() {

        setPreferredSize(
                new Dimension(
                        WIDTH,
                        HEIGHT
                )
        );

        setBackground(Color.WHITE);


        // ======================================
        // CREATE SNAKE
        // ======================================

        snake = new ArrayList<>();

        snake.add(
                new Point(4, 5)
        );

        snake.add(
                new Point(3, 5)
        );

        snake.add(
                new Point(2, 5)
        );


        // ======================================
        // CREATE FOOD
        // ======================================

        food = new Point(10, 5);
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
        // GAME BOARD BORDER
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
    // MAIN METHOD
    // ==========================================

    public static void main(String[] args) {

        JFrame frame =
                new JFrame(
                        "Snake Game - Week 4"
                );


        SnakeGame game =
                new SnakeGame();


        // Add game board
        frame.add(game);


        // Close program
        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );


        // Fixed window size
        frame.setResizable(false);


        // Set proper size
        frame.pack();


        // Center window
        frame.setLocationRelativeTo(null);


        // Show window
        frame.setVisible(true);
    }
}