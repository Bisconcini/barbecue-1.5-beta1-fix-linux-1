/***********************************************************************************************************************
 * Copyright (c) 2004, International Barcode Consortium
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 * Neither the name of the International Barcode Consortium nor the names of any contributors may be used to endorse
 * or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 ***********************************************************************************************************************/

package net.sourceforge.barbecue.linear.ean;

import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.Module;
import net.sourceforge.barbecue.linear.upc.UPCABarcode;
import net.sourceforge.barbecue.output.LabelLayoutFactory;
import net.sourceforge.barbecue.output.Output;
import net.sourceforge.barbecue.output.OutputException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a concrete implementation of the EAN13 barcode.
 *
 * @author <a href="mailto:james@metalskin.com">James Jenner</a>
 */
public class EAN13Barcode extends UPCABarcode {
    /**
     * A list of type identifiers for the EAN13 barcode format
     */
    public static final String[] TYPES = new String[]{
        "EAN-13", "EAN13"
    };

    public final static int LEFT_ODD = 0;
    public final static int LEFT_EVEN = 1;
    public final static int RIGHT = 2;

    public final static int PARITY_EVEN = 0;
    public final static int PARITY_ODD = 1;

    public final static int BARCODE_LENGTH = 12;

    public final static String ISBN_NUMBER_SYSTEM = "978";
    public final static int ISBN_SIZE = 10;

    /**
     * Constructs a basic mode EAN13 barcode with the specified data and an optional
     * checksum.  The length of the barcode is 11, 12 with a checksum.  If the length
     * passed is only 11, then a checksum will be automaticaly added.  If the length
     * is not 11 or 12 then a barcode exception will be thrown.
     *
     * @param data The data to encode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public EAN13Barcode(String data) throws BarcodeException {
        super(data);
    }

    protected int getBarcodeLength() {
        return BARCODE_LENGTH;
    }

    protected int getGuardCharSize() {
        return EAN13ModuleFactory.GUARD_CHAR_SIZE;
    }

    protected int getLeftWidth() {
        return EAN13ModuleFactory.LEFT_WIDTH;
    }

    protected Module getRightMargin() {
        return EAN13ModuleFactory.RIGHT_MARGIN;
    }

    /* TODO: The following is very close to the parent (UPCABarcode), 
     * should change parent so can handle both.  
     * 
     * note that the following code uses member functions to get static 
     * values from the ModuleFactory class, instead of referencing the 
     * values directly.  This is so sub-classes can override the 
     * member functions and thus change the static values.
     */
    protected Dimension draw(Output output, int x, int y, int barWidth, int barHeight) throws OutputException {
        int currentX = x;

        output.beginDraw();
        
        // need to change the output.barHeight value, appears to be no means to do so
        int guardBarHeight = 0;
        int shortBarHeight = barHeight;
        int textHeight = 10 * barWidth;

        if (drawingText) {
            shortBarHeight = barHeight - (11 * barWidth);
            guardBarHeight = shortBarHeight + (6 * barWidth);
        } else {
            shortBarHeight = barHeight - (6 * barWidth);
            guardBarHeight = barHeight;
        }

        String text = getLabel();
        int currentY = this.barHeight + y;

        Module[] modules = encodeData();

        String leadChar = String.valueOf(text.charAt(0));
        String firstSet = text.substring(1, getLeftWidth());
        String lastSet = text.substring(getLeftWidth());

        if (requiresChecksum) {
            lastSet = lastSet + calculateChecksum().getSymbol();
        }

        int startTextPos = 0;
        int firstTextPos = 0;
        int secondTextPos = 0;

        int startTextW = x;
        int firstTextW = 0;
        int secondTextW = 0;
        int width = 0;
        Module preAmble = getPreAmble();
        Module postAmble = getPostAmble();
        startTextW = 0;
        
        // draw leading white space
        if (super.drawingQuietSection) {
            currentX += drawModule(getLeftMargin(), output, currentX, y, barWidth, shortBarHeight + textHeight);
        }
        startTextPos = x;
        startTextW = currentX - startTextPos;
        width = currentX;
        int guardCharSize = getGuardCharSize();
        int leftWidth = getLeftWidth() - 1;
        
        // draw the left guard
        if (preAmble != null) {
            currentX += drawModule(preAmble, output, currentX, y, barWidth, guardBarHeight);
        }
        
        // draw first char in left side
        for (int i = 0; i < guardCharSize; i++) {
            currentX += drawModule(modules[0], output, currentX, y, barWidth, guardBarHeight);
        }
        firstTextPos = currentX;
        
        // draw the blank space below the guard
        width = currentX - width;
        output.paintBackground(currentX - width, guardBarHeight + currentY, width, ((shortBarHeight + textHeight) - guardBarHeight));

        for (int i = guardCharSize; i < leftWidth; i++) {
            currentX += drawModule(modules[i], output, currentX, y, barWidth, shortBarHeight);
        }

        firstTextW = currentX - firstTextPos;

        width = currentX;
        // draw the centre guard
        currentX += drawModule(getCentreGuard(), output, currentX, y, barWidth, guardBarHeight);
        secondTextPos = currentX;
        
        // draw the blank space below the guard
        width = currentX - width;
        output.paintBackground(currentX - width, guardBarHeight + currentY, width, ((shortBarHeight + textHeight) - guardBarHeight));

        for (int i = leftWidth; i < modules.length; i++) {
            currentX += drawModule(modules[i], output, currentX, y, barWidth, shortBarHeight);
        }

        secondTextW = currentX - secondTextPos;
        width = currentX;
        
        // draw the right guard
        if (postAmble != null) {
            currentX += drawModule(postAmble, output, currentX, y, barWidth, guardBarHeight);
        }
        
        // draw the blank space below the guard
        width = currentX - width;
        output.paintBackground(currentX - width, guardBarHeight + currentY, width, ((shortBarHeight + textHeight) - guardBarHeight));
        
        // draw trailing white space
        currentX += drawModule(getRightMargin(), output, currentX, y, barWidth, shortBarHeight + textHeight);

        if (drawingText) {
            output.drawText(leadChar, LabelLayoutFactory.createMarginLayout(startTextPos, shortBarHeight, startTextW, textHeight));
            output.drawText(firstSet, LabelLayoutFactory.createMarginLayout(firstTextPos, shortBarHeight, firstTextW, textHeight));
            output.drawText(lastSet, LabelLayoutFactory.createMarginLayout(secondTextPos, shortBarHeight, secondTextW, textHeight));
        }

        Dimension size = new Dimension((int) (currentX - x), (int) (currentY) - y);

        output.endDraw((int) size.getWidth(), (int) size.getHeight());

        return size;
    }


