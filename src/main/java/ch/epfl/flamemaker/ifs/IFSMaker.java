package ch.epfl.flamemaker.ifs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class IFSMaker {

	private final static int SIERPINSKY_TRIANGLE	= 0;
	private final static int BARNSLEYS_FERN			= 1;

	public static void main(String[] args) {
		genFractal(SIERPINSKY_TRIANGLE);
		genFractal(BARNSLEYS_FERN);
	}

	private static void genFractal(int FRACTAL) {
		switch (FRACTAL) {
			case SIERPINSKY_TRIANGLE: {
				int w = 100;
				int h = 100;

				Rectangle r = new Rectangle(new Point(0.5, 0.5), 1, 1);

				System.out.println("Computing image sierpinsky_triangle ...");
				IFSAccumulator ac = IFS.SIERPINSKY_TRIANGLE.compute(r, w, h, 1);

				System.out.println("Saving image sierpinsky_triangle ...");
				writeImage(ac, "sierpinsky_triangle");
			} break;
			case BARNSLEYS_FERN: {
				int w = 120;
				int h = 200;

				Rectangle r = new Rectangle(new Point(0.0, 4.5), 6, 10);

				System.out.println("Computing image barnsleys_fern ...");
				IFSAccumulator ac = IFS.BARNSLEYS_FERN.compute(r, w, h, 150);

				System.out.println("Saving image barnsleys_fern ...");
				writeImage(ac, "barnsleys_fern");
			} break;
		}
	}

	private static void writeImage(IFSAccumulator ac, String name) {

		int width	= ac.width();
		int height	= ac.height();

		PrintStream out = null;

		try {
			out = new PrintStream(new File("res/" + name + ".pbm"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		out.printf("P1\n");
		out.printf("%d %d\n", width, height);
		for (int j = height - 1; j >= 0; --j) {
			for (int i = 0; i < width; ++i)
				out.printf("%d ", (ac.isHit(i, j)) ? 1 : 0);
			out.printf("\n");
		}
		out.close();
	}
}
