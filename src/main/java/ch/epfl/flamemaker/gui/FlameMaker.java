package ch.epfl.flamemaker.gui;

import javax.swing.*;

/**
 * Launches the GUI for the {@code FlameMaker} software
 *
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */
public final class FlameMaker {
    public static void main(final String[] arguments) {
        SwingUtilities.invokeLater(() -> new FlameMakerGUI().start());
    }
}
