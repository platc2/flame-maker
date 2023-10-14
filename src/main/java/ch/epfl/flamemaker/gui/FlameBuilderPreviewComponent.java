package ch.epfl.flamemaker.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.Timer;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.flame.FlameAccumulator;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;
import ch.epfl.flamemaker.gui.FlameMakerGUI.Observer;

/**
 * Component for displaying a flame fractal 
 * 
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.1
 */
public class FlameBuilderPreviewComponent extends JComponent implements ResetableRectangle {
	
	/** */
	private final static long serialVersionUID = 1L;
	
	/** Points to calculate for iterative calculations */
	private static final int STEP = 10000;
	
	/** {@code ObservableFlameBuilder} for computing the flame */
	private final ObservableFlameBuilder builder;
	
	/** Backgroundcolor for the display */
	private final Color bgColor;
	
	/** {@code Palette} for painting the flame */
	private final Palette palette;
	
	/** {@code Rectangle} limiting the area */
	private Rectangle rectangle;
	
	/** Density for computing the flame */
	private final int density;
	
	/** Timer for calculating the fractal iterative */
	private Timer timer; 
	
	/** An image containing the flame */
	private Image image; 
	
	/** FlameAccumulator for the computer flame */
	private FlameAccumulator.Builder accBuilder = null; 
	
	/** boolean value whether the builder changed or not */
	private boolean builderChanged = false; 
	
	/** boolean value whether the rectangle changed or not */
	private boolean rectangleChanged = false; 
	
	/** The old value of width and height */
	private int width, height; 
	
	/**
	 * Creates a new {@code FlameBuilderPreviewComponent} for displaying the flame fractal 
	 * @param builder for computing the flame
	 * @param bgColor for the display
	 * @param palette for painting the flame
	 * @param rectangle limiting the area
	 * @param density for computing the flame
	 */
	public FlameBuilderPreviewComponent(ObservableFlameBuilder builder, Color bgColor, Palette palette, Rectangle rectangle, int density) {
		this.builder = builder;
		this.bgColor = bgColor;
		this.palette = palette;
		this.rectangle = rectangle;
		this.density = density;
		
		builder.addObserver(new Observer<ObservableFlameBuilder>() {
			@Override
			public void update(ObservableFlameBuilder builder) {
				builderChanged = true;
				repaint();
			}
		});
	}
	
