
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.Random;

public class TuringPattern {
    private Grid currentGrid;
    private Grid nextGrid;
    private final BufferedImage pattern;
    private TuringPatternSimulator simulator;
    private boolean running = true;
    private boolean paused = false;
    private Random random = new Random();

    public TuringPattern(int width, int height, int scale, TuringPatternSimulator simulator) {
        currentGrid = new Grid(width / scale, height / scale);
        nextGrid = new Grid(width / scale, height / scale);
        pattern = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.simulator = simulator;
    }

    public BufferedImage getPattern() {
        return pattern;
    }

    public void stopSimulation() {
        running = false;
    }

    public void pauseSimulation() {
        paused = true;
    }

    public void resumeSimulation() {
        paused = false;
        synchronized (this) {
            notify();
        }
    }

    public void simulate(double diffA, double diffB, double feedRate, double killRate) {
        int width = currentGrid.getWidth();
        int height = currentGrid.getHeight();

        while (running) {
            synchronized (this) {
                while (paused) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Cell cell = currentGrid.getCell(x, y);
                    double laplaceA = laplace(currentGrid, x, y, true);
                    double laplaceB = laplace(currentGrid, x, y, false);

                    double nextA = cell.getA() + (diffA * laplaceA - cell.getA() * cell.getB() * cell.getB() + feedRate * (1 - cell.getA()));
                    double nextB = cell.getB() + (diffB * laplaceB + cell.getA() * cell.getB() * cell.getB() - (killRate + feedRate) * cell.getB());
                    
                    nextGrid.getCell(x, y).setA(nextA);
                    nextGrid.getCell(x, y).setB(nextB);
                }
            }
            swapGrids();
            updatePattern();
            simulator.repaint(); // Update GUI every iteration
        }
        System.out.println("Simulation stopped");
    }

    private double laplace(Grid grid, int x, int y, boolean isA) {
        double value = 0;
        int[][] neighbors = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        for (int[] neighbor : neighbors) {
            int nx = (x + neighbor[0] + grid.getWidth()) % grid.getWidth();
            int ny = (y + neighbor[1] + grid.getHeight()) % grid.getHeight();
            value += isA ? grid.getCell(nx, ny).getA() : grid.getCell(nx, ny).getB();
        }
        value -= 8 * (isA ? grid.getCell(x, y).getA() : grid.getCell(x, y).getB());
        return value / 8.0;
    }

    private void swapGrids() {
        Grid temp = currentGrid;
        currentGrid = nextGrid;
        nextGrid = temp;
    }

    private void updatePattern() {
        for (int x = 0; x < currentGrid.getWidth(); x++) {
            for (int y = 0; y < currentGrid.getHeight(); y++) {
                int value = (int) (Math.min(1.0, Math.max(0.0, currentGrid.getCell(x, y).getB())) * 255);
                Color color = new Color(value, value, value);
                pattern.setRGB(x, y, color.getRGB());
            }
        }
    }

    public void disturbCell(int x, int y) {
        currentGrid.getCell(x, y).setB(random.nextDouble());
        updatePattern();
        simulator.repaint();
    }
}