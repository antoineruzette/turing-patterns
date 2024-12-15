/**
 * TuringPattern.java 
 * A class containing an implementation of the reaction-diffusion model for evolving Turing patterns.
 * 
 * Last modified: 2024-15-12
 * @author Antoine A. Ruzette
 * 
 * Introduction to Computer Science using Java II, Fall 2024, Harvard Extension School
 */

import java.awt.image.BufferedImage;
import java.awt.Color;
// import java.util.Random;

public class TuringPattern {
    private Grid currentGrid;
    private Grid nextGrid;
    private final BufferedImage pattern;
    private Simulator simulator;
    private boolean running = true;
    private boolean paused = false;
    // private Random random = new Random();

    /**
     * Constructor for the TuringPattern class.
     * @param width the width of the 2D grid
     * @param height the height of the 2D grid
     * @param scale the scale of the grid
     * @param simulator the TuringPatternSimulator object
     */
    public TuringPattern(int width, int height, int scale, Simulator simulator) {
        currentGrid = new Grid(width / scale, height / scale);
        nextGrid = new Grid(width / scale, height / scale);
        pattern = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.simulator = simulator;
    }


    /**
     * Get the BufferedImage pattern.
     * @return
     */
    public BufferedImage getPattern() {
        return pattern;
    }

    /**
     * Stop the simulation.
     */
    public void stopSimulation() {
        running = false;
    }

    /**
     * Pause the simulation.
     */
    public void pauseSimulation() {
        paused = true;
    }

    /**
     * Start or resume the simulation.
     */
    public void resumeSimulation() {
        paused = false;
        synchronized (this) {
            notify();
        }
    }

    /**
     * Simulate the reaction-diffusion model to evolve Turing patterns.
     * 
     * @param diffA the diffusion rate of molecule A
     * @param diffB the diffusion rate of molecule B
     * @param feedRate the feed rate of molecule A
     * @param killRate the kill rate of molecule B
     */
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
            simulator.repaint(); // update the grid every iteration
        }
        System.out.println("Simulation stopped");
    }

    /**
     * Calculate the Laplacian of a cell using a 3x3 kernel.
     * 
     * @param grid the 2D grid
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     * @param isA true if the cell is molecule A, false if molecule B
     * @return the Laplacian of the cell
     */
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

    /**
     * Swap the current grid with the next grid to update the grid.
     */
    private void swapGrids() {
        Grid temp = currentGrid;
        currentGrid = nextGrid;
        nextGrid = temp;
    }

    /**
     * Update the BufferedImage based on the current grid.
     */
    private void updatePattern() {
        for (int x = 0; x < currentGrid.getWidth(); x++) {
            for (int y = 0; y < currentGrid.getHeight(); y++) {
                int value = (int) (Math.min(1.0, Math.max(0.0, currentGrid.getCell(x, y).getB())) * 255);
                Color color = new Color(value, value, value);
                pattern.setRGB(x, y, color.getRGB());
            }
        }
    }

    /**
     * Disturb a cell in the grid.
     * 
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     * @param radius the radius of the disturbance (cast to int)
     * @param strength the strength of the disturbance
     */
    public void disturbCell(int x, int y, double radius, double strength) {
        int width = currentGrid.getWidth();
        int height = currentGrid.getHeight();
        for (int i = -((int) radius); i <= radius; i++) {
            for (int j = -((int) radius); j <= radius; j++) {
                int nx = (x + i + width) % width;
                int ny = (y + j + height) % height;
                if (Math.sqrt(i * i + j * j) <= radius) {
                    Cell cell = currentGrid.getCell(nx, ny);
                    cell.setB(Math.min(1.0, cell.getB() + strength));
                }
            }
        }
        updatePattern();
        simulator.repaint();
    }
}