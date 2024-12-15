/**
 * SimulationParameters.java 
 * A class that acts as a placeholder to store simulation parameters.
 * 
 * Last modified: 2024-12-12
 * @author Antoine A. Ruzette
 * 
 * Introduction to Computer Science using Java II, Fall 2024, Harvard Extension School
 */
 public class SimulationParameters {
    public static int WIDTH = 400;                                  // Width of the grid
    public static int HEIGHT = 400;                                 // Height of the grid
    public static int SCALE = 1;                                    // Set SCALE to 1 for better visualization
    public static double FEED_RATE = 0.055;                         // Feed rate of B
    public static double KILL_RATE = 0.062;                         // Kill rate of B
    public static double DIFF_A = 1.0;                              // Diffusion rate of A
    public static double DIFF_B = 0.5;                              // Diffusion rate of B
    public static double INITIAL_PERTURBATION_PROBABILITY = 0.02;   // 2% of cells will have an initial random concentration
    public static double BRUSH_RADIUS = 5.0;                        // Radius of the brush for user perturbation
    public static double BRUSH_STRENGTH = 0.1;                      // Increment of B concentration when the brush is applied
}