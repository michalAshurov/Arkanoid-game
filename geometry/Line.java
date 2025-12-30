package geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * this class represent a Line.
 */
public class Line {
    //Threshold for double calculation.
    private static final double THRESHOLD = 0.0001;

    private final Point start;
    private final Point end;

    /**
     * Line class Constructor.
     * @param start - First point. (where the line starts).
     * @param end - Second point. (where the line ends).
     */
    public Line(Point start, Point end) {
        this.start = new Point(start.getX(), start.getY());
        this.end = new Point(end.getX(), end.getY());
    }

    /**
     * Getter for the start point.
     *
     * @return - start point of the line.
     */
    public Point start() {
        return new Point(this.start.getX(), this.start.getY());
    }

    /**
     * Function to calculate the incline of our line.
     * (meant for calculation purposes).
     * Using the incline calculation from math classes: m = (y1 - y2) / (x1 - x2)
     * @return - value of the incline in double.
     */
    public double incline() {
        if (this.start.getX() == this.end.getX()) {
            return Double.POSITIVE_INFINITY;
        }
        return (this.end.getY() - this.start.getY()) / (this.end.getX() - this.start.getX());
    }

    /**
     * Function to find the y value once x value is 0 regardless of if it's between the start and end point or not.
     * (meant for calculation purposes).
     * Using the equation: m = (y1 - yB) / (x1 - 0) we get to this calculation
     * @return - value of y in double.
     */
    public double x0Point() {
        if (this.incline() == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }
        return this.start.getY() - this.incline() * this.start.getX();
    }

    /**
     * Find the min x value of the line.
     * @return - double value of min x.
     */
    public double minX() {
        return Math.min(this.start.getX(), this.end.getX());
    }

    /**
     * Find the max x value of the line.
     * @return - double value of max x.
     */
    public double maxX() {
        return Math.max(this.start.getX(), this.end.getX());
    }

    /**
     * Find the min y value of the line.
     * @return - double value of min y.
     */
    public double minY() {
        return Math.min(this.start.getY(), this.end.getY());
    }

    /**
     * Find the max y value of the line.
     * @return - double value of max y.
     */
    public double maxY() {
        return Math.max(this.start.getY(), this.end.getY());
    }

    /**
     * Check if the line is parallel to Y-axis.
     *
     * @return - true if the line is parallel to Y-axis, otherwise false.
     */
    public boolean isParallelY() {
        return Math.abs(this.start.getX() - this.end.getX()) < THRESHOLD;
    }

    /**
     * Check if the line is parallel to X-axis.
     *
     * @return - true if the line is parallel to X-axis, otherwise false.
     */
    public boolean isParallelX() {
        return Math.abs(this.start.getY() - this.end.getY()) < THRESHOLD;
    }

    /**
     * Find the intersection point between 2 lines.
     * Using determinant calculations from linear algebra we can find if lines are intersection and where.
     * @param other - line to check intersection with.
     * @return - if exists the intersection point, otherwise null.
     */
    public Point intersectionWith(Line other) {
        //This line (AB) represented as a1x + b1y = c1.
        double a1 = this.end.getY() - this.start.getY();
        double b1 = this.start.getX() - this.end.getX();
        double c1 = a1 * this.start.getX() + b1 * this.start.getY();
        //Other line (CD) represented as a2x + b2y = c2.
        double a2 = other.end.getY() - other.start.getY();
        double b2 = other.start.getX() - other.end.getX();
        double c2 = a2 * other.start.getX() + b2 * other.start.getY();
        //Calculate the determinant.
        double determinant = a1 * b2 - a2 * b1;

        if (determinant == 0) {
            // Lines are parallel - no intersections.
            return null;
        } else {
            double x = (b2 * c1 - b1 * c2) / determinant;
            double y = (a1 * c2 - a2 * c1) / determinant;
            Point intersection = new Point(x, y);
            // Check if the intersection point is on both line segments.
            if (this.isOnLine(intersection) && other.isOnLine(intersection)) {
                return intersection;
            } else {
                return null;
            }
        }
    }

    /**
     * Finds the closest intersection point to the start of the line with a given rectangle.
     *
     * @param rectangle - the rectangle to check for intersections with.
     * @return - closest intersection point, otherwise null if there are no intersections.
     */
    public Point closestIntersectionToStartOfLine(Rectangle rectangle) {
        List<Point> intersections = rectangle.intersectionPoints(this);
        if (intersections.isEmpty()) {
            return null;
        }
        double closestDistance = intersections.get(0).distance(this.start);
        Point closest = intersections.get(0);
        for (Point point : intersections) {
            if (point.distance(this.start) < closestDistance) {
                closestDistance = point.distance(this.start);
                closest = point;
            }
        }
        return closest;
    }


    /**
     * Checks if a point is on the finite line.
     *
     * @param point - point to be checked with.
     * @return - true if the point lies on the line segment, otherwise false.
     */
    public boolean isOnLine(Point point) {
        if (this.isParallelY()) {
            //For parallel to Y-axis make sure the x values are equal.
            if (Math.abs(point.getX() - this.start.getX()) > THRESHOLD) {
                return false;
            }
            //Make sure the y value is in range of the finite line.
            return point.getY() >= minY() - THRESHOLD && point.getY() <= maxY() + THRESHOLD;
        }
        if (this.isParallelX()) {
            //For parallel to X-axis make sure the y values are equal.
            if (Math.abs(point.getY() - this.start.getY()) > THRESHOLD) {
                return false;
            }
            //Make sure the x value is in range of the finite line.
            return point.getX() >= minX() - THRESHOLD && point.getX() <= maxX() + THRESHOLD;
        }
        //Use the formula y=mx+b to find if putting the point in our line matches and in range of finite line.
        double expectedY = incline() * point.getX() + x0Point();
        return Math.abs(point.getY() - expectedY) <= THRESHOLD
                && point.getX() >= minX() - THRESHOLD && point.getX() <= maxX() + THRESHOLD
                && point.getY() >= minY() - THRESHOLD && point.getY() <= maxY() + THRESHOLD;
    }

    /**
     * divide line to 5 parts for paddle.
     * @return - list of lines made of the 5 zones.
     */
    public List<Line> divideTo5() {
        List<Line> zones = new ArrayList<>();
        Point p1 = this.start();
        double size = Math.abs(this.end.getX() - this.start.getX()) / 5;
        for (int i = 0; i < 5; i++) {
            Point p2 = new Point(p1.getX() + size, this.start.getY());
            zones.add(new Line(p1, p2));
            p1 = p2;
        }
        //return list of lines
        return zones;
    }

}
