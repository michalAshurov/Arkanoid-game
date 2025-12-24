package geometry;
import java.util.Random;

/**
 * this class represent a point.
 */
public class Point {
    private double x;
    private double y;

    /**
     * Point class Constructor.
     * @param x - x value of the point.
     * @param y - y value of the point.
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * calculate the distance between a point to another point.
     * @param other - the other point.
     * @return - distance between the two points .
     */
    public double distance(Point other) {
        if (other == null) {
            return -1;
        }
        return Math.sqrt((this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y));
    }

    /**
     * compare between the two points.
     * @param other - the other point.
     * @return - The comparison value.
     */
    public boolean equals(Point other) {
        if (other == null) {
            return false;
        }
        double epsilon = 0.00001;
        //There can be a small difference between two variables of the same values in double, use epsilon for checking.
        return (Math.abs(this.getX() - other.getX()) <= epsilon) && (Math.abs(this.getY() - other.getY()) <= epsilon);
    }

    /**
     * Getter for the y value variable.
     * @return - the value of the x variable of our point.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Getter for the y value variable.
     * @return - the value of the y variable of our point.
     */
    public double getY() {
        return this.y;
    }

    /**
     * Setter for the x value of the point.
     * @param x - new x value.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Setter for the y value of the point.
     * @param y - new y value.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Generate a random point in the provided area.
     * @param minX - min value allowed for x.
     * @param maxX - max value allowed for x.
     * @param minY - min value allowed for y.
     * @param maxY - max value allowed for y.
     * @return - point instance between the min and max values of x and y.
     */
    public static Point randomPoint(int minX, int maxX, int minY, int maxY) {
        Random rnd = new Random();
        int x = rnd.nextInt((maxX - minX) + 1) + minX;
        int y = rnd.nextInt((maxY - minY) + 1) + minY;
        return new Point(x, y);
    }

    @Override
    public String toString() {
        return "Point: (" + this.x + ", " + this.y + ")";
    }

}
