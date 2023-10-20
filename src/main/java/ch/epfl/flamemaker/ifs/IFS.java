package ch.epfl.flamemaker.ifs;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * A class representing an iterated function system
 *
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */
public final class IFS {

    /**
     * Affine transformations for sierpinskys triangle
     */
    public static final IFS SIERPINSKY_TRIANGLE = new IFS(Arrays.asList(
            new AffineTransformation(
                    0.5, 0, 0,
                    0, 0.5, 0),
            new AffineTransformation(
                    0.5, 0, 0.5,
                    0, 0.5, 0),
            new AffineTransformation(
                    0.5, 0, 0.25,
                    0, 0.5, 0.5)));

    /**
     * Affine transformations for barnsley's fern
     */
    public static final IFS BARNSLEYS_FERN = new IFS(Arrays.asList(
            new AffineTransformation(
                    0, 0, 0,
                    0, 0.16, 0),
            new AffineTransformation(
                    0.2, -0.26, 0,
                    0.23, 0.22, 1.6),
            new AffineTransformation(
                    -0.15, 0.28, 0,
                    0.26, 0.24, 0.44),
            new AffineTransformation(
                    0.85, 0.04, 0,
                    -0.04, 0.85, 1.6)));

    /**
     * A list containing all the affine transformations
     */
    private final List<AffineTransformation> transformations;

    /**
     * Creates a new IFS taking a list of affine transformations
     * as parameter
     *
     * @param transformations List of transformations
     */
    public IFS(final List<AffineTransformation> transformations) {
        this.transformations = new ArrayList<>();

        this.transformations.addAll(transformations);
    }

    /**
     * Computes the fractal in the region limited by the frame
     * using the chaos algorithm and saves it in an {@link IFSAccumulator}
     *
     * @param frame   limiting the area
     * @param width   of the accumulator
     * @param height  of the accumulator
     * @param density : iterations per field
     * @return IFSAccumulator with specified width and height containing the specified fractal
     * @see IFSAccumulator
     */
    public IFSAccumulator compute(final Rectangle frame, final int width, final int height, final int density) {

        Point p = Point.ORIGIN;
        final Random r = new Random();

        // Builder to create the IFS accumulator
        final IFSAccumulatorBuilder builder = new IFSAccumulatorBuilder(frame, width, height);

        // Number of iterations to perform
        final int m = density * height * width;

        // First twenty iterations to make sure first point hitting is not necessarily in the center
        for (int i = 0; i < 20; ++i)
            // Transforms the point with the affine transformation
            p = transformations.get(r.nextInt(transformations.size())).transformPoint(p);

        for (int i = 0; i < m; ++i) {
            p = transformations.get(r.nextInt(transformations.size())).transformPoint(p);

            // Hits the point on the accumulator
            builder.hit(p);
        }

        // Builds the IFS accumulator and returns it
        return builder.build();
    }
}