    /**
     * Returns the encoded data for the barcode.
     *
     * @return An array of modules that represent the data as a barcode
     */
    protected Module[] encodeData() {
        List modules = new ArrayList();
        Module module = null;
        int len = data.length();
        char c;

        String firstChar = data.substring(0, 1);
        
        /* EAN13 is a funny one.  The first digit actualy isn't 
         * encoded, even though its displayed on the human readable.
         *
         * go figure.
         */
        for (int i = 1; i < len; i++) {
            c = data.charAt(i);

            module = EAN13ModuleFactory.getModule(firstChar, String.valueOf(c), i);
            width += module.widthInBars();
            modules.add(module);
        }

        if (requiresChecksum) {
            module = EAN13ModuleFactory.getModule(firstChar, calculateChecksum().getSymbol(), modules.size() - 1);
            width += module.widthInBars();
            modules.add(module);
        }

        return (Module[]) modules.toArray(new Module[0]);
    }

    /**
     * Returns the checksum for the barcode, pre-encoded as a Module.
     *
     * @return a Mod-10 caclulated checksum, if no checksum is required Null
     */
    protected Module calculateChecksum() {
        if (requiresChecksum) {
            return EAN13ModuleFactory.getModuleForIndex(data.substring(0, 1), getMod10CheckDigit(data));
        }

        return null;
    }

    public static int getMod10CheckDigit(String data) {
        int sum = 0;
        int len = data.length();
        int value;
        
        /*
         * note that the for loop is from 0, as indexing for data is from 0,
         * but the modolo 10 considers the first character position to be 1.
         * in UPCA 0 is odd, as the first position (being 1) is considered 
         * an odd number.  However EAN13 is one extra character in size. To
         * be backward compatible EAN13 has a '0' added to the front for 
         * UPCA numbers and as such the first position is now even.
         * So the following code must compare against 0, not 1 as is in
         * the UPCA getMod10CheckDigit code.
         */
        for (int i = 0; i < len; i++) {
            try {
                value = Integer.parseInt(String.valueOf(data.charAt(i)));
                sum += calculateChecksum(value, (i % 2) == 0);
            } catch (java.lang.NumberFormatException e) {
            }
        }

        return 10 - (sum % 10);
    }
}
