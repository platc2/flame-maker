package ch.epfl.flamemaker.flame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.InterpolatedPalette;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * Creates the specified fractals 
 * and saves them to a file 
 *
 * @author Groux Marcel Jean Jacques	227630
 * @author Platzer Casimir Benjamin		228352
 * @version 1.0
 */
public class FlamePPMMaker {
	public static void main(String[] args) {

		final Palette p = new InterpolatedPalette(Arrays.asList(
				Color.RED,
				Color.GREEN,
				Color.BLUE));

		saveFile(Flame.TURBULENCE.compute(
						new Rectangle(new Point(0.1, 0.1), 3, 3), 500, 500, 50),
				p,
				Color.BLACK,
				"turbulence.ppm");
		saveFile(Flame.SHARKFIN.compute(
						new Rectangle(new Point(-0.25, 0), 5, 4), 500, 400, 50),
				p,
				Color.BLACK,
				"shark-fin.ppm");
	}

	/**
	 * Creates a .ppm-file given the needed values 
	 * @param ac the {@code FlameAccumulator} the {@code Flame} is stored in 
	 * @param p {@code Palette} for painting the {@code Flame}
	 * @param background {@code Color}
	 * @param filename of the .ppm-file (Including path)
	 */
	private static void saveFile(FlameAccumulator ac, Palette p, Color background, String filename) {
		PrintStream out = null;

		int width = ac.width();
		int height = ac.height();

		Color tmp = null;


		try {
			out = new PrintStream(new File(filename));
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}

		// Fills the file with the needed values 
		out.printf("P3\n");
		out.printf("%d %d\n", width, height);
		out.printf("100\n");

		for (int j = height - 1; j >= 0; --j) {
			for (int i = 0; i < width; ++i) {
				tmp = ac.color(p, background, i, j);

				// Prints out the values of the color-components
				out.printf("%d %d %d ",
						Color.sRGBEncode(tmp.red(), 100),
						Color.sRGBEncode(tmp.green(), 100),
						Color.sRGBEncode(tmp.blue(), 100));
			}
			out.printf("\n");
		}
		out.close();

		System.out.printf("Saved file to : %s\n", filename);
	}
}
