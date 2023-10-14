package ch.epfl.flamemaker.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.InterpolatedPalette;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.FlameTransformation;
import ch.epfl.flamemaker.flame.Variation;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * A GUI for the {@code FlameMaker} software 
 * which can modify and display flame fractals 
 *
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.1
 */
public class FlameMakerGUI implements ResetableRectangle {

	/** {@code ObservableFlameBuilder} for computing the flame */
	private final ObservableFlameBuilder builder = new ObservableFlameBuilder(Flame.SHARKFIN);

	/** Backgroundcolor for the display */
	private final Color bgColor = Color.BLACK;

	/** {@code Palette} for painting the flame */
	private final Palette palette = new InterpolatedPalette(
			Color.RED,
			Color.GREEN,
			Color.BLUE);

	/** {@code Rectangle} limiting the area */
	private Rectangle rectangle = new Rectangle(
			new Point(-0.25, 0.0),
			5,
			4);

	/** Density for computing the flame */
	private final int density = 50;

	/** List of the {@code Observers} for the rectangle */
	private final List<Observer<Rectangle>> rectangleObservers = new ArrayList<Observer<Rectangle>>();

	/** List of the {@code Observers} for the selectedTransformationIndex */
	private final List<Observer<Integer>> observers = new ArrayList<Observer<Integer>>();

	/** The index of the selected transformation */
	private int selectedTransformationIndex = 0;

