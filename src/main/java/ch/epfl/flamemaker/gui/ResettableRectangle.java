package ch.epfl.flamemaker.gui;

import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * Interface for components which can modify their rectangle
 *
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */
public interface ResettableRectangle {
    /**
     * @param rectangle to replace
     */
    void setRectangle(final Rectangle rectangle);
}
