package ch.epfl.flamemaker.geometry2d;


/**
 * A class representing a transformation
 *
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */

public interface Transformation {
    /**
     * Transforms a {@code Point}.
     *
     * @param p {@code Point} to transform
     * @return transformed {@code Point}
     */
    Point transformPoint(final Point p);
}
