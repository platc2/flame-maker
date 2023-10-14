package ch.epfl.flamemaker.color;

/**
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */
public interface Palette {
	
	/**
	 * @param index of the {@code Color} 
	 * @return the {@code Color} for the specified index
	 */
	Color colorForIndex(double index);
}
