package ch.epfl.flamemaker.geometry2d;


/**
 * Affine transformation in a 2D-system
 *
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */
public final class AffineTransformation implements Transformation {

    /**
     * Identity matrix:
     * <pre>
     *  [	1	0	0	]
     *  [	0	1	0	]
     *  [	0	0	1	]
     * </pre>
     */
    public final static AffineTransformation IDENTITY = new AffineTransformation(
            1, 0, 0,
            0, 1, 0);

    /**
     *
     */
    private final double a;

    /**
     *
     */
    private final double b;

    /**
     *
     */
    private final double c;

    /**
     *
     */
    private final double d;

    /**
     *
     */
    private final double e;

    /**
     *
     */
    private final double f;

    /**
     * Creates an new affine transformation represented by the following matrix:
     * <pre>
     *  	[	a	b	c	]
     *  	[	d	e	f	]
     *  	[	0	0	1	]
     * </pre>
     *
     * @param a
     * @param b
     * @param c
     * @param d
     * @param e
     * @param f
     */
    public AffineTransformation(final double a, final double b,
                                final double c, final double d,
                                final double e, final double f) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
    }

    /**
     * Creates a translation represented by the following matrix:
     * <pre>
     *  	[	1	0	x	]
     *  	[	0	1	y	]
     *  	[	0	0	1	]
     * </pre>
     *
     * @param dX offset on the x-axis
     * @param dY offset on the y-axis
     * @return affine transformation representing a translation
     */
    public static AffineTransformation newTranslation(final double dX, final double dY) {
        return new AffineTransformation(
                1, 0, dX,
                0, 1, dY);
    }

    /**
     * Creates a rotation represented by the following matrix:
     * <pre>
     *  	[	cos(&#952)	-sin(&#952)	0	]
     *  	[	sin(&#952)	cos(&#952)	0	]
     *  	[	0	0	1	]
     * </pre>
     *
     * @param theta angle in radian
     * @return affine transformation representing a rotation
     */
    public static AffineTransformation newRotation(final double theta) {
        return new AffineTransformation(
                Math.cos(theta), -Math.sin(theta), 0,
                Math.sin(theta), Math.cos(theta), 0);
    }

    /**
     * Creates a scaling represented by the following matrix:
     * <pre>
     *  	[	s<sub>x</sub>	0	0	]
     *  	[	0	s<sub>y</sub>	0	]
     *  	[	0	0	1	]
     * </pre>
     *
     * @param sX amount to scale on the x-axis
     * @param sY amount to scale on the y-axis
     * @return affine transformation representing a scaling
     */
    public static AffineTransformation newScaling(final double sX, final double sY) {
        return new AffineTransformation(
                sX, 0, 0,
                0, sY, 0);
    }

    /**
     * Creates a shear on the x-axis represented by the following matrix:
     * <pre>
     *  	[	1	s<sub>x</sub>	0	]
     *  	[	0	1	0	]
     *  	[	0	0	1	]
     * </pre>
     *
     * @param sX amount to shear on the x-axis
     * @return affine transformation representing a shear on the x-axis
     */
    public static AffineTransformation newShearX(final double sX) {
        return new AffineTransformation(
                1, sX, 0,
                0, 1, 0);
    }

    /**
     * Creates a shear on the y-axis represented by the following matrix:
     * <pre>
     *  	[	1	s<sub>y</sub>	0	]
     *  	[	0	1	0	]
     *  	[	0	0	1	]
     * </pre>
     *
     * @param sY amount to shear on the y-axis
     * @return affine transformation representing a shear on the y-axis
     */
    public static AffineTransformation newShearY(final double sY) {
        return new AffineTransformation(
                1, 0, 0,
                sY, 1, 0);
    }

    /**
     * Modifies the coordinates of the {@code Point} the following way:
     * <pre>
     *  	<i>x = ax + by + c
     *  	y = dy + ex + f</i>
     * </pre>
     *
     * @param p {@code Point} to transform
     * @return transformed {@code Point}
     */
    public Point transformPoint(final Point p) {
        return new Point(
                a * p.x() + b * p.y() + c,
                d * p.x() + e * p.y() + f);
    }

    /**
     * @return amount of translation on x-axis
     */
    public double translationX() {
        return c;
    }

    /**
     * @return amount of translation on y-axis
     */
    public double translationY() {
        return f;
    }

    /**
     * Creates a composition of two affine transformations.
     * In other words: Simple matrix multiplication
     *
     * @param that affine transformation to compose with
     * @return The composition of the both affine transformations
     */
    public AffineTransformation composeWith(final AffineTransformation that) {

        /* Solution for solving a 3x3 (Here: 3x2) matrix multiplication */
        return new AffineTransformation(
                that.a * a + that.b * d,
                that.a * b + that.b * e,
                that.a * c + that.b * f + that.c,
                that.d * a + that.e * d,
                that.d * b + that.e * e,
                that.d * c + that.e * f + that.f);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format(
                "AffineTransformation(%.2f %.2f %.2f \n%.2f %.2f %.2f)",
                a, b, c, d, e, f);
    }
}
