package ch.epfl.flamemaker.gui;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

/**
 * A component displaying the {@code AffineTransformations} using
 * arrows in a 2D coordinate system
 *
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */
public final class AffineTransformationsComponent extends JComponent implements ResettableRectangle {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * {@code ObservableFlameBuilder} for computing the flame
     */
    private final ObservableFlameBuilder builder;

    /**
     * {@code Rectangle} limiting the area
     */
    private Rectangle rectangle;

    /**
     * The currently selected {@coed Transformation}
     */
    private int highlightedTransformationIndex = 0;

    /**
     * Creates an new {@code AffineTransformationsComponent} for displaying the {@code AffineTransformations}
     *
     * @param builder   for computing the flame
     * @param rectangle limiting the area
     */
    public AffineTransformationsComponent(final ObservableFlameBuilder builder, final Rectangle rectangle) {
        this.builder = builder;
        this.rectangle = rectangle;

        // Repaints the component when the builder was changed
        builder.addObserver(x -> repaint());
    }

    /**
     * @param highlightedTransformationIndex sets the currently selected {@code Transformation}
     */
    public void setHighlightedTransformationIndex(final int highlightedTransformationIndex) {
        this.highlightedTransformationIndex = highlightedTransformationIndex;
        repaint();
    }

    /**
     * @return the index of the currently selected {@code Transformation}
     */
    public int highlightedTransformationIndex() {
        return highlightedTransformationIndex;
    }

    @Override
    public void setRectangle(final Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 150);
    }

    @Override
    public void paintComponent(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;

        // Adds antialiasing to the component
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Width and height of the component
        final double width = getWidth();
        final double height = getHeight();

        // Expands the rectangle to the aspect ratio of the component
        final Rectangle tmp = rectangle.expandToAspectRatio(width / height);

        // Amount of transformations
        final int size = builder.transformationsCount();

        // AffineTransformation for transforming the coordinates to the components' coordinate system
        final AffineTransformation convert = AffineTransformation.newTranslation(-tmp.left(), -tmp.bottom()).composeWith(
                AffineTransformation.newScaling(width / tmp.width(), height / tmp.height())).composeWith(
                AffineTransformation.newTranslation(0.0, -height / 2.0)).composeWith(
                AffineTransformation.newScaling(1.0, -1.0)).composeWith(
                AffineTransformation.newTranslation(0.0, height / 2.0));

        // Draws the grid of the component
        drawGrid(g2d, convert, tmp);

        // Draws the transformations. Draws the selected one in red at last
        g2d.setColor(Color.BLACK); // java.awt.Color

        // Index just after the currently selected one
        int index = (highlightedTransformationIndex + 1) % size;
        for (int i = 0; i < size - 1; ++i) {
            // Draws the transformations
            drawTransformation(g2d, convert, builder.affineTransformation(index++ % size));
        }

        g2d.setColor(Color.RED); // java.awt.Color
        // Draws the currently selected transformation in red
        drawTransformation(g2d, convert, builder.affineTransformation(highlightedTransformationIndex));
    }

    /**
     * Draws the grid of the coordinate system
     *
     * @param g2d     {@code Graphics} object for drawing
     * @param convert {@code AffineTransformation} for converting the coordinates to the system of the {@code Graphics}
     * @param tmp     {@code Rectangle} to draw the grid of
     */
    private void drawGrid(final Graphics2D g2d, final AffineTransformation convert, final Rectangle tmp) {
        // Draws the horizontal lines
        for (int d = (int) tmp.bottom(); d <= (int) tmp.top(); ++d) {
            // Chooses the color depending on whether it's the middle line or not
            g2d.setColor((d == 0) ? Color.WHITE : new Color(0.9f, 0.9f, 0.9f));
            drawLine(g2d, convert, new Point(tmp.left(), d), new Point(tmp.right(), d));
        }

        // Draws the vertical lines
        for (int d = (int) tmp.left(); d <= (int) tmp.right(); ++d) {
            // Chooses the color depending on whether it's the middle line or not
            g2d.setColor((d == 0) ? Color.WHITE : new Color(0.9f, 0.9f, 0.9f));
            drawLine(g2d, convert, new Point(d, tmp.top()), new Point(d, tmp.bottom()));
        }
    }

    /**
     * Draws the grid of the coordinate system
     *
     * @param g2d            {@code Graphics} object for drawing
     * @param convert        {@code AffineTransformation} for converting the coordinates to the system of the {@code Graphics}
     * @param transformation {@code Transformation} to display
     */
    private void drawTransformation(final Graphics2D g2d, final AffineTransformation convert, AffineTransformation transformation) {
        // One point for every ending of a Point (Head = Ending of 3 lines)
        final Point tail = new Point(-1, 0);
        final Point head = new Point(1, 0);
        final Point up = new Point(head.x() - 0.1, head.y() + 0.1);
        final Point down = new Point(head.x() - 0.1, head.y() - 0.1);

        // Draws the first arrow, rotates it by 90 degrees and draws the second (x, y)
        for (int i = 0; i < 2; ++i) {
            // The current points are transformed using the transformation
            final Point tmpTail = transformation.transformPoint(tail);
            final Point tmpHead = transformation.transformPoint(head);
            final Point tmpUp = transformation.transformPoint(up);
            final Point tmpDown = transformation.transformPoint(down);

            // Draws the 3 lines an arrow consists of
            drawLine(g2d, convert, tmpHead, tmpTail);
            drawLine(g2d, convert, tmpHead, tmpUp);
            drawLine(g2d, convert, tmpHead, tmpDown);

            final double dX = transformation.translationX(), dY = transformation.translationY();
            transformation = transformation.composeWith(AffineTransformation.newTranslation(-dX, -dY)).composeWith(
                    AffineTransformation.newRotation(Math.toRadians(90))).composeWith(
                    AffineTransformation.newTranslation(dX, dY));
        }
    }

    /**
     * Draws a line using the specified {@code Graphics}
     *
     * @param g2d     {@code Graphics} object for drawing
     * @param convert {@code AffineTransformation} for converting the coordinates to the system of the {@code Graphics}
     * @param p1      first {@code Point} of the line
     * @param p2      second {@code Point} of the line
     */
    private void drawLine(final Graphics2D g2d, final AffineTransformation convert, final Point p1, final Point p2) {
        // The points are converted to the coordinate system of the graphics object
        final Point p1Transformed = convert.transformPoint(p1);
        final Point p2Transformed = convert.transformPoint(p2);

        g2d.draw(new Line2D.Double(
                p1Transformed.x(), p1Transformed.y(),
                p2Transformed.x(), p2Transformed.y()));
    }
}
