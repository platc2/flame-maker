package ch.epfl.flamemaker.flame;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * A class representing a flame fractal
 *
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */
public final class Flame {

    /**
     * Static field containing the turbulence fractal
     */
    public static final Flame TURBULENCE = new Flame(Arrays.asList(
            new FlameTransformation(new AffineTransformation(
                    0.7124807, -0.4113509, -0.3,
                    0.4113513, 0.7124808, -0.7), new double[]{0.5, 0, 0, 0.4, 0, 0}),
            new FlameTransformation(new AffineTransformation(
                    0.3731079, -0.6462417, 0.4,
                    0.6462414, 0.3731076, 0.3), new double[]{1.0, 0, 0.1, 0, 0, 0}),
            new FlameTransformation(new AffineTransformation(
                    0.0842641, -0.314478, -0.1,
                    0.314478, 0.0842641, 0.3), new double[]{1.0, 0, 0, 0, 0, 0})));

    /**
     * Static field containing the sharkfin fractal
     */
    public static final Flame SHARKFIN = new Flame(Arrays.asList(
            new FlameTransformation(new AffineTransformation(
                    -0.4113504, -0.7124804, -0.4,
                    0.7124795, -0.4113508, 0.8), new double[]{1.0, 0.1, 0, 0, 0, 0}),
            new FlameTransformation(new AffineTransformation(
                    -0.3957339, 0, -1.6,
                    0, -0.3957337, 0.2), new double[]{0, 0, 0, 0, 0.8, 1.0}),
            new FlameTransformation(new AffineTransformation(
                    0.4810169, 0, 1,
                    0, 0.4810169, 0.9), new double[]{1.0, 0, 0, 0, 0, 0})));

