package net.sourceforge.barbecue.linear;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.Module;
import net.sourceforge.barbecue.output.LabelLayoutFactory;
import net.sourceforge.barbecue.output.Output;
import net.sourceforge.barbecue.output.OutputException;

import java.awt.*;

public abstract class LinearBarcode extends Barcode {

    protected LinearBarcode(String data) throws BarcodeException {
        super(data);
    }

    protected Dimension draw(Output output, int x, int y, int barWidth, int barHeight) throws OutputException {
        int currentX = x;
        Module preAmble = getPreAmble();
        Module postAmble = getPostAmble();
        output.beginDraw();

        if (preAmble != null) {
            currentX += drawModule(getPreAmble(), output, currentX, y, barWidth, barHeight);
        }

        Module[] modules = encodeData();
        for (int i = 0; i < modules.length; i++) {
            Module module = modules[i];
            currentX += drawModule(module, output, currentX, y, barWidth, barHeight);
        }

        currentX += drawModule(calculateChecksum(), output, currentX, y, barWidth, barHeight);

        if (postAmble != null) {
            currentX += drawModule(postAmble, output, currentX, y, barWidth, barHeight);
        }

        int currentY = this.barHeight + y;

        if (drawingText) {
            currentY += drawTextLabel(output, x, currentY, currentX);
        }

        Dimension size = new Dimension(currentX - x, currentY - y);
        output.endDraw((int) size.getWidth(), (int) size.getHeight());
        return size;
    }

    protected int drawTextLabel(Output params, int x, int y, int width) throws OutputException {
        return params.drawText(getLabel(), LabelLayoutFactory.createCenteredLayout(x, y, width));
    }
}
