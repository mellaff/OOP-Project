package core.function;

/**
 * Represents a point in a 2-dimensional coordinate system.
 */
public class Point {
    private double x;
    private double y;

    /**
     * Constructs a new Point object with default coordinates (0, 0).
     */
    public Point() {
    }

    /**
     * Constructs a new Point object with the specified coordinates.
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a new Point object that is a copy of the specified Point object.
     * @param that The Point object to copy.
     */
    public Point(Point that) {
        this.x = that.x;
        this.y = that.y;
    }

    /**
     * Gets the x-coordinate of the point.
     * @return The x-coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the point.
     * @return The y-coordinate.
     */
    public double getY() {
        return y;
    }
}
