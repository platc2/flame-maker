package ch.epfl.flamemaker.gui;

import javax.swing.SwingUtilities;

/**
 * Launches the GUI for the {@code FlameMaker} software
 *
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */
public class FlameMaker {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new FlameMakerGUI().start();
			}
		});
	}
}
