package ch.epfl.flamemaker.color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An {@code InterpolatedPalette} with colors randomly filled in 
 *
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */
public class RandomPalette implements Palette {

	/** Interpolated {@code Palette} to save the randomly filled in colors */
	private final InterpolatedPalette palette;

	/**
	 * Creates a new {@code RandomPalette} given the amount of colors. 
	 * The colors are filled in randomly in an {@code InterpolatedPalette}
	 * @param amount of colors
	 * @throws java.lang.IllegalArgumentException if the amount is not greater or equal than two
	 */
	public RandomPalette(int amount) {
		if (amount < 2)
			throw new IllegalArgumentException("Palette must at least contain two colors");

		final List<Color> colors = new ArrayList<Color>();
		final Random r = new Random();

		for (int i = 0; i < amount; ++i)
			colors.add(new Color(r.nextDouble(), r.nextDouble(), r.nextDouble()));

		palette = new InterpolatedPalette(colors);
	}

	/** @see InterpolatedPalette */
	@Override
	public Color colorForIndex(double index) {
		return palette.colorForIndex(index);
	}
}