	/** Starts the program : Creates a {@code JFrame} and displays all components on it */
	public void start() {
		try {
			// Tries to set the look&feel to Nimbus look&feel
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException e) {
			System.err.printf("Class '%s' was not found! \nUsing '%s' instead.", e.getMessage(), UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (InstantiationException e) {
			System.err.printf("Could not instantiate class '%s'. \nUsing '%s' instead.", e.getMessage(), UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (IllegalAccessException e) {
			System.err.printf("Access exception, using '%s' instead.", UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			System.err.printf("LookAndFeel '%s' is not supported! \nUsing '%s' instead.", e.getMessage(), UIManager.getCrossPlatformLookAndFeelClassName());
		}

		// The frame containing the different panels 
		final JFrame frame = new JFrame("Flame Maker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		// Panel containing the components displaying the affine transformations and the flame fractal 
		final JPanel superPanel = new JPanel();
		superPanel.setLayout(new GridLayout(1, 2));

		// Panel containing the preview component for the affine transformations
		final JPanel affinePreviewPanel = new JPanel();
		affinePreviewPanel.setLayout(new BorderLayout());
		affinePreviewPanel.setBorder(BorderFactory.
				createTitledBorder("Affine Transformations Preview"));

		// Component for displaying the affine transformations
		final AffineTransformationsComponent affinePreviewComponent = new AffineTransformationsComponent(
				builder, rectangle);
		// Observer informing the affine preview component when the selected index changed
		addObserver(new Observer<Integer>() {
			@Override
			public void update(Integer index) {
				affinePreviewComponent.setHighlightedTransformationIndex(index);
			}
		});
		addRectangleObserver(new Observer<Rectangle>() {
			@Override
			public void update(Rectangle rectangle) {
				affinePreviewComponent.setRectangle(rectangle);
				affinePreviewComponent.repaint();
			}
		});

		// Panel containing the preview component for the flame fractal
		final JPanel fractalPreviewPanel = new JPanel();
		fractalPreviewPanel.setLayout(new BorderLayout());
		fractalPreviewPanel.setBorder(BorderFactory.
				createTitledBorder("Fractal Preview"));

		// Component for displaying the flame fractal
		final FlameBuilderPreviewComponent fractalPreviewComponent = new FlameBuilderPreviewComponent(
				builder, bgColor, palette, rectangle, density);
		fractalPreviewComponent.addRectangleResizeListener(this);
		addRectangleObserver(new Observer<Rectangle>() {
			@Override
			public void update(Rectangle rectangle) {
				fractalPreviewComponent.setRectangle(rectangle);
				fractalPreviewComponent.repaint();
			}
		});

		// Panel containing the transformations and the components for editing them 
		final JPanel inferPanel = new JPanel();
		inferPanel.setLayout(new BoxLayout(inferPanel, BoxLayout.LINE_AXIS));

		// Panel containing the transformations list and the buttons to add/remove transformations
		final JPanel transformationsListPanel = new JPanel();
		transformationsListPanel.setLayout(new BorderLayout());
		transformationsListPanel.setBorder(BorderFactory.
				createTitledBorder("Transformations"));

		final TransformationsListModel transformationsListModel = new TransformationsListModel();
		// JList containing all existing transformations of the builder
		final JList<String> transformationsList = new JList<String>(transformationsListModel);
		transformationsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		transformationsList.setVisibleRowCount(4);
		transformationsList.setSelectedIndex(0);

		// Listener informing the model (FlameMakerGUI) about any changes for the selected transformation
		transformationsList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent evt) {
				setSelectedIndex(transformationsList.getSelectedIndex());
			}
		});

		// JScrollPane containing the list of the transformations
		final JScrollPane transformationsScrollPane = new JScrollPane(transformationsList);

		// Panel containing the buttons for adding or removing a transformation
		final JPanel transformationsListButtons = new JPanel();
		transformationsListButtons.setLayout(new GridLayout(1, 2));

		// Button to remove a transformation
		final JButton removeTransformation = new JButton("Remove");
		removeTransformation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = selectedTransformationIndex;
				int size = transformationsListModel.getSize() - 1;

				// Selects the second last index (Last one after removing a transformation)
				transformationsList.setSelectedIndex((index == size)
						? index - 1
						: index + 1);

				transformationsListModel.removeTransformation(index);

				if (size <= 1)
					removeTransformation.setEnabled(false);
			}
		});

		// Button to add a transformation
		final JButton addTransformation = new JButton("Add (Identity)");
		// ActionListeners for adding/removing a transformation from the ListModel
		addTransformation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				transformationsListModel.addTransformation();

				int index = transformationsListModel.getSize() - 1;

				// Selects the last index and ensures it's visible
				transformationsList.setSelectedIndex(index);
				transformationsList.ensureIndexIsVisible(index);

				if (!removeTransformation.isEnabled())
					removeTransformation.setEnabled(true);
			}
		});

		// Panel containing the components for editing the selected transformation
		final JPanel transformationEditPanel = new JPanel();
		transformationEditPanel.setLayout(new BoxLayout(transformationEditPanel, BoxLayout.PAGE_AXIS));
		transformationEditPanel.setBorder(BorderFactory.
				createTitledBorder("Transformation Edit"));

		// Panel containing the components for editing the affine part of a transformation
		final JPanel transformationAffineEdit = new JPanel();
		// Creates the group layout for this panel and "configures" it
		final GroupLayout affineEditGroupLayout = new GroupLayout(transformationAffineEdit);
		configureAffineGroupLayout(affineEditGroupLayout);
		transformationAffineEdit.setLayout(affineEditGroupLayout);

		// Panel containing the components for editing and displaying the variations of a transformation
		final JPanel transformationVariationEdit = new JPanel();
		// Creates the group layout for this panel and "configures" it
		final GroupLayout variationEditGroupLayout = new GroupLayout(transformationVariationEdit);
		configureVariationGroupLayout(variationEditGroupLayout);
		transformationVariationEdit.setLayout(variationEditGroupLayout);

		// Adds all components to their associating panel 
		frame.getContentPane().add(superPanel, BorderLayout.CENTER);
		superPanel.add(affinePreviewPanel);
		affinePreviewPanel.add(affinePreviewComponent, BorderLayout.CENTER);
		superPanel.add(fractalPreviewPanel);
		fractalPreviewPanel.add(fractalPreviewComponent, BorderLayout.CENTER);
		frame.getContentPane().add(inferPanel, BorderLayout.PAGE_END);
		inferPanel.add(transformationsListPanel);
		transformationsListPanel.add(transformationsScrollPane, BorderLayout.CENTER);
		transformationsListPanel.add(transformationsListButtons, BorderLayout.PAGE_END);
		transformationsListButtons.add(addTransformation);
		transformationsListButtons.add(removeTransformation);
		inferPanel.add(transformationEditPanel);
		transformationEditPanel.add(transformationAffineEdit);
		transformationEditPanel.add(transformationVariationEdit);

		// Sets the frame's current and minimum size to the preferred size, after everything was added it's set visibile 
		frame.pack();
		frame.setMinimumSize(
				frame.getPreferredSize());
		frame.setVisible(true);
	}

	/**
	 * Configures the GroupLayout specified and adds components for 
	 * editing the affine part of a {@code FlameTransformation}
	 * @param gl GroupLayout to be modified 
	 */
	private void configureAffineGroupLayout(GroupLayout gl) {

		// DecimalFormat for all textfields, except for the rotation 
		final DecimalFormat format = new DecimalFormat("#0.0#");
		format.setRoundingMode(RoundingMode.HALF_UP);
		// DecimalFormat for the rotation textfield (Floating point numbers displayed as integers)
		final DecimalFormat rotationFormat = new DecimalFormat("#0");
		format.setRoundingMode(RoundingMode.HALF_UP);

		// Verifier checking that a value is not zero (For scaling)
		final InputVerifier nonZeroVerifier = new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				try {
					if (input instanceof JFormattedTextField) {
						JFormattedTextField textField = (JFormattedTextField) input;
						AbstractFormatter formatter = textField.getFormatter();
						double value = ((Number) formatter.stringToValue(textField.getText())).doubleValue();

						if (value == 0.0)
							textField.setText(formatter.valueToString(textField.getValue()));
					}
				} catch (ParseException ex) {
					ex.printStackTrace();
				}
				return true;
			}
		};

		// Textfield for the translation value 
		final JFormattedTextField translation = new JFormattedTextField(format);
		translation.setHorizontalAlignment(SwingConstants.RIGHT);
		translation.setValue(0.1);
		// Buttons and their ActionListeners for a translation 
		final JButton translationW = new JButton("\u2190");
		translationW.addActionListener(new AffineButtonActionListener(translation) {
			@Override
			public AffineTransformation applyTransformation(AffineTransformation current, double value) {
				return AffineTransformation.newTranslation(-value, 0.0);
			}
		});
		final JButton translationE = new JButton("\u2192");
		translationE.addActionListener(new AffineButtonActionListener(translation) {
			@Override
			public AffineTransformation applyTransformation(AffineTransformation current, double value) {
				return AffineTransformation.newTranslation(value, 0.0);
			}
		});
		final JButton translationN = new JButton("\u2191");
		translationN.addActionListener(new AffineButtonActionListener(translation) {
			@Override
			public AffineTransformation applyTransformation(AffineTransformation current, double value) {
				return AffineTransformation.newTranslation(0.0, value);
			}
		});
		final JButton translationS = new JButton("\u2193");
		translationS.addActionListener(new AffineButtonActionListener(translation) {
			@Override
			public AffineTransformation applyTransformation(AffineTransformation current, double value) {
				return AffineTransformation.newTranslation(0.0, -value);
			}
		});

		// Textfield for the rotation value 
		final JFormattedTextField rotation = new JFormattedTextField(rotationFormat);
		rotation.setHorizontalAlignment(SwingConstants.RIGHT);
		rotation.setValue(15);
		// Buttons and their ActionListeners for a rotation 
		final JButton rotationCounterclock = new JButton("\u21BA");
		rotationCounterclock.addActionListener(new AffineButtonActionListener(rotation) {
			@Override
			public AffineTransformation applyTransformation(AffineTransformation current, double value) {
				double dX = current.translationX(), dY = current.translationY();
				return AffineTransformation.newTranslation(-dX, -dY).composeWith(
						AffineTransformation.newRotation(Math.toRadians(value)).composeWith(
								AffineTransformation.newTranslation(dX, dY)));
			}
		});
		final JButton rotationClockwise = new JButton("\u21BB");
		rotationClockwise.addActionListener(new AffineButtonActionListener(rotation) {
			@Override
			public AffineTransformation applyTransformation(AffineTransformation current, double value) {
				double dX = current.translationX(), dY = current.translationY();
				return AffineTransformation.newTranslation(-dX, -dY).composeWith(
						AffineTransformation.newRotation(Math.toRadians(-value))).composeWith(
						AffineTransformation.newTranslation(dX, dY));
			}
		});

		// Textfield for the scaling value 
		final JFormattedTextField scaling = new JFormattedTextField(format);
		scaling.setHorizontalAlignment(SwingConstants.RIGHT);
		scaling.setValue(1.05);
		// Sets an InputVerifier so only nonzero values will be accepted
		scaling.setInputVerifier(nonZeroVerifier);
		// Buttons and their ActionListeners for a scaling
		final JButton scaleHPlus = new JButton("+ \u2194");
		scaleHPlus.addActionListener(new AffineButtonActionListener(scaling) {
			@Override
			public AffineTransformation applyTransformation(AffineTransformation current, double value) {
				return AffineTransformation.newScaling(value, 1.0);
			}
		});
		final JButton scaleHMinus = new JButton("- \u2194");
		scaleHMinus.addActionListener(new AffineButtonActionListener(scaling) {
			@Override
			public AffineTransformation applyTransformation(AffineTransformation current, double value) {
				return AffineTransformation.newScaling(1.0 / value, 1.0);
			}
		});
		final JButton scaleVPlus = new JButton("+ \u2195");
		scaleVPlus.addActionListener(new AffineButtonActionListener(scaling) {
			@Override
			public AffineTransformation applyTransformation(AffineTransformation current, double value) {
				return AffineTransformation.newScaling(1.0, value);
			}
		});
		final JButton scaleVMinus = new JButton("- \u2195");
		scaleVMinus.addActionListener(new AffineButtonActionListener(scaling) {
			@Override
			public AffineTransformation applyTransformation(AffineTransformation current, double value) {
				return AffineTransformation.newScaling(1.0, 1.0 / value);
			}
		});

		// Textfield for the shearing value 
		final JFormattedTextField shearing = new JFormattedTextField(format);
		shearing.setHorizontalAlignment(SwingConstants.RIGHT);
		shearing.setValue(0.1);

		// Buttons and their ActionListeners for a shearing 
		final JButton shearW = new JButton("\u2190");
		shearW.addActionListener(new AffineButtonActionListener(shearing) {
			@Override
			public AffineTransformation applyTransformation(AffineTransformation current, double value) {
				return AffineTransformation.newShearX(-value);
			}
		});
		final JButton shearE = new JButton("\u2192");
		shearE.addActionListener(new AffineButtonActionListener(shearing) {
			@Override
			public AffineTransformation applyTransformation(AffineTransformation current, double value) {
				return AffineTransformation.newShearX(value);
			}
		});
		final JButton shearN = new JButton("\u2191");
		shearN.addActionListener(new AffineButtonActionListener(shearing) {
			@Override
			public AffineTransformation applyTransformation(AffineTransformation current, double value) {
				return AffineTransformation.newShearY(value);
			}
		});
		final JButton shearS = new JButton("\u2193");
		shearS.addActionListener(new AffineButtonActionListener(shearing) {
			@Override
			public AffineTransformation applyTransformation(AffineTransformation current, double value) {
				return AffineTransformation.newShearY(-value);
			}
		});

		// An array on how to "align" the components in the GroupLayout 
		final Component[][] components = {
				{ new JLabel("Translation"), translation,	translationW, translationE, translationN, translationS },
				{ new JLabel("Rotation"), rotation, rotationCounterclock, rotationClockwise },
				{ new JLabel("Scaling"), scaling, scaleHPlus, scaleHMinus, scaleVPlus, scaleVMinus },
				{ new JLabel("Shearing"), shearing, shearW, shearE, shearN, shearS }
		};

		// The sequential groups for the parallel groups 
		final Group vertical = gl.createSequentialGroup();
		final Group horizontal = gl.createSequentialGroup();

		// Six horizontal (columns) Groups 
		final Group[] h = new Group[6];
		// Aligns the labels to the right side 
		h[0] = gl.createParallelGroup(Alignment.TRAILING);
		for (int i = 1; i < h.length; ++i)
			h[i] = gl.createParallelGroup();

		// Iterates over the components array 
		for (int i = 0; i < components.length; ++i) {
			// Creates a vertical group (row) and centers all components on the baseline 
			final Group v = gl.createParallelGroup(Alignment.BASELINE);

			for (int j = 0; j < components[i].length; ++j) {
				v.addComponent(components[i][j]);

				h[j].addComponent(components[i][j]);
				if (i + 1 >= components.length) {
					horizontal.addGroup(h[j]);
				}
			}
			vertical.addGroup(v);
		}

		// Sets the vertical and horizontal groups of the GroupLayout 
		gl.setVerticalGroup(vertical);
		gl.setHorizontalGroup(horizontal);
	}


	/**
	 * Configures the GroupLayout specified and adds components for 
	 * editing the variation part of a {@code FlameTransformation}
	 * @param gl GroupLayout to be modified 
	 */
	private void configureVariationGroupLayout(GroupLayout gl) {
		// The sequential groups for the parallel groups 
		final Group vertical = gl.createSequentialGroup();
		final Group horizontal = gl.createSequentialGroup();

		final int size = Variation.ALL_VARIATIONS.size();

		// Decimal format for the textfields 
		final DecimalFormat format = new DecimalFormat("#0.##");
		format.setRoundingMode(RoundingMode.HALF_UP);
		// Array of textfields for adding them later to an observer 
		final JFormattedTextField[] textFields = new JFormattedTextField[size];

		// Old values of the textfields 
		final double[] oldValues = new double[size];

		// Six horizontal (columns) Groups, 3x JFormattedTextField & JLabel for description 
		final Group[] h = new Group[6];
		for (int i = 0; i < h.length; ++i)
			// Creates the group and sets the alignment of it
			h[i] = gl.createParallelGroup(Alignment.TRAILING);

		// Calculates the amount of vertical groups (rows), if there are 6 elements (3 variations) per group (row)
		for (int i = 0; i < size / 3.0 + 1; ++i) {
			// Creates the group and sets the alignment of it 
			final Group v = gl.createParallelGroup(Alignment.BASELINE);

			// Creates the components for the row, 3 if it's not the last row, otherwise the amount of the last one 
			for (int j = 0; j < Math.min(3, size - 3*i); ++j) {
				final int thisIndex = 3*i + j;
				final Variation current = Variation.ALL_VARIATIONS.get(thisIndex);

				// The JLabel and the JFormattedTextField for displaying the variation's name and weight 
				final JLabel label = new JLabel(current.name());
				final JFormattedTextField textField = new JFormattedTextField(format);
				textField.setHorizontalAlignment(SwingConstants.RIGHT);

				// Uses setValue() for setting an initial oldValue from the PropertyChangeEvent
				oldValues[thisIndex] = builder.variationWeight(selectedTransformationIndex, current);
				textField.setValue(oldValues[thisIndex]);

				// Adds the textfield to the array so it can observer the current selected transformation
				textFields[thisIndex] = textField;

				// PropertyChangeListener informing the builder about any changes on the "value"-property
				textField.addPropertyChangeListener("value", new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						double value = ((Number) evt.getNewValue()).doubleValue();
						if (value != oldValues[thisIndex]) {
							builder.setVariationWeight(selectedTransformationIndex, current, ((Number) evt.getNewValue()).doubleValue());
							oldValues[thisIndex] = value;
						}
					}
				});

				// After configuring the components, they're added to the vertical group (row)
				v.addComponent(label);
				v.addComponent(textField);

				// They are also added to the corresponding horizontal group, for not having to use another for-loop 
				h[2*j].addComponent(label);
				h[2*j+1].addComponent(textField);

				// Adds the horizontal group to the group of horizontal groups 
				horizontal.addGroup(h[thisIndex]);
				if (thisIndex % 2 == 1)
					// Adds a gap between the different variations (textfield + label) 
					((SequentialGroup) horizontal).addPreferredGap(ComponentPlacement.UNRELATED);
			}

			// Adds the vertical group to the group of vertical groups 
			vertical.addGroup(v);
		}

		// Adds an observer updating all the textfield once the selected transformation changes 
		addObserver(new Observer<Integer>() {
			@Override
			public void update(Integer index) {
				for (int i = 0; i < textFields.length; ++i) {
					oldValues[i] = builder.variationWeight(index, Variation.ALL_VARIATIONS.get(i));
					textFields[i].setValue(oldValues[i]);
				}
			}
		});

		// Sets the vertical and horizontal groups of the GroupLayout 
		gl.setVerticalGroup(vertical);
		gl.setHorizontalGroup(horizontal);
	}

	/** @param selectedTransformationIndex index of the new selected {@code Transformation} */
	public void setSelectedIndex(int selectedTransformationIndex) {
		this.selectedTransformationIndex = selectedTransformationIndex;
		// Notifies all observers about the changes 
		notifyObservers();
	}

	/** @returns the index of the currently selected {@code Transformation} */
	public int selectedTransformationIndex() {
		return selectedTransformationIndex;
	}

	/** @param observer to add to the list */
	public void addObserver(Observer<Integer> o) {
		observers.add(o);
	}

	/** @param observer to remove from the list */
	public void removeObserver(Observer<Integer> o) {
		observers.remove(o);
	}

	/** Notifies all the observers about any changes */
	protected void notifyObservers() {
		for (Observer<Integer> o : observers)
			o.update(selectedTransformationIndex);
	}

	/** @param rectangle the new {@code Rectangle} */
	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
		notifyRectangleObservers();
	}

	/** @param observer to add to the rectangle observers list */
	public void addRectangleObserver(Observer<Rectangle> o) {
		rectangleObservers.add(o);
	}

	/** @param observer to remove from the rectangle observers list */
	public void removeRectangleObserver(Observer<Rectangle> o) {
		rectangleObservers.remove(o);
	}

	protected void notifyRectangleObservers() {
		for (Observer<Rectangle> o : rectangleObservers)
			o.update(rectangle);
	}

	/**
	 * Interface for observer 
	 *
	 * @author Groux Marcel Jean Jacques	227630
	 * @author Platzer Casimir Benjamin		228352
	 * @param <T> Type of the observed object 
	 */
	protected interface Observer<T> {
		/**
		 * Updates the observer 
		 * @param object which changed 
		 */
		public void update(T object);
	}

	/**
	 * {@code ActionListener} for the buttons modifying the {@code AffineTransformation}
	 *
	 * @author Groux Marcel Jean Jacques	227630
	 * @author Platzer Casimir Benjamin		228352
	 */
	private abstract class AffineButtonActionListener implements ActionListener {

		/** The {@code JFormattedTextField} for extracting the value for the transformation */
		private final JFormattedTextField textField;

		/**
		 * Creates a new {@code ActionListener}
		 * @param textField to extract the value from 
		 */
		public AffineButtonActionListener(JFormattedTextField textField) {
			this.textField = textField;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// Gets the index of the selected transformation and so the currently selected transformation 
			int index = selectedTransformationIndex;
			AffineTransformation current = builder.affineTransformation(index);

			// Modifies the currently selected transformation by getting the transformation to modify with
			builder.setAffineTransformation(index, current.composeWith(
					applyTransformation(current, ((Number) textField.getValue()).doubleValue())));
		}

		/**
		 * This method allows to get a transformation which will be used to modify another one 
		 * @param current The currently selected transformation
		 * @param value the value of the according textfield
		 * @return a transformation representing the selected modification
		 */
		public abstract AffineTransformation applyTransformation(AffineTransformation current, double value);
	}

	/**
	 * {@code ListModel} as model of the {@code JList}
	 *
	 * @author Groux Marcel Jean Jacques	227630
	 * @author Platzer Casimir Benjamin		228352
	 */
	private class TransformationsListModel extends AbstractListModel<String> {

		/** */
		private final static long serialVersionUID = 1L;

		/** Adds a identity transformation to the builder and to the list */
		public void addTransformation() {
			// Allows to change the amount of Variations dynamicly
			double[] weights = new double[Variation.ALL_VARIATIONS.size()];
			weights[0] = 1.0;
			for (int i = 1; i < weights.length; ++i)
				weights[i] = 0.0;

			FlameTransformation ft = new FlameTransformation(
					AffineTransformation.IDENTITY,
					weights);
			builder.addTransformation(ft);
			fireIntervalAdded(this, getSize() - 1, getSize());
		}

		/**
		 * Removes a transformation from the builder and the list 
		 * @param index of the transformation to remove
		 */
		public void removeTransformation(int index) {
			builder.removeTransformation(index);
			fireIntervalRemoved(this, index, index);
		}

		@Override
		public int getSize() {
			return builder.transformationsCount();
		}

		@Override
		public String getElementAt(int index) {
			return String.format("Transformation n\u00B0%d", index + 1);
		}
	}
}