	@Override
	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
		rectangleChanged = true;
	}
	
	public void builderChanged(boolean changed) {
		builderChanged = changed;
	}
	
	public void addRectangleResizeListener(ResetableRectangle model) {
		RectangleResizeListener listener = new RectangleResizeListener(model);
		addMouseListener(listener);
		addMouseMotionListener(listener);
		addMouseWheelListener(listener);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(200, 100);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		final Graphics2D g2d = (Graphics2D) g;
		
		// Expands the rectangle to the smallest one with the same aspectRatio as the component 
		Rectangle tmp = rectangle.expandToAspectRatio((double) getWidth() / (double) getHeight());
		
		// If the builder hasn't been initialized yet, the rectangle has or the component's size have changed 
		if (accBuilder == null || rectangleChanged || width != getWidth() || height != getHeight()) {
			// Updates the current dimensions of the component 
			width = getWidth();
			height = getHeight();
			
			// Rectangle with same aspect ratio as the component 
			tmp = rectangle.expandToAspectRatio((double) width / (double) height); 
			
			builderChanged = true; 
			rectangleChanged = false;
		}
		
		if (builderChanged) {
			// Reset the FlameAccumulator.Builder 
			accBuilder = new FlameAccumulator.Builder(tmp, width, height);
			
			// If there's a calculation from before going on, stop it 
			if (timer != null) 
				timer.stop();
			
			// Timer for iterative calculation 
			timer = new Timer(0, new ActionListener() {
				int count = 0;
				@Override
				public void actionPerformed(ActionEvent e) {
					final int m = width * height * density;
					// The points to add, without going further than m 
					final int add = (m - count < STEP) ? m - count : STEP;
					
					accBuilder = builder.build().compute(add, accBuilder);
					image = getAccumulatorImage(accBuilder.build());
					
					count += add;
					
					repaint();
					
					if (count >= m) 
						timer.stop();
				}
			});
			timer.setDelay(30);
			timer.setRepeats(true);
			timer.start();
			
			builderChanged = false;
		}
		
		// Draws the image to the screen at position (0, 0) (used null as AffineTransform)
		g2d.drawImage(image, null, null);
	}
	
	/**
	 * Creates an {@code Image} showing the content of the {@code FlameAccumulator}
	 * @param ac {@code FlameAccumulator} which content will be <i>"printed"</i> onto an {@code Image}
	 * @return {@code Image} containing the {@code FlameAccumulator}s content
	 */
	private Image getAccumulatorImage(FlameAccumulator ac) {
		// Width and height of the accumulator 
		int width = ac.width();
		int height = ac.height();
		
		final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		// Fills the image with the colors given by the accumulator 
		// (Also flips the image on the x-axis, since another coordinate system was used for the accumulator!)
		for (int i = 0; i < width; ++i) 
			for (int j = 0; j < height; ++j) 
				image.setRGB(i, height - j - 1, ac.color(
						palette, bgColor, i, j).asPackedRGB());
		
		return image;
	}
	
	/** 
	 * {@code MouseAdapter} listening for mouseEvents on the flame 
	 * preview component for resizing or moving the {@code Rectangle} 
	 * 
	 * @author Groux Marcel Jean Jacques	227630
	 * @author Platzer Casimir Benjamin		228352
	 * @version 1.0
	 */
	class RectangleResizeListener extends MouseAdapter {
		
		/** Factor for scaling */
		private final static double SCALE_FACTOR = 1.05;
		
		/** A model whose rectangle can be reset again */
		private final ResetableRectangle model; 
		
		/** Whether the left mousebutton is pressed or not */
		private boolean mouseLeftPressed = false;
		
		/** Whether the right mousebutton is pressed or not */
		private boolean mouseRightPressed = false; 
		
		// Old coordinates 
		private double oldX; 
		private double oldY; 
		
		// Timer for the constant scaling 
		private Timer scaleTimer; 
		
		/** 
		 * Creates a new {@code RectangleResizeListener} using the specified model 
		 * 
		 * @param model to modify the {@code Rectangle} 
		 */
		public RectangleResizeListener(ResetableRectangle model) {
			this.model = model;
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			
			// Saves the last saved values 
			oldX = e.getX(); 
			oldY = e.getY(); 
			
			switch (e.getButton()) {
			case 1: mouseLeftPressed = true; 
				break;
			case 3: mouseRightPressed = true; 
				break;
			}
			
			// Sets the cursor depending on the pressed buttons 
			setCursors();
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			switch (e.getButton()) {
			case 1: mouseLeftPressed = false; 
				break;
			case 3: {
				mouseRightPressed = false;
				scaleTimer.stop();
			}
				break;
			}
			
			// Sets the cursor depending on the pressed buttons 
			setCursors();
		}
		
		@Override
		public void mouseDragged(MouseEvent e) { 
			// Either moves the rectangle or resizes it, depending what mousebuttons are pressed 
			if (mouseLeftPressed) 
				move(e);
			else if (mouseRightPressed) 
				scale(e); 
		}
		
		
		/** Sets the cursor depending on what button is pressed */
		private void setCursors() {
			if (mouseLeftPressed) 
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			else if (mouseRightPressed) 
				setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
			else 
				setCursor(Cursor.getDefaultCursor());
		}
		
		/** Moves the rectangle depending on the mouseEvent */
		private void move(MouseEvent e) {
			final double compWidth = getWidth();
			final double compHeight = getHeight();
			
			final Rectangle tmp = rectangle.expandToAspectRatio(compWidth / compHeight);
			final Point c = rectangle.center(); 
			
			final double x = e.getX();
			final double y = e.getY();
			
			// Calculates the delta values and converts them to the same coordinate system as the rectangle (multiplication)
			final double dX = -(x - oldX) * (tmp.width() / compWidth); 
			final double dY = (y - oldY) * (tmp.height() / compHeight); 
			
			// Updates the old values
			oldX = x;
			oldY = y;
			
			model.setRectangle(new Rectangle(new Point(c.x() + dX, c.y() + dY), rectangle.width(), rectangle.height()));
		}
		
		/** Scales the image depending of the wheelrotation */
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			// The scrollamount calculated depending of the scrolltype 
			int amount = 1;
			if (e.getScrollType() == MouseWheelEvent.WHEEL_BLOCK_SCROLL) 
				amount = e.getScrollAmount();
			
			// The rotation of the scrolling
			int rotation = e.getWheelRotation();
			
			// Applies a scaling to the rectangle 
			for (int i = 0; i < amount; ++i) 
				if (amount * rotation < 0) 
					scale(SCALE_FACTOR);
				else 
					scale(1.0 / SCALE_FACTOR);
		}
		
		/** Scales the rectangle depending on the mouseEvent */
		private void scale(MouseEvent e) {
			final double dY = -(e.getY() - oldY);
			
			// Factor to multiply 
			final double factor = ((SCALE_FACTOR - 1) / (getHeight() / 2.0)) * Math.abs(dY) + 1;
			
			if (scaleTimer != null) 
				scaleTimer.stop();
			
			// Timer for constant scaling while the user has the mouse pressed 
			scaleTimer = new Timer(0, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (dY < 0) 
						scale(1.0 / factor);
					else 
						scale (factor);
				}
			});
			scaleTimer.setRepeats(true);
			scaleTimer.setDelay(30);
			scaleTimer.start();
		}
		
		/** Method used to scale the Rectangle */
		private void scale(double factor) {
			model.setRectangle(new Rectangle(rectangle.center(), rectangle.width() * factor, rectangle.height() * factor));
		}
	}
}

/** 
 * Interface for components which can modify their rectangle 
 * 
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */
interface ResetableRectangle {
	/** @param rectangle to replace */
	public void setRectangle(Rectangle rectangle); 
}