package ch.epfl.flamemaker.color;

/**
 * Class representing a color
 *
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */
public final class Color {

    /**
     * Static color BLACK
     */
    public final static Color BLACK = new Color(0.0, 0.0, 0.0);

    /**
     * Static color WHITE
     */
    public final static Color WHITE = new Color(1.0, 1.0, 1.0);

    /**
     * Static color RED
     */
    public final static Color RED = new Color(1.0, 0.0, 0.0);

    /**
     * Static color GREEN
     */
    public final static Color GREEN = new Color(0.0, 1.0, 0.0);

    /**
     * Static color BLUE
     */
    public final static Color BLUE = new Color(0.0, 0.0, 1.0);

    /**
     * The red-value of the {@code Color}
     */
    private final double r;

    /**
     * The green-value of the {@code Color}
     */
    private final double g;

    /**
     * The blue-value of the {@code Color}
     */
    private final double b;

    /**
     * Creates a new {@code Color} using RGB
     *
     * @param r red-value
     * @param g green-value
     * @param b blue-value
     * @throws java.lang.IllegalArgumentException if the values are not greater or equal zero and smaller or equal than 1.0
     */
    public Color(final double r, final double g, final double b) {
        if (r < 0.0 || r > 1.0
                || g < 0.0 || g > 1.0
                || b < 0.0 || b > 1.0) {
            throw new IllegalArgumentException("Arguments must be greater or equal than zero and smaller or equal than 1.0");
        }

        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * @return red-value of the {@code Color}
     */
    public double red() {
        return r;
    }

    /**
     * @return green-value of the {@code Color}
     */
    public double green() {
        return g;
    }

    /**
     * @return blue-value of the {@code Color}
     */
    public double blue() {
        return b;
    }

    /**
     * Mixes a {@code Color} with another given the proportion
     * <pre>
     *  Color<sub>mixed</sub> = Color<sub>this</sub> * proportion + Color<sub>other</sub> * (1 - proportion)
     * </pre>
     *
     * @param color      other {@code Color} to mix with
     * @param proportion of the first {@code Color}
     * @return the {@code Color} mixed with an other {@code Color} using specified proportion
     * @throws java.lang.IllegalArgumentException if the proportion is not greater or equal than zero and smaller or equal than 1.0
     */
    public Color mixWidth(final Color color, final double proportion) {
        if (proportion < 0.0 || proportion > 1.0) {
            throw new IllegalArgumentException("Proportion must be greater or equal than zero and smaller or equal than 1.0");
        }

        return new Color(
                color.red() * proportion + (1.0 - proportion) * red(),
                color.green() * proportion + (1.0 - proportion) * green(),
                color.blue() * proportion + (1.0 - proportion) * blue());
    }

    /**
     * Returns the {@code Color} as an integer using the hexadecimal form:
     * 0xRRGGBB (red, green, blue)
     *
     * @return the {@code Color}, as 24bit-integer
     */

    public int asPackedRGB() {
        // The color is 24bits, where the first 8 bits are for red, the next for green and the last ones for blue
        return (sRGBEncode(red(), 255) << 16)
                + (sRGBEncode(green(), 255) << 8)
                + sRGBEncode(blue(), 255);
    }

    /**
     * Encodes a component of the {@code Color} to make it brighter
     *
     * @param v   component of the {@code Color} (RGB: red / green / blue)
     * @param max the range in which the component shall be later
     * @return the encoded component in the range of 0 and max
     */
    public static int sRGBEncode(final double v, final int max) {
        return (int) (max * ((v <= 0.0031308) ? (12.92 * v)
                : (1.055 * Math.pow(v, 1.0 / 2.4) - 0.055)));
    }

    /**
     * @param n the transformation to return the color index for
     * @return the color index for the n-th transformation
     */
    public static double colorIndex(final int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return -1 + Math.pow(2, (-Math.ceil(Math.log(n) / Math.log(2)))) * (-1 + 2 * n);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Color(%.2f, %.2f, %.2f)", red(), green(), blue());
    }
}
