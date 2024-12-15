/**
 * Grid.java 
 * A class that initializes a 2D grid that contains width x height cell objects.
 * 
 * Last modified: 2024-12-12
 * @author Antoine A. Ruzette
 * 
 * Introduction to Computer Science using Java II, Fall 2024, Harvard Extension School
 */

import java.util.Random;

public class Grid {
    private final Cell[][] cells;

    /**
     * Constructor for the Grid class.
     * 
     * @param width the width of the grid
     * @param height the height of the grid
     */
    public Grid(int width, int height) {
        cells = new Cell[width][height];
        initialize();
    }

    /**
     * Get the cell at the specified coordinates.
     * 
     * @param x
     * @param y
     * @return the cell at the specified coordinates
     */
    public Cell getCell(int x, int y) {
        int wrappedX = (x + cells.length) % cells.length;
        int wrappedY = (y + cells[0].length) % cells[0].length;
        return cells[wrappedX][wrappedY];
    }

    /**
     * Get the width of the grid.
     * @return the width of the grid
     */
    public int getWidth() {
        return cells.length;
    }

    /**
     * Get the height of the grid.
     * @return the height of the grid
     */
    public int getHeight() {
        return cells[0].length;
    }

    /**
     * Initialize the cells on the grid, with some random concentrations for molecule B.
     */
    private void initialize() {
        Random random = new Random();
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                cells[x][y] = new Cell();
                // initial random concentration with some probability
                if (random.nextDouble() < SimulationParameters.INITIAL_PERTURBATION_PROBABILITY) {
                    cells[x][y].setB(random.nextDouble());
                }
            }
        }
    }
}