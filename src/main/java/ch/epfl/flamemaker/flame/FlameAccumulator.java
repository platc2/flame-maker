package ch.epfl.flamemaker.flame;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * A {@code FlameAccumulator} which contains the fractal
 *
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */
public final class FlameAccumulator {

    /**
     * Array containing the amount of each field it was hit
     */
    private final int[][] hitCount;

    /**
     * Array containing the sum of color indexes for every field
     */
    private final double[][] colorIndexSum;

    /**
     * The constant part of the formula to calculate the intensity
     */
    private final double intensity_formula;

    /**
     * Creates a new {@code FlameAccumulator} given a 2D-array containing the amount
     * of each field it was hit
     *
     * @param hitCount      integer-array how many times each single field was hit
     * @param colorIndexSum double-array containing the sum of color indexes for every field
     */
    private FlameAccumulator(final int[][] hitCount, final double[][] colorIndexSum) {

        this.hitCount = new int[hitCount.length][];
        this.colorIndexSum = new double[colorIndexSum.length][];

        int max = 0;
        for (int i = 0; i < hitCount.length; ++i) {
            // Makes a deep copy of both arrays
            this.hitCount[i] = hitCount[i].clone();
            this.colorIndexSum[i] = colorIndexSum[i].clone();

            // Calculates the maximum value of hitCount
            for (int j = 0; j < hitCount[i].length; ++j) {
                if (hitCount[i][j] > max) max = hitCount[i][j];
            }
        }

        // Calculates the value of intensity_formula
        intensity_formula = Math.log(max + 1);
    }

    /**
     * @return width of the {@code FlameAccumulator}
     */
    public int width() {
        return hitCount.length;
    }

    /**
     * @return height of the {@code FlameAccumulator}
     */
    public int height() {
        return hitCount[0].length;
    }

    /**
     * @param x coordinate (field)
     * @param y coordinate (field)
     * @return the intensity of the specified field given a logarithmic formula
     * @throws java.lang.IndexOutOfBoundsException if the coordinates are invalid
     */
    public double intensity(final int x, final int y) {
        if (x < 0 || x >= width()
                || y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException("Specified coordinates are invalid");
        }

        return Math.log(hitCount[x][y] + 1) / intensity_formula;
    }

    /**
     * @param palette    the palette to use
     * @param background background color
     * @param x          coordinate (field)
     * @param y          coordinate (field)
     * @return the color for the specified field
     * @throws java.lang.IndexOutOfBoundsException if the coordinates are invalid
     */
    public Color color(final Palette palette, final Color background, final int x, final int y) {
        if (x < 0 || x >= width()
                || y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException("Specified coordinates are invalid!");
        }

        // Checks whether the field was hit or not
        if (hitCount[x][y] != 0) {

            // Divides the index by the hitCount, since the index is a sum
            return palette.colorForIndex(colorIndexSum[x][y] / (double) hitCount[x][y]).
                    mixWidth(background, 1 - intensity(x, y));
        }

        // If the field was not hit, return the background
        return background;
    }

    /**
     * Builder for building a {@code FlameAccumulator}
     *
     * @author Groux Marcel Jean Jacques	227630
     * @author Platzer Casimir Benjamin		228352
     * @version 1.0
     */
    public static final class Builder {

        /**
         * Array of the grid containing the amount a field was hit
         */
        private final int[][] hitCount;

        /**
         * Array of the grid containing the colors for each field
         */
        private final double[][] colorIndexSum;

        /**
         * {@code Rectangle} limiting the area
         */
        private final Rectangle frame;

        /**
         * {@code FlameTransformation} to transform Points in the method {@link #hit(Point, double)}
         * so they are contained in the specified array {@link #hitCount}
         */
        private final AffineTransformation transformation;

        /**
         * Creates a new builder for building a flame accumulator
         *
         * @param frame  limiting the area
         * @param width  of the accumulator
         * @param height of the accumulator
         * @throws java.lang.IllegalArgumentException if the width or the height are not greater than zero
         */
        public Builder(final Rectangle frame, final int width, final int height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Width and height must be greater than zero");
            }

            this.frame = frame;
            hitCount = new int[width][height];
            colorIndexSum = new double[width][height];

            final double sX = (double) width / frame.width();
            final double sY = (double) height / frame.height();
            final double dX = -frame.left();
            final double dY = -frame.bottom();

            /*
             * Since the point is contained in a Rectangle which lower-left point
             * is not necessarily in the origin, we have to create a translation,
             * which moves the point there. Also since the size of the Rectangle is
             * not necessarily the same as our array, which represents a grid.
             * That is why we also have to create a scaling and either increase
             * or decrease the point's coordinates.
             */
            transformation = AffineTransformation.newTranslation(dX, dY)
                    .composeWith(AffineTransformation.newScaling(sX, sY));
        }

        /**
         * Hits the frame containing the specified {@code Point}:
         * <ul>
         *  <li>Increments the value containing the amount of hits
         * 	<li>Sets the {@code Color} for the field
         * </ul>
         *
         * @param p          {@code Point}
         * @param colorIndex of the {@code Point}
         */
        public void hit(Point p, final double colorIndex) {

            // Only calculate this if the unmodified frame contains the unmodified Point
            // By unmodified we mean: Not transformed by the affine transformation yet
            if (frame.contains(p)) {
                p = transformation.transformPoint(p);

                // Since the coordinates should be positive, there's no need of Math.floor()
                final int x = (int) p.x();
                final int y = (int) p.y();

                // Because of rounding, numbers still might get invalid
                if (x >= 0 && x < hitCount.length && y >= 0 && y < hitCount[0].length) {
                    hitCount[x][y] += 1;
                    colorIndexSum[x][y] += colorIndex;
                }
            }
        }

        /**
         * Builds the {@code FlameAccumulator} and returns it
         *
         * @return the built {@code FlameAccumulator}
         */
        public FlameAccumulator build() {
            return new FlameAccumulator(hitCount, colorIndexSum);
        }
    }
}
