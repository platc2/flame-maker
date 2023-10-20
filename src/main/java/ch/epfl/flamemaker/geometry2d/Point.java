package ch.epfl.flamemaker.geometry2d;


/**
 * {@code Point} in a 2D-system
 *
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */
public final class Point {

    /**
     * {@code Point} at origin: (x, y) = (0, 0)
     */
    public final static Point ORIGIN = new Point(0, 0);

    /**
     * x coordinate of the {@code Point}
     */
    private final double x;

    /**
     * y coordinate of the {@code Point}
     */
    private final double y;

    /**
     * Creates a new {@code Point} with given specified coordinates (x, y)
     *
     * @param x coordinate of the point
     * @param y coordinate of the point
     */
    public Point(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return x coordinate of the {@code Point}
     */
    public double x() {
        return x;
    }

    /**
     * @return y coordinate of the {@code Point}
     */
    public double y() {
        return y;
    }

    /**
     * @return Radius of the {@code Point} <i>(Polar coordinates)</i>
     */
    public double r() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * @return Angle <b>&#952</b> of the {@code Point} <i>(Polar coordinates)</i>
     */
    public double theta() {
        return Math.atan2(y, x);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }
}
