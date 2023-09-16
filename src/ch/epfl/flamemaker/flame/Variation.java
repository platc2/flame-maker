package ch.epfl.flamemaker.flame;

import java.util.Arrays;
import java.util.List;

import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Transformation;

/**
 * {@code Variation} for flame transformations
 * 
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */
public abstract class Variation implements Transformation {
	
	/** Static list of given variations */
	public static final List<Variation> ALL_VARIATIONS = Arrays.asList(
			new Variation(0, "Linear") {
				@Override
				public Point transformPoint(Point p) {
					return new Point(
							p.x(), 
							p.y());
				}
			}, new Variation(1, "Sinusoidal") {
				@Override
				public Point transformPoint(Point p) {
					return new Point(
							Math.sin(p.x()), 
							Math.sin(p.y()));
				}
			}, new Variation(2, "Spherical") {
				@Override
				public Point transformPoint(Point p) {
					double tmp = p.r() * p.r();
					return new Point(
							p.x() / tmp, 
							p.y() / tmp);
				}
			}, new Variation(3, "Swirl") {
				@Override
				public Point transformPoint(Point p) {
					double tmp = p.r() * p.r();
					double tmp_sin = Math.sin(tmp);
					double tmp_cos = Math.cos(tmp);
					return new Point(
							p.x() * tmp_sin - p.y() * tmp_cos, 
							p.x() * tmp_cos + p.y() * tmp_sin);
				}
			}, new Variation(4, "Horseshoe") {
				@Override
				public Point transformPoint(Point p) {
					return new Point(
							(p.x() - p.y()) * (p.x() + p.y()) / p.r(), 
							2 * p.x() * p.y() / p.r());
				}
			}, new Variation(5, "Bubble") {
				@Override
				public Point transformPoint(Point p) {
					double tmp = p.r() * p.r();
					return new Point(
							4 * p.x() / (tmp + 4), 
							4 * p.y() / (tmp + 4));
				}
			});
	
	/** The index of the {@code Variation} */
	private final int index;
	
	/** The name of the {@code Variation} */
	private final String name;
	
	/**
	 * Creates a new {@code Variation} with given index and name 
	 * @param index of the {@code Variation} 
	 * @param name of the {@code Variation} 
	 */
	private Variation(int index, String name) {
		this.index = index;
		this.name = name;
	}
	
	/** @return the name of the {@code Variation} */
	public String name() {
		return name;
	}
	
	/** @return the name of the {@code Variation} */
	public int index() {
		return index;
	}
	
	@Override
	abstract public Point transformPoint(Point p);
}
