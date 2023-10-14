package ch.epfl.flamemaker.ifs;

import java.util.Arrays;

/**
 * An IFSAccumulator which contains the fractal 
 * 
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */
public final class IFSAccumulator {
	
	/** Array of the grid containing whether a field is hit or not */
	private final boolean[][] isHit;
	
	/**
	 * Creates a new IFSAccumulator given a 2D-array containing 
	 * whether the fields where hit or not
	 * 
	 * @param isHit boolean-array whether fields are hit or not
	 */
	public IFSAccumulator(boolean[][] isHit) {
		
		// Deep copy of array
		int length = isHit.length;
		
		this.isHit	= new boolean[length][];
		
		// Since isHit is an array of arrays, it is necessary to copy all these "sub-arrays" 
		for (int i = 0; i < length; ++i) 
			this.isHit[i]	= Arrays.copyOf(isHit[i], isHit[i].length);
	}
	
	/** @return width of the accumulator */
	public int width() {
		return isHit.length;
	}
	
	/** @return height of the accumulator */
	public int height() {
		return isHit[0].length;
	}
	
	/**
	 * @param x coordinate of field
	 * @param y coordinate of field
	 * @return whether the field is hit or not
	 * @throws java.lang.IndexOutOfBoundsException if the specified coordinates are invalid
	 */
	public boolean isHit(int x, int y) {
		if (x < 0
				|| x >= width()
				|| y < 0
				|| y >= height())
			throw new IndexOutOfBoundsException("Specified coordinates are invalid");
		
		return isHit[x][y];
	}
}
