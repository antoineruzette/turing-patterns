import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TuringPatternSimulator extends JPanel {
    private TuringPattern turingPattern;
    private boolean editMode = false;

    public TuringPatternSimulator() {
        setLayout(new BorderLayout());
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 2));

        JTextField widthField = new JTextField(String.valueOf(SimulationParameters.WIDTH));
        JTextField heightField = new JTextField(String.valueOf(SimulationParameters.HEIGHT));

        controlPanel.add(new JLabel("Width:"));
        controlPanel.add(widthField);
        controlPanel.add(new JLabel("Height:"));
        controlPanel.add(heightField);

        JButton startButton = new JButton("(Re-)Start Simulation");
        JButton stopButton = new JButton("Stop Simulation");
        JButton resumeButton = new JButton("Resume Simulation");
        stopButton.setEnabled(false);
        resumeButton.setEnabled(false);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SimulationParameters.WIDTH = Integer.parseInt(widthField.getText());
                    SimulationParameters.HEIGHT = Integer.parseInt(heightField.getText());

                    turingPattern = new TuringPattern(SimulationParameters.WIDTH, SimulationParameters.HEIGHT, SimulationParameters.SCALE, TuringPatternSimulator.this);
                    System.out.println("Starting simulation...");
                    stopButton.setEnabled(true);
                    startButton.setEnabled(false);
                    resumeButton.setEnabled(false);
                    new Thread(() -> turingPattern.simulate(SimulationParameters.DIFF_A, SimulationParameters.DIFF_B, SimulationParameters.FEED_RATE, SimulationParameters.KILL_RATE)).start();
                    System.out.println("Simulation started...");
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input: " + ex.getMessage());
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (turingPattern != null) {
                    turingPattern.pauseSimulation();
                    System.out.println("Pausing simulation...");
                    stopButton.setEnabled(false);
                    resumeButton.setEnabled(true);
                    startButton.setEnabled(true);
                }
            }
        });

        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (turingPattern != null) {
                    System.out.println("Resuming simulation...");
                    stopButton.setEnabled(true);
                    resumeButton.setEnabled(false);
                    startButton.setEnabled(false);
                    new Thread(() -> turingPattern.resumeSimulation()).start();
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(resumeButton);

        add(controlPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (editMode && turingPattern != null) {
                    int x = e.getX() / SimulationParameters.SCALE;
                    int y = e.getY() / SimulationParameters.SCALE;
                    if (x >= 0 && x < SimulationParameters.WIDTH && y >= 0 && y < SimulationParameters.HEIGHT) {
                        turingPattern.disturbCell(x, y);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("2D Turing Pattern Simulation");
            TuringPatternSimulator simulator = new TuringPatternSimulator();
            frame.add(simulator);
            frame.setSize(900, 1000);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