    /**
     * Static field containing the sierpinski triangle
     */
    public static final Flame SIERPINSKI_TRIANGLE = new Flame(Arrays.asList(
            new FlameTransformation(new AffineTransformation(
                    0.5, 0.0, 0.0,
                    0.0, 0.5, 0.0), new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0}),
            new FlameTransformation(new AffineTransformation(
                    0.5, 0.0, 1.0,
                    0.0, 0.5, 0.0), new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0}),
            new FlameTransformation(new AffineTransformation(
                    0.5, 0.0, 0.0,
                    0.0, 0.5, 1.0), new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0})));

    /**
     * Static field containing the sierpinski carpet
     */
    public static final Flame SIERPINSKI_CARPET = new Flame(Arrays.asList(
            new FlameTransformation(new AffineTransformation(
                    1.0 / 3.0, 0.0, 0.0,
                    0.0, 1.0 / 3.0, 0.0), new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0}),
            new FlameTransformation(new AffineTransformation(
                    1.0 / 3.0, 0.0, 1.0 / 3.0,
                    0.0, 1.0 / 3.0, 0.0), new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0}),
            new FlameTransformation(new AffineTransformation(
                    1.0 / 3.0, 0.0, 2.0 / 3.0,
                    0.0, 1.0 / 3.0, 0.0), new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0}),
            new FlameTransformation(new AffineTransformation(
                    1.0 / 3.0, 0.0, 0.0,
                    0.0, 1.0 / 3.0, 1.0 / 3.0), new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0}),
            new FlameTransformation(new AffineTransformation(
                    1.0 / 3.0, 0.0, 2.0 / 3.0,
                    0.0, 1.0 / 3.0, 1.0 / 3.0), new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0}),
            new FlameTransformation(new AffineTransformation(
                    1.0 / 3.0, 0.0, 0.0,
                    0.0, 1.0 / 3.0, 2.0 / 3.0), new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0}),
            new FlameTransformation(new AffineTransformation(
                    1.0 / 3.0, 0.0, 1.0 / 3.0,
                    0.0, 1.0 / 3.0, 2.0 / 3.0), new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0}),
            new FlameTransformation(new AffineTransformation(
                    1.0 / 3.0, 0.0, 2.0 / 3.0,
                    0.0, 1.0 / 3.0, 2.0 / 3.0), new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0})));

    /**
     * Static field containing the barsnely farn
     */
    public static final Flame BARNSLEY_FARN = new Flame(Arrays.asList(
            new FlameTransformation(new AffineTransformation(
                    0.0, 0.0, 0.0,
                    0.0, 0.16, 0.0), new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0}),
            new FlameTransformation(new AffineTransformation(
                    0.85, 0.04, 0.0,
                    -0.04, 0.85, 1.6), new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0}),
            new FlameTransformation(new AffineTransformation(
                    0.2, -0.26, 0.0,
                    0.23, 0.22, 1.6), new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0}),
            new FlameTransformation(new AffineTransformation(
                    -0.15, 0.28, 0.0,
                    0.26, 0.24, 0.44), new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0})));

    /**
     * Static field containing the dragon curve
     */
    public static final Flame DRAGON_CURVE = new Flame(Arrays.asList(
            new FlameTransformation(new AffineTransformation(
                    Math.cos(Math.toRadians(45)) / Math.sqrt(2), -Math.sin(Math.toRadians(45)) / Math.sqrt(2), 0.0,
                    Math.sin(Math.toRadians(45)) / Math.sqrt(2), Math.cos(Math.toRadians(45)) / Math.sqrt(2), 0.0), new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0}),
            new FlameTransformation(new AffineTransformation(
                    Math.cos(Math.toRadians(135)) / Math.sqrt(2), -Math.sin(Math.toRadians(135)) / Math.sqrt(2), 1.0,
                    Math.sin(Math.toRadians(135)) / Math.sqrt(2), Math.cos(Math.toRadians(135)) / Math.sqrt(2), 0.0), new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0})));

    /**
     * A list containing all the flame transformations for this fractal
     */
    private final List<FlameTransformation> transformations;

    /**
     * Creates a new flame fractal
     *
     * @param transformations list of flame transformations of the fractal
     */
    public Flame(final List<FlameTransformation> transformations) {
        this.transformations = new ArrayList<>();

        this.transformations.addAll(transformations);
    }

    /**
     * Computes the fractal in the region limited by the frame
     * using the chaos algorithm and saves it in an {@link FlameAccumulator}
     *
     * @param frame   limiting the area
     * @param width   of the accumulator
     * @param height  of the {@code FlameAccumulator}
     * @param density : iterations per field
     * @return {@code FlameAccumulator} with specified width and height containing the specified fractal
     * @see FlameAccumulator
     */
    public FlameAccumulator compute(final Rectangle frame, final int width, final int height, final int density) {
        // Fields for points, colors and random values with their starting values
        Point p = Point.ORIGIN;
        double colorIndex = 0;
        int rand;

        final int size = transformations.size();

        // Random number generator for choosing the transformation
        final Random r = new Random();

        // Number of iterations to perform
        final int m = density * height * width;

        // Contains the values for some quite slow method
        final double[] indexes = new double[size];

        // Builder to create the flame accumulator
        final FlameAccumulator.Builder builder = new FlameAccumulator.Builder(frame, width, height);

        // Returns the empty builder if there aren't any transformations
        if (size == 0) {
            return builder.build();
        }

        for (int i = 0; i < size; ++i) {
            // Computes the values and saves them for not computing the same values over and over
            indexes[i] = Color.colorIndex(i);
        }

        // First twenty iterations to make sure first point hitting is not necessarily in the center
        for (int i = 0; i < 20; ++i) {
            rand = r.nextInt(size);

            // Transforms the point with the flame transformation
            p = transformations.get(rand).transformPoint(p);

            // Calculates the color for this point using the last color and the color index for this transformation
            colorIndex = (colorIndex + indexes[rand]) * 0.5;
        }

        for (int i = 0; i < m; ++i) {
            rand = r.nextInt(size);

            p = transformations.get(rand).transformPoint(p);

            colorIndex = (colorIndex + indexes[rand]) * 0.5;

            // Hits the point on the accumulator
            builder.hit(p, colorIndex);
        }

        // Builds the flame accumulator and returns it
        return builder.build();
    }

    /**
     * Computes the fractal until the given amount of {@code points} were computer
     *
     * @param amount  of {@code Points} to calculate
     * @param builder {@code FlameAccumulator.Builder} if existent to continue calculating
     * @return
     */
    public FlameAccumulator.Builder compute(final int amount, final FlameAccumulator.Builder builder) {
        // Fields for points, colors and random values with their starting values
        Point p = Point.ORIGIN;
        double colorIndex = 0;
        int rand;

        final int size = transformations.size();

        // Random number generator for choosing the transformation
        final Random r = new Random();

        // Contains the values for some quite slow method
        final double[] indexes = new double[size];

        // Returns the empty builder if there aren't any transformations
        if (size == 0) {
            return builder;
        }

        for (int i = 0; i < size; ++i) {
            // Computes the values and saves them for not computing the same values over and over
            indexes[i] = Color.colorIndex(i);
        }

        // First twenty iterations to make sure first point hitting is not necessarily in the center
        for (int i = 0; i < 20; ++i) {
            rand = r.nextInt(size);

            // Transforms the point with the flame transformation
            p = transformations.get(rand).transformPoint(p);

            // Calculates the color for this point using the last color and the color index for this transformation
            colorIndex = (colorIndex + indexes[rand]) * 0.5;
        }

        for (int i = 0; i < amount; ++i) {
            rand = r.nextInt(size);

            p = transformations.get(rand).transformPoint(p);

            colorIndex = (colorIndex + indexes[rand]) * 0.5;

            // Hits the point on the accumulator
            builder.hit(p, colorIndex);
        }

        // Builds the flame accumulator and returns it
        return builder;
    }

    /**
     * A builder for {@code Flame}
     *
     * @author Groux Marcel Jean Jacques	227630
     * @author Platzer Casimir Benjamin		228352
     * @version 1.0
     */
    public static final class Builder {

        /**
         * List of {@code FlameTransformation.Builder} for the {@code FlameTransformtation}
         */
        private final List<FlameTransformation.Builder> builders = new ArrayList<>();

        /**
         * Creates a new {@code Flame.Builder} given a {@code Flame} and so it's {@code FlameTransformations}
         *
         * @param flame for the {@code FlameTransformations}
         */
        public Builder(final Flame flame) {
            flame.transformations.forEach(this::addTransformation);
        }

        /**
         * @return the amount of {@code FlameTransformations}
         */
        public int transformationsCount() {
            return builders.size();
        }

        /**
         * Adds a new {@code FlameTransformation} to the {@code Flame}
         *
         * @param transformation to add
         */
        public void addTransformation(final FlameTransformation transformation) {
            this.builders.add(new FlameTransformation.Builder(
                    transformation));
        }

        /**
         * @param index of the {@code FlameTransformation}
         * @return the {@code AffineTransformation} of the {@code FlameTransformation}
         */
        public AffineTransformation affineTransformation(final int index) {
            checkIndex(index);
            return builders.get(index).affineTransformation();
        }

        /**
         * Sets the {@code AffineTransformation} of the {@code FlameTransformation}
         *
         * @param index          of the {@code FlameTransformation}
         * @param transformation to set
         */
        public void setAffineTransformation(final int index, final AffineTransformation transformation) {
            checkIndex(index);
            builders.get(index).setAffineTransformation(transformation);
        }

        /**
         * @param index     of the {@code FlameTransformation}
         * @param variation to get the weight from
         * @return the weight of the specified {@code Variation}
         */
        public double variationWeight(final int index, final Variation variation) {
            checkIndex(index);
            return builders.get(index).variationWeight(variation);
        }

        /**
         * Sets the weight of the specified {@code Variation} in the specified {@code FlameTransformation}
         *
         * @param index     of the {@code FlameTransformation}
         * @param variation to modify
         * @param weight    of the {@code Variation}
         */
        public void setVariationWeight(final int index, final Variation variation, final double weight) {
            checkIndex(index);
            builders.get(index).setVariationWeight(variation, weight);
        }

        /**
         * Removes the {@code FlameTransformation} at the specified position from the list
         *
         * @param index of the {@code FlameTransformation}
         */
        public void removeTransformation(final int index) {
            checkIndex(index);
            builders.remove(index);
        }

        /**
         * Checks whether the index is invalid or not (Throws an exception if invalid)
         *
         * @param index of the {@code FlameTransformation}
         * @throws java.lang.IllegalArgumentException if the index is invalid
         */
        private void checkIndex(final int index) {
            if (index < 0 || index > transformationsCount()) {
                throw new IllegalArgumentException("Invalid index!");
            }
        }

        /**
         * Builds the {@code Flame} and returns it. But first it is necessary to build all the
         * {@code FlameTransformations} and add them to the {@code Flame}
         *
         * @return the built {@code Flame}
         */
        public Flame build() {
            // Builds the transformations into a list
            final List<FlameTransformation> transformations = builders
                    .stream()
                    .map(FlameTransformation.Builder::build)
                    .collect(Collectors.toList());

            return new Flame(transformations);
        }
    }
}
