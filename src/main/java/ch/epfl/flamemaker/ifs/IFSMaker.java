package ch.epfl.flamemaker.ifs;

import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public final class IFSMaker {

    private final static int SIERPINSKY_TRIANGLE = 0;
    private final static int BARNSLEYS_FERN = 1;

    public static void main(final String[] arguments) {
        genFractal(SIERPINSKY_TRIANGLE);
        genFractal(BARNSLEYS_FERN);
    }

    private static void genFractal(int FRACTAL) {
        switch (FRACTAL) {
            case SIERPINSKY_TRIANGLE: {
                final int width = 100;
                final int height = 100;

                final Rectangle rectangle = new Rectangle(new Point(0.5, 0.5), 1, 1);

                System.out.println("Computing image sierpinsky_triangle ...");
                final IFSAccumulator ifsAccumulator = IFS.SIERPINSKY_TRIANGLE.compute(rectangle, width, height, 1);

                System.out.println("Saving image sierpinsky_triangle ...");
                writeImage(ifsAccumulator, "sierpinsky_triangle");
            }
            break;
            case BARNSLEYS_FERN: {
                final int width = 120;
                final int height = 200;

                final Rectangle rectangle = new Rectangle(new Point(0.0, 4.5), 6, 10);

                System.out.println("Computing image barnsleys_fern ...");
                final IFSAccumulator ifsAccumulator = IFS.BARNSLEYS_FERN.compute(rectangle, width, height, 150);

                System.out.println("Saving image barnsleys_fern ...");
                writeImage(ifsAccumulator, "barnsleys_fern");
            }
            break;
        }
    }

    private static void writeImage(final IFSAccumulator ifsAccumulator, final String fileName) {

        final int width = ifsAccumulator.width();
        final int height = ifsAccumulator.height();

        try (final PrintStream outputStream = new PrintStream("res/" + fileName + ".pbm")) {
            outputStream.println("P1");
            outputStream.printf("%d %d", width, height);
            for (int j = height - 1; j >= 0; --j) {
                for (int i = 0; i < width; ++i) {
                    outputStream.print(ifsAccumulator.isHit(i, j) ? 1 : 0);
                }
                outputStream.println();
            }
        } catch (final FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }
}
