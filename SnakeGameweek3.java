
package snakegame;

import javax.swing.*;
import java.awt.*;

public class SnakeGame {

    private JFrame frame;

    public static void main(String[] args) {

        new SnakeGame();
    }

    public SnakeGame() {

        createMainMenu();
    }

    private void createMainMenu() {

        frame = new JFrame("Snake Game");

        frame.setSize(500, 400);

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        frame.setLocationRelativeTo(null);

        frame.setResizable(false);


        // Main Panel
        JPanel panel = new JPanel();

        panel.setLayout(
                new GridLayout(4, 1, 10, 10)
        );


        // Title
        JLabel title = new JLabel(
                "SNAKE GAME",
                SwingConstants.CENTER
        );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );


        // Start Button
        JButton startButton =
                new JButton("Start");


        // Help Button
        JButton helpButton =
                new JButton("Help");


        // Exit Button
        JButton exitButton =
                new JButton("Exit");


        // Start Event
        startButton.addActionListener(
                e -> showStartScreen()
        );


        // Help Event
        helpButton.addActionListener(
                e -> showHelpScreen()
        );


        // Exit Event
        exitButton.addActionListener(
                e -> System.exit(0)
        );


        // Add components
        panel.add(title);

        panel.add(startButton);

        panel.add(helpButton);

        panel.add(exitButton);


        frame.add(panel);

        frame.setVisible(true);
    }


    // Start Screen
    private void showStartScreen() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );


        JButton backButton =
                new JButton("Back");


        backButton.addActionListener(
                e -> createMainMenu()
        );


        panel.add(
                backButton,
                BorderLayout.SOUTH
        );


        frame.getContentPane().removeAll();

        frame.add(panel);

        frame.revalidate();

        frame.repaint();
    }


    // Help Screen
    private void showHelpScreen() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );


        JLabel helpLabel =
                new JLabel(
                        "Help",
                        SwingConstants.CENTER
                );


        helpLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );


        JButton backButton =
                new JButton("Back");


        backButton.addActionListener(
                e -> createMainMenu()
        );


        panel.add(
                helpLabel,
                BorderLayout.CENTER
        );

        panel.add(
                backButton,
                BorderLayout.SOUTH
        );


        frame.getContentPane().removeAll();

        frame.add(panel);

        frame.revalidate();

        frame.repaint();
    }
}