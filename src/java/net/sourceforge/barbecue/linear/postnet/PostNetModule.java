package net.sourceforge.barbecue.linear.postnet;

import net.sourceforge.barbecue.Module;
import net.sourceforge.barbecue.output.AbstractOutput;
import net.sourceforge.barbecue.output.OutputException;

/**
 * @author Brendon Anderson
 */
public class PostNetModule extends Module {

    protected final static double BARWIDTH = 3.0;
    protected final static double BLANKWIDTH = 4.0;

    public PostNetModule(int[] bars) {
        super(bars);
    }

    protected double draw(AbstractOutput output, double x, double y) throws OutputException {
        double sum = 0;
        double fullheight = PostNetBarcode.HEIGHT;
        double halfheight = fullheight * .4;

        for (int i = 0; i < bars.length; i++) {
            int bar = bars[i];
            if (bar == 0) {
                output.drawBar((int) x, (int) (y + (fullheight - halfheight)), (int) BARWIDTH, (int) halfheight, true);
            } else {
                output.drawBar((int) x, (int) y, (int) BARWIDTH, (int) fullheight, true);
            }
            sum += BARWIDTH;
            x += BARWIDTH;
            output.drawBar((int) x, (int) y, (int) BLANKWIDTH, (int) fullheight, false);
            sum += BLANKWIDTH;
            x += BLANKWIDTH;
            output.drawBar((int) x, (int) y, (int) BLANKWIDTH, (int) fullheight, false);
        }
        return sum;
    }
}












