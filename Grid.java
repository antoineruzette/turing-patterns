
import java.util.Random;

public class Grid {
    private final Cell[][] cells;

    public Grid(int width, int height) {
        cells = new Cell[width][height];
        initialize();
    }

    public Cell getCell(int x, int y) {
        int wrappedX = (x + cells.length) % cells.length;
        int wrappedY = (y + cells[0].length) % cells[0].length;
        return cells[wrappedX][wrappedY];
    }

    public int getWidth() {
        return cells.length;
    }

    public int getHeight() {
        return cells[0].length;
    }

    private void initialize() {
        Random random = new Random();
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                cells[x][y] = new Cell();
                // Introduce an initial perturbation with some probability
                if (random.nextDouble() < SimulationParameters.INITIAL_PERTURBATION_PROBABILITY) {
                    cells[x][y].setB(random.nextDouble());
                }
            }
        }
    }
}