package ch.epfl.flamemaker.ifs;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * Builder for building the IFS accumulator
 * 
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */
public class IFSAccumulatorBuilder {
	
	/** Array of the grid containing whether a field was hit or not */
	private boolean[][] isHit;
	
	/** Frame limiting the area */
	private Rectangle frame;
	
	/** Affine transformation to transform Points in the method {@link #hit(Point)} 
	 * so they are contained in the specified array {@link #isHits} */
	private AffineTransformation at;
	
	/**
	 * Creates a new Builder for creating an IFSAccumulator
	 * @param frame limiting the area 
	 * @param width of the accumulator
	 * @param height of the accumulator
	 * @throws java.lang.IllegalArgumentException if the width or the height are not greater than zero
	 */
	public IFSAccumulatorBuilder(Rectangle frame, int width, int height) {
		if (width <= 0 || height <= 0) throw new IllegalArgumentException("Width and height must be greater than zero");
		
		this.frame	= frame;
		
		isHit	= new boolean[width][height];
		
		double sX	= (double) width / frame.width();
		double sY	= (double) height / frame.height();
		double dX	= -frame.left();
		double dY	= -frame.bottom();
		
		/*
		 * Since the point is contained in a Rectangle which lower-left point 
		 * is not necessarily in the origin, we have to create a translation, 
		 * which moves the point there. Also since the size of the Rectangle is 
		 * not necessarily the same as our array, which represents a grid. 
		 * That is why we also have to create a scaling and either increase 
		 * or decrease the point's coordinates. 
		 */
		at = AffineTransformation.newTranslation(dX, dY).composeWith(
				AffineTransformation.newScaling(sX, sY));
	}
	
	/**
	 * Hits the frame containing the specified Point: 
	 * Sets the field containing the point to true
	 * @param p Point
	 */
	public void hit(Point p) {
		
		// Only calculate this if the unmodified frame contains the unmodified Point
		// By unmodified we mean: Not transformed by the affine transformation yet
		if (frame.contains(p)) {
			p = at.transformPoint(p);
			
			// Since the coordinates are positive, there's no need of Math.floor()
			isHit[(int) p.x()][(int) p.y()] = true;
		}
	}
	
	/**
	 * Builds the IFSAccumulator and returns it
	 * @return the built accumulator
	 */
	public IFSAccumulator build() {
		return new IFSAccumulator(isHit);
	}
}
