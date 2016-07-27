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

package net.sourceforge.barbecue.linear.upc;

import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.Module;
import net.sourceforge.barbecue.linear.LinearBarcode;
import net.sourceforge.barbecue.output.LabelLayoutFactory;
import net.sourceforge.barbecue.output.Output;
import net.sourceforge.barbecue.output.OutputException;

import java.awt.*;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a concrete implementation of the UPC-A barcode.
 *
 * @author <a href="mailto:james@metalskin.com">James Jenner</a>
 */
public class UPCABarcode extends LinearBarcode {
    /**
     * A list of type identifiers for the UPC-A barcode format
     */
    public static final String[] TYPES = new String[]{
        "UPC-A", "UPCA"
    };
    protected boolean requiresChecksum = false;
    protected final String label;
    protected int width = 0;

    protected final static int CHECKSUM_WEIGHT_EVEN = 1;
    protected final static int CHECKSUM_WEIGHT_ODD = 3;

    public final static int BARCODE_LENGTH = 11;

    /**
     * Constructs a basic mode UPC-A barcode with the specified data and an optional
     * checksum.  The length of the barcode is 11, 12 with a checksum.  If the length
     * passed is only 11, then a checksum will be automaticaly added.  If the length
     * is not 11 or 12 then a barcode exception will be thrown.
     *
     * @param data The data to encode
     * @throws net.sourceforge.barbecue.BarcodeException
     *          If the data to be encoded is invalid
     */
    public UPCABarcode(String data) throws BarcodeException {
        this(data, false);
    }

    public UPCABarcode(String data, boolean randomWeight) throws BarcodeException {
        super(validateChars(data));

        if (data.length() != getBarcodeLength()) {
            throw new BarcodeException("Invalid data length");
        }

        requiresChecksum = true;
        if (randomWeight) {
            data = populateRandonWeightCheckDigit(data);
        }
        this.label = data;
    }

    protected int getBarcodeLength() {
        return BARCODE_LENGTH;
    }

    /* TODO: The following is very close to EAN13Barcode's version,
     * should change this so EAN13Barcode doesn't need to override.  
     * 
     * Note that the following code uses member functions to get static 
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
        String endChar = String.valueOf(text.charAt(text.length() - 1));
        String firstSet = text.substring(1, getLeftWidth());
        String lastSet = text.substring(getLeftWidth(), 11);

        if (requiresChecksum) {
            endChar = calculateChecksum().getSymbol();
        }

        int startTextPos = 0;
        int firstTextPos = 0;
        int secondTextPos = 0;
        int endTextPos = 0;

        int startTextW = x;
        int firstTextW = 0;
        int secondTextW = 0;
        int endTextW = 0;
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
        int leftWidth = getLeftWidth();
        
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
        output.paintBackground(currentX - width, guardBarHeight, width, ((shortBarHeight + textHeight) - guardBarHeight));

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
        output.paintBackground(currentX - width, guardBarHeight, width, ((shortBarHeight + textHeight) - guardBarHeight));

        int endGuardOffset = modules.length - guardCharSize;

        for (int i = leftWidth; i < endGuardOffset; i++) {
            currentX += drawModule(modules[i], output, currentX, y, barWidth, shortBarHeight);
        }

        secondTextW = currentX - secondTextPos;
        width = currentX;
        for (int i = endGuardOffset; i < modules.length; i++) {
            currentX += drawModule(modules[i], output, currentX, y, barWidth, guardBarHeight);
        }
        
        // draw the right guard
        if (postAmble != null) {
            currentX += drawModule(postAmble, output, currentX, y, barWidth, guardBarHeight);
        }
        
        // draw the blank space below the guard
        width = currentX - width;
        output.paintBackground(currentX - width, guardBarHeight, width, ((shortBarHeight + textHeight) - guardBarHeight));

        endTextPos = currentX;

        // draw trailing white space
        if (super.drawingQuietSection) {
            currentX += drawModule(getRightMargin(), output, currentX, y, barWidth, shortBarHeight + textHeight);
        }

        endTextW = currentX - endTextPos;

        if (drawingText) {
            output.drawText(leadChar, LabelLayoutFactory.createMarginLayout(startTextPos, shortBarHeight, startTextW, textHeight));
            output.drawText(firstSet, LabelLayoutFactory.createMarginLayout(firstTextPos, shortBarHeight, firstTextW, textHeight));
            output.drawText(lastSet, LabelLayoutFactory.createMarginLayout(secondTextPos, shortBarHeight, secondTextW, textHeight));
            output.drawText(endChar, LabelLayoutFactory.createMarginLayout(endTextPos, shortBarHeight, endTextW, textHeight));
        }


        Dimension size = new Dimension((int) (currentX - x), (int) (currentY) - y);

        output.endDraw((int) size.getWidth(), (int) size.getHeight());

        return size;
    }

    /**
     * Returns the text that will be displayed underneath the barcode (if requested).
     *
     * @return The text label for the barcode
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the barcode width for the given resolution.
     *
     * @param resolution The output resolution
     * @return The barcode width
     */
    protected double getBarcodeWidth(int resolution) {
        encodeData();

        return barWidth * width;
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
        for (int i = 0; i < len; i++) {
            c = data.charAt(i);

            module = ModuleFactory.getModule(String.valueOf(c), i);
            width += module.widthInBars();
            modules.add(module);
        }

        if (requiresChecksum) {
            module = ModuleFactory.getModule(calculateChecksum().getSymbol(), modules.size() - 1);
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
            return ModuleFactory.getModuleForIndex(getMod10CheckDigit(data));
        }

        return null;
    }

