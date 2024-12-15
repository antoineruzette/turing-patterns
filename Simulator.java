/**
 * TuringPatternSimulator.java 
 * A class for the GUI of the 2D Turing pattern simulator.
 * 
 * Last modified: 2024-15-12
 * @author Antoine A. Ruzette
 * 
 * Introduction to Computer Science using Java II, Fall 2024, Harvard Extension School
 */

import javax.swing.*;
import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Simulator extends JPanel {
    private TuringPattern turingPattern;

    /**
     * Constructor for the TuringPatternSimulator GUI class.
     */
    public Simulator() {
        setLayout(new BorderLayout());
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(6, 2));

        // Fields for grid size
        JTextField widthField = new JTextField(String.valueOf(SimulationParameters.WIDTH));
        JTextField heightField = new JTextField(String.valueOf(SimulationParameters.HEIGHT));

        // Fields for diffusion and reaction parameters
        JTextField diffAField = new JTextField(String.valueOf(SimulationParameters.DIFF_A));
        JTextField diffBField = new JTextField(String.valueOf(SimulationParameters.DIFF_B));
        // JTextField feedRateField = new JTextField(String.valueOf(SimulationParameters.FEED_RATE));
        // JTextField killRateField = new JTextField(String.valueOf(SimulationParameters.KILL_RATE));

        // Add components to control panel
        controlPanel.add(new JLabel("Width:"));
        controlPanel.add(widthField);
        controlPanel.add(new JLabel("Height:"));
        controlPanel.add(heightField);
        controlPanel.add(new JLabel("Diffusion A:"));
        controlPanel.add(diffAField);
        controlPanel.add(new JLabel("Diffusion B:"));
        controlPanel.add(diffBField);
        // controlPanel.add(new JLabel("Feed Rate:"));
        // controlPanel.add(feedRateField);
        // controlPanel.add(new JLabel("Kill Rate:"));
        // controlPanel.add(killRateField);

        // Buttons for controlling simulation
        JButton startButton = new JButton("(Re-)Start Simulation");
        JButton stopButton = new JButton("Stop Simulation");
        JButton resumeButton = new JButton("Resume Simulation");
        stopButton.setEnabled(false);
        resumeButton.setEnabled(false);

        startButton.addActionListener(e -> {
            try {
                // Update parameters
                SimulationParameters.WIDTH = Integer.parseInt(widthField.getText());
                SimulationParameters.HEIGHT = Integer.parseInt(heightField.getText());
                SimulationParameters.DIFF_A = Double.parseDouble(diffAField.getText());
                // SimulationParameters.DIFF_B = Double.parseDouble(diffBField.getText());
                // SimulationParameters.FEED_RATE = Double.parseDouble(feedRateField.getText());
                // SimulationParameters.KILL_RATE = Double.parseDouble(killRateField.getText());

                // Start simulation
                turingPattern = new TuringPattern(SimulationParameters.WIDTH, SimulationParameters.HEIGHT, SimulationParameters.SCALE, Simulator.this);
                System.out.println("Starting simulation...");
                stopButton.setEnabled(true);
                startButton.setEnabled(false);
                resumeButton.setEnabled(false);
                new Thread(() -> turingPattern.simulate(SimulationParameters.DIFF_A, SimulationParameters.DIFF_B, SimulationParameters.FEED_RATE, SimulationParameters.KILL_RATE)).start();
                System.out.println("Simulation started...");
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input: " + ex.getMessage());
            }
        });

        stopButton.addActionListener(e -> {
            if (turingPattern != null) {
                turingPattern.pauseSimulation();
                System.out.println("Pausing simulation...");
                stopButton.setEnabled(false);
                resumeButton.setEnabled(true);
                startButton.setEnabled(true);
            }
        });

        resumeButton.addActionListener(e -> {
            if (turingPattern != null) {
                System.out.println("Resuming simulation...");
                stopButton.setEnabled(true);
                resumeButton.setEnabled(false);
                startButton.setEnabled(false);
                new Thread(() -> turingPattern.resumeSimulation()).start();
            }
        });

        // Button panel for controls
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(resumeButton);

        add(controlPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (turingPattern != null) {
                    int x = (int) Math.round((double) e.getX() / getWidth() * SimulationParameters.WIDTH);
                    int y = (int) Math.round((double) e.getY() / getHeight() * SimulationParameters.HEIGHT);
                    if (x >= 0 && x < SimulationParameters.WIDTH && y >= 0 && y < SimulationParameters.HEIGHT) {
                        turingPattern.disturbCell(x, y, SimulationParameters.BRUSH_RADIUS, SimulationParameters.BRUSH_STRENGTH);
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (turingPattern != null) {
            g.drawImage(turingPattern.getPattern(), 0, 0, this.getWidth(), this.getHeight(), null);
        } else {
            System.out.println("No pattern to display. Please start the simulation.");
        }
    }

    /**
     * Main method to launch the turing simulator.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("2D Turing Pattern Simulation");
            Simulator simulator = new Simulator();
            frame.add(simulator);
            frame.setSize(900, 1000);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
