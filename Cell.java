/**
 * Cell.java 
 * A class for a cell object characterized by molecular concentrations.
 * 
 * Last modified: 2024-12-12
 * @author Antoine A. Ruzette
 * 
 * Introduction to Computer Science using Java II, Fall 2024, Harvard Extension School
 */

/**
 * The Cell class represents a cell with two chemical concentrations, A and B.
 * 
 * @param A the concentration of molecule A
 * @param B the concentration of molecule B
 */
public class Cell {
    private double A;
    private double B;

    /**
     * Constructor for a cell with default concentrations of 1.0 for A and 0.0 for B.
     */
    public Cell() {
        this.A = 1.0;
        this.B = 0.0;
    }

    /**
     * Returns the concentration of molecule A.
     * 
     * @return the concentration of molecule A
     */
    public double getA() {
        return A;
    }

    /**
     * Sets the concentration of molecule A.
     * 
     * @param a the concentration of molecule A
     */
    public void setA(double a) {
        A = a;
    }

    /**
     * Returns the concentration of molecule B.
     * 
     * @return the concentration of molecule B
     */
    public double getB() {
        return B;
    }

    /**
     * Sets the concentration of molecule B.
     * 
     * @param b the concentration of molecule B
     */
    public void setB(double b) {
        B = b;
    }
}