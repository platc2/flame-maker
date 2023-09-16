package ch.epfl.flamemaker.color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An {@code InterpolatedPalette} 
 * 
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */
public class InterpolatedPalette implements Palette {
	
	/** A list containing all the colors of the {@code Palette} */
	private final List<Color> colorPalette;
	
	/**
	 * Creates a new {@code InterpolatedPalette} given a list of colors 
	 * 
	 * @param colorPalette a list of colors 
	 * @throws java.lang.IllegalArgumentException if the list does not contain at least 2 colors
	 */
	public InterpolatedPalette(List<Color> colorPalette) {
		if (colorPalette.size() < 2) 
			throw new IllegalArgumentException("Palette must at least contain two colors");
		
		this.colorPalette = new ArrayList<Color>();
		
		for (Color c : colorPalette) 
			this.colorPalette.add(c);
	}
	
	/**
	 * Constructor using {@code varargs} (Java 1.5)
	 * 
	 * @param colors Array of colors to add to the list
	 */
	public InterpolatedPalette(Color ... colors) {
		this(Arrays.asList(colors));
	}
	
	/** @throws java.lang.IllegalArgumentException if the index is not greater or equal than zero and less or equal than 1.0 */
	@Override
	public Color colorForIndex(double index) {
		if (index < 0.0 || index > 1.0) 
			throw new IllegalArgumentException("Index must be greater or equal than zero and lessor equal than 1.0");
		
		index *= (colorPalette.size() - 1);
		
		return colorPalette.get((int) Math.floor(index)).mixWidth(
				colorPalette.get((int) Math.ceil(index)), index % 1);
	}
}