    protected int getGuardCharSize() {
        return ModuleFactory.GUARD_CHAR_SIZE;
    }

    protected int getLeftWidth() {
        return ModuleFactory.LEFT_WIDTH;
    }

    protected Module getLeftMargin() {
        return ModuleFactory.LEFT_MARGIN;

    }

    protected Module getRightMargin() {
        return ModuleFactory.RIGHT_MARGIN;
    }

    /**
     * Returns the pre-amble for the barcode.
     *
     * @return pre amble for the barcode
     */
    protected Module getPreAmble() {
        return ModuleFactory.LEFT_GUARD;
    }

    /**
     * Returns the middle bar for the barcode.
     *
     * @return pre amble for the barcode
     */
    protected Module getCentreGuard() {
        return ModuleFactory.CENTRE_GUARD;
    }

    /**
     * Returns the post-amble for the barcode.
     *
     * @return postamble for the barcode
     */
    protected Module getPostAmble() {
        return ModuleFactory.RIGHT_GUARD;
    }

    public static int getMod10CheckDigit(String data) {
        int sum = 0;
        int len = data.length();
        int value;
        
        /*
         * note that the for loop is from 0, as indexing for data is from 0,
         * but the modolo 10 considers the first character position to be 1.
         * as such 0 is odd, not even and 1 is even not odd, so compare to
         * 1, not 0 when attempting to find if its an even or odd number.
         */
        for (int i = 0; i < len; i++) {
            try {
                value = Integer.parseInt(String.valueOf(data.charAt(i)));
                sum += calculateChecksum(value, (i % 2) == 1);
            } catch (java.lang.NumberFormatException e) {
            }
        }

        int checkDigit = 10 - (sum % 10);

        if (checkDigit == 10) {
            checkDigit = 0;
        }

        return checkDigit;
    }

    protected static int calculateChecksum(int value, boolean even) {
        if (even) {
            return value * CHECKSUM_WEIGHT_EVEN;
        } else {
            return value * CHECKSUM_WEIGHT_ODD;
        }
    }

    private static String validateChars(String data) throws BarcodeException {
        if (data == null) {
            throw new IllegalArgumentException("data param must contain a value, not null");
        }

        StringCharacterIterator iter = new StringCharacterIterator(data);
        for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
            if (!ModuleFactory.hasModule(String.valueOf(c))) {
                throw new BarcodeException("Illegal character");
            }
        }

        return data;
    }

    private String populateRandonWeightCheckDigit(String upc) {
        int[][] checkDigitCalcs = {{0, 2, 4, 6, 8, 9, 1, 3, 5, 7}, {0, 3, 6, 9, 2, 5, 8, 1, 4, 7},
                                   {0, 5, 9, 4, 8, 3, 7, 2, 6, 1}};
        int total = 0;
        int checkdigit = 0;
        char[] upcCharArray = upc.toCharArray();

        int digit = Character.digit(upcCharArray[7], 10);
        total += checkDigitCalcs[0][digit];
        digit = Character.digit(upcCharArray[8], 10);
        total += checkDigitCalcs[0][digit];

        /*
         * calculation changes for the 9th and 10th digits which is why we are
         * not using a loop
         */
        digit = Character.digit(upcCharArray[9], 10);
        total += checkDigitCalcs[1][digit];
        digit = Character.digit(upcCharArray[10], 10);
        total += checkDigitCalcs[2][digit];

        if ((total % 10) == 0) {
            checkdigit = 0;
        } else {
            checkdigit = (10 - (total % 10));
        }

        upcCharArray[6] = String.valueOf(checkdigit).charAt(0);
        return String.valueOf(upcCharArray);
    }
}
