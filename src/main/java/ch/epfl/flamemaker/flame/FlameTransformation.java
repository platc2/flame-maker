package ch.epfl.flamemaker.flame;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Transformation;

/**
 * {@code FlameTransformation} formed by an {@code AffineTransformation} and {@code Variations}
 * 
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @see ch.epfl.flamemaker.flame.Variation
 * @version 1.0
 */
public class FlameTransformation implements Transformation {
	
	/** The affine part of the {@code FlameTransformation}  */
	private final AffineTransformation affineTransformation;
	
	/** The weight of the different variations */
	private final double[] variationWeight;
	
	/**
	 * Creates a new {@code FlameTransformation} given an {@code AffineTransformation} 
	 * and the weight for the different variations 
	 * 
	 * @param affineTransformation an {@code AffineTransformation} 
	 * @param variationWeight of the different variations
	 * @throws java.lang.IllegalArgumentException if the weights-array has an invalid size 
	 */
	public FlameTransformation(AffineTransformation affineTransformation, double[] variationWeight) {
		if (variationWeight.length != Variation.ALL_VARIATIONS.size()) 
			throw new IllegalArgumentException("Array of weights has an invalid size!");
		
		this.affineTransformation = affineTransformation;
		this.variationWeight = variationWeight.clone();
	}
	
	@Override
	public Point transformPoint(Point p) {
		
		double x = 0, y = 0;
		Point tmp;
		
		p = affineTransformation.transformPoint(p);
		
		// Iterates over all variations and transforms the point
		for (int i = 0; i < variationWeight.length; ++i) {
			if (variationWeight[i] != 0) {
				
				// Variation_index(AffineTransformation(Point)) 
				// The point is first being transformed by the affine transformation and then by the variation
				tmp = Variation.ALL_VARIATIONS.get(i).transformPoint(p);
				
				// The weight is applied to the point, before it's added to our output-point, represented only by the coordinates
				x += tmp.x() * variationWeight[i];
				y += tmp.y() * variationWeight[i];
			}
		}
		
		// Returns the output-point transformed by the given formula for flame fractals
		return new Point(x, y);
	}
	
	/**
	 * A builder for {@code FlameTransformation}
	 * 
	 * @author Groux Marcel Jean Jacques	227630
	 * @author Platzer Casimir Benjamin		228352
	 * @version 1.0
	 */
	public static class Builder {
		
		/** {@code AffineTransformation} of the {@code FlameTransformation} */
		private AffineTransformation affineTransformation;
		
		/** Weights of the {@code Variation} */
		private double[] variationWeight;
		
		/**
		 * Creates a new {@code FlameTransformation.Builder} given a {@code FlameTransformation} 
		 * @param transformation 
		 */
		public Builder(FlameTransformation transformation) {
			this.affineTransformation = transformation.affineTransformation;
			this.variationWeight = transformation.variationWeight.clone();
		}
		
		/** @return the {@code AffineTransformation} of the {@code FlameTransformation} */
		public AffineTransformation affineTransformation() {
			return affineTransformation;
		}
		
		/**
		 * Sets the {@code AffineTransformation} 
		 * 
		 * @param transformation new {@code AffineTransformation} 
		 */
		public void setAffineTransformation(AffineTransformation transformation) {
			this.affineTransformation = transformation;
		}
		
		/**
		 * @param variation to get the weight of 
		 * @return the weight of the specified {@code Variation} 
		 */
		public double variationWeight(Variation variation) {
			return variationWeight[variation.index()];
		}
		
		/**
		 * Sets the weight of the specified {@code Variation} 
		 * 
		 * @param variation to set the weight of 
		 * @param weight of the {@code Variation} 
		 */
		public void setVariationWeight(Variation variation, double weight) {
			variationWeight[variation.index()] = weight;
		}
		
		/**
		 * Builds the {@code FlameTransformation} and returns it 
		 * @return the built {@code FlameTransformation} 
		 */
		public FlameTransformation build() {
			return new FlameTransformation(affineTransformation, variationWeight);
		}
	}
}
