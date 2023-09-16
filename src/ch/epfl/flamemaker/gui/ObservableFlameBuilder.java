package ch.epfl.flamemaker.gui;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.FlameTransformation;
import ch.epfl.flamemaker.flame.Variation;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.gui.FlameMakerGUI.Observer;

/** 
 * An observable builder for a {@code Flame}
 * 
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */
public class ObservableFlameBuilder {
	
	/** A {@code Flame.Builder} for the {@code ObservableFlameBuilder} */
	private final Flame.Builder builder;
	
	/** List of {@code Observers} */
	private final List<Observer<ObservableFlameBuilder>> observers = new ArrayList<Observer<ObservableFlameBuilder>>();
	
	/**
	 * Creates an new {@code ObservableFlameBuilder} given a {@code Flame} and so it's {@code FlameTransformations}
	 * @param flame for the {@code FlameTransformations}
	 */
	public ObservableFlameBuilder(Flame flame) {
		builder = new Flame.Builder(flame);
	}
	
	/** @return the amount of {@code FlameTransformations} */
	public int transformationsCount() {
		return builder.transformationsCount();
	}
	
	/** 
	 * Adds a new {@code FlameTransformation} to the {@code Flame} 
	 * 
	 * @param transformation to add
	 */
	public void addTransformation(FlameTransformation transformation) {
		builder.addTransformation(transformation);
		notifyObservers();
	}
	
	/**
	 * @param index of the {@code FlameTransformation} 
	 * @return the {@code AffineTransformation} of the {@code FlameTransformation} 
	 */
	public AffineTransformation affineTransformation(int index) {
		return builder.affineTransformation(index);
	}
	
	/** 
	 * Sets the {@code AffineTransformation} of the {@code FlameTransformation} 
	 * 
	 * @param index of the {@code FlameTransformation} 
	 * @param transformation to set
	 */
	public void setAffineTransformation(int index, AffineTransformation transformation) {
		builder.setAffineTransformation(index, transformation);
		notifyObservers();
	}
	
	/**
	 * @param index of the {@code FlameTransformation} 
	 * @param variation to get the weight from
	 * @return the weight of the specified {@code Variation} 
	 */
	public double variationWeight(int index, Variation variation) {
		return builder.variationWeight(index, variation);
	}
	
	/**
	 * Sets the weight of the specified {@code Variation} in the specified {@code FlameTransformation} 
	 * @param index of the {@code FlameTransformation} 
	 * @param variation to modify 
	 * @param weight of the {@code Variation} 
	 */
	public void setVariationWeight(int index, Variation variation, double weight) {
		builder.setVariationWeight(index, variation, weight);
		notifyObservers();
	}
	
	/**
	 * Removes the {@code FlameTransformation} at the specified position from the list 
	 * @param index of the {@code FlameTransformation} 
	 */
	public void removeTransformation(int index) {
		builder.removeTransformation(index);
		notifyObservers();
	}
	
	/** 
	 * Builds the {@code Flame} and returns it. 
	 * @return the built {@code Flame} 
	 */
	public Flame build() {
		return builder.build();
	}
	
	/** @param o Adds the specified {@code observer} to the {@code list} */
	public void addObserver(Observer<ObservableFlameBuilder> o) {
		observers.add(o);
	}
	
	/** @param o Removes the specified {@code observer} from the {@code list} */
	public void removeObserver(Observer<ObservableFlameBuilder> o) {
		observers.remove(o);
	}
	
	/** Notifies all the {@code Observers} */
	protected void notifyObservers() {
		for (Observer<ObservableFlameBuilder> o : observers) 
			o.update(this);
	}
}
