package ch.epfl.flamemaker.geometry2d;


/**
 * {@code Rectangle} in a 2D-system
 *
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */
public final class Rectangle {

    /**
     * The center of the {@code Rectangle}
     */
    private final Point center;

    /**
     * The width of the {@code Rectangle}
     */
    private final double width;

    /**
     * The height of the {@code Rectangle}
     */
    private final double height;

    /**
     * Creates a new {@code Rectangle} given the center, width and height
     *
     * @param    center of the Rectangle
     * @param    width of the Rectangle
     * @param    height of the Rectangle
     * @see        ch.epfl.flamemaker.geometry2d.Point
     * @throws java.lang.IllegalArgumentException if the dimension are not greater than zero
     */
    public Rectangle(final Point center, final double width, final double height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Rectangle's dimension must be greater than zero");
        }

        this.center = center;
        this.width = width;
        this.height = height;
    }

    /**
     * @return the minimum x coordinate
     */
    public double left() {
        return center.x() - (width / 2.0);
    }

    /**
     * @return the maximum x coordinate
     */
    public double right() {
        return center.x() + (width / 2.0);
    }

    /**
     * @return the minimum y coordinate
     */
    public double bottom() {
        return center.y() - (height / 2.0);
    }

    /**
     * @return the maximum y coordinate
     */
    public double top() {
        return center.y() + (height / 2.0);
    }

    /**
     * @return the width of the {@code Rectangle}
     */
    public double width() {
        return width;
    }

    /**
     * @return the height of the {@code Rectangle}
     */
    public double height() {
        return height;
    }

    /**
     * @return the center of the {@code Rectangle}
     * @see        ch.epfl.flamemaker.geometry2d.Point
     */
    public Point center() {
        return center;
    }

    /**
     * Checks whether a {@code Point} is contained in the {@code Rectangle} or not.
     * A {@code Point} is contained in the {@code Rectangle} if it's coordinates are
     * greater or equal to {@link #left()} and {@link #bottom()} and
     * less than {@link #right()} and {@link #top()}
     *
     * @param    p {@code Point} to check
     * @return true if the {@code Point} is in the {@code Rectangle}
     * @see        ch.epfl.flamemaker.geometry2d.Point
     */
    public boolean contains(final Point p) {
        return (p.x() >= left()
                && p.x() < right()
                && p.y() >= bottom()
                && p.y() < top());
    }

    /**
     * Calculates and returns the aspect ratio: width / height
     *
     * @return aspect ratio
     */
    public double aspectRatio() {
        return width / height;
    }

    /**
     * Gets the smallest {@code Rectangle} greater or equal to the
     * current one with the specified aspect ratio
     *
     * @param    aspectRatio of the new {@code Rectangle}
     * @return a {@code Rectangle} with the specified aspect ratio
     * @throws java.lang.IllegalArgumentException if the aspect ratio isn't greater than zero
     */
    public Rectangle expandToAspectRatio(final double aspectRatio) {
        if (aspectRatio <= 0) {
            throw new IllegalArgumentException("aspectRatio must be greater than 0");
        }

        double newSide;

        // If the specified aspectRatio is greater than the current Rectangles' one
        if (aspectRatio > aspectRatio()) {
            newSide = width * aspectRatio / aspectRatio();
            return new Rectangle(center, newSide, height);
        } else {
            newSide = height * aspectRatio() / aspectRatio;
            return new Rectangle(center, width, newSide);
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("(%s, %.2f, %.2f)", center.toString(), width, height);
    }
}
