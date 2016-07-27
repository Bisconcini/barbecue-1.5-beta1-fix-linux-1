/***********************************************************************************************************************
 * Copyright (c) 2003, International Barcode Consortium
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

package net.sourceforge.barbecue.linear.code128;

import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BlankModule;
import net.sourceforge.barbecue.CompositeModule;
import net.sourceforge.barbecue.Module;
import net.sourceforge.barbecue.linear.LinearBarcode;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a concrete implementation of the Code 128 barcode. It fully supports all three
 * available character sets (A, B and C), and also fully supports code shifts and set
 * changes on-the-fly, providing an automatic optimisation mode.
 *
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 */
public class Code128Barcode extends LinearBarcode {
    /** Character set A flag */
    public static final int A = 0;
    /** Character set B flag */
    public static final int B = 1;
    /** Character set c flag */
    public static final int C = 2;
    /** Auto character set flag */
    public static final int O = 3;
    
    /** Code shift character */
    public static final String SHIFT = "\306";
    /** Code set change from current to A character */
    public static final String CHANGE_TO_A = "\311";
    /** Code set change from current to B character */
    public static final String CHANGE_TO_B = "\310";
    /** Code set change from current to c character */
    public static final String CHANGE_TO_C = "\307";
    
    /** FNC1 start character */
    public static final String FNC_1 = "\312";
    
    public static final Module START_A = new Module(new int[] {2, 1, 1, 4, 1, 2});
    public static final Module START_B = new Module(new int[] {2, 1, 1, 2, 1, 4});
    public static final Module START_C = new Module(new int[] {2, 1, 1, 2, 3, 2});
    protected static final Module STOP = new Module(new int[] {2, 3, 3, 1, 1, 1, 2});
    protected static final Module QUIET_SECTION = new BlankModule(10);
    
    private static final Module[] START = {
        START_A,
        START_B,
        START_C
    };
    
    protected static final int[] START_INDICES = {
        103, 104, 105
    };
    
    protected static final int[] BUF_SIZES = {
        1, 1, 2
    };
    
    protected int startIndex;
    protected int mode;
    private int startingMode;
    private boolean shiftNext;
    private boolean shifted;
    private CharBuffer buf;
    private Module checkDigit;
    private boolean optimising;
    private Accumulator sum;
    private Accumulator index;
    
    /**
     * Create a new Code 128 barcode using character set B.
     * @param data The data to encode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public Code128Barcode(String data) throws BarcodeException {
        this(data, O);
    }
    
    /**
     * Creates a new Coded 128 barcode with the specified data and the specified
     * character set.
     * @param data The data to encode
     * @param mode The character set to use for encoding
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public Code128Barcode(String data, int mode) throws BarcodeException {
        super(data);
        if (mode == O) {
            optimising = true;
            this.mode = B;
        } else {
            optimising = false;
            this.mode = mode;
        }
        this.startingMode = this.mode;
        this.shiftNext = false;
        this.shifted = false;
        this.startIndex = START_INDICES[this.mode];
    }
    
    /**
     * Returns the current character set being used in this barcode.
     * @return The flag indicating the current character set
     */
    public int getCharacterSet() {
        return startingMode;
    }
    
    /**
     * Returns the text label to be displayed underneath the barcode/
     * @return The text label for the barcode
     */
    public String getLabel() {
        if(label != null) {
            return label;
        } else {
            return data;
        }
    }
    
    /**
     * Returns the width of the encoded symbol portion of the barcode in pixels for
     * the given resolution.
     * @param resolution The resolution to calculate the width for
     * @return The width of the encoded portion of the barcode
     */
    protected double getSymbolWidth(int resolution) {
        //		L = (11C + 35)X (alphanumeric) L = (5.5C + 35)X (numeric only using Code C)
        //		where
        //		L = length of symbol (not counting quiet zone)
        //		C = number of data characters, code characters and shift characters
        //		(do not include start, stop or checksum. They are automatically added in.)
        //		X = X-dimension
        double barWidthMM = convertToMillimetres(barWidth, resolution);
        double multiplier = 11;
        if (startingMode == C) {
            multiplier = 5.5;
        }
        return (multiplier * data.length() + 35) * barWidthMM;
    }
    
    /**
     * Calculates the minimum allowed barcode height for the barcode. The height must
     * be at least .15 times the length of the symbol (excluding quiet zones) and .25
     * inches (whichever is larger).
     * @param resolution The output resolution (for calculating the width)
     * @return The minimum height
     */
    protected int calculateMinimumBarHeight(int resolution) {
        // The height of the bars must be at least .15 times the symbol's length or .25 inches,
        // whichever is larger
        double point25Inches = resolution * 0.25;
        // TODO: Need to get rid of this and do it in the output class
        return (int) Math.max((0.15 * getSymbolWidth(resolution)), point25Inches);
    }
    
    /**
     * Encodes the data of the barcode into bars.
     * @return The encoded bar data
     */
    public Module[] encodeData() {
        // We are calculating the check digit as we encode - this will ensure that it is
        // calculated correctly, even with code changes to char set C
        sum = new Accumulator(startIndex);
        List modules = new ArrayList();
        buf = new CharBuffer(BUF_SIZES[mode]);
        index = new Accumulator(1);
        padDataToEvenLength();
        
        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            if (optimising && startingMode == B) {
                if (i + 1 < data.length() && isControl(c) && mode != A) {
                    if (mode == B) {
                        encode(modules, SHIFT);
                    } else {
                        encode(modules, CHANGE_TO_A);
                    }
                } else if (i + 3 < data.length() && digitGroupIsNext(i, data) && mode != C) {
                    encode(modules, CHANGE_TO_C);
                } else if (i + 1 <= data.length() && digitGroupEndIsNext(i, data)
                && mode == C && buf.size() != 1) {
                    encode(modules, CHANGE_TO_B);
                }
            }
            
            if (isShiftOrCode(c)) {
                encode(modules, String.valueOf(c));
                buf.clear();
            } else {
                buf.addChar(c);
                if (buf.isFull()) {
                    encode(modules, buf.toString());
                    buf.clear();
                }
            }
        }
        
        checkDigit = ModuleFactory.getModuleForIndex(sum.getValue() % 103, mode);
        mode = startingMode;
        return (Module[]) modules.toArray(new Module[0]);
    }
    
    private boolean isShiftOrCode(char c) {
        String s = String.valueOf(c);
        return (s.equals(SHIFT)
        || s.equals(CHANGE_TO_A)
        || s.equals(CHANGE_TO_B)
        || s.equals(CHANGE_TO_C)
        || s.equals(FNC_1));
    }
    
    /**
     * Calculates the check sum digit for the barcode.
     * @return The check sum digit
     */
    public Module calculateChecksum() {
        if (checkDigit == null) {
            encodeData();
        }
        return checkDigit;
    }
    
    /**
     * Returns the pre-amble for the barcode. This is a combination of a
     * quiet section and the start character for the character set that the barcode
     * was constructed with.
     * @return The pre-amble
     */
    protected Module getPreAmble() {
        CompositeModule module = new CompositeModule();
        if(drawingQuietSection) {
            module.add(QUIET_SECTION);
        }
        module.add(START[mode]);
        return module;
    }
    
    /**
     * Returns the post amble for the barcode. This is the combination
     * of the stop character anda quiet section.
     * @return The post amble
     */
    protected Module getPostAmble() {
        CompositeModule module = new CompositeModule();
        module.add(STOP);
        if(drawingQuietSection) {
            module.add(QUIET_SECTION);
        }
        return module;
    }
    
    private boolean isControl(char c) {
        return Character.isISOControl(c);
    }
    
    private boolean digitGroupIsNext(int index, String chars) {
        char c1 = chars.charAt(index);
        char c2 = chars.charAt(index + 1);
        char c3 = chars.charAt(index + 2);
        char c4 = chars.charAt(index + 3);
        return (Character.isDigit(c1)
        && Character.isDigit(c2)
        && Character.isDigit(c3)
        && Character.isDigit(c4));
    }
    
    private boolean digitGroupEndIsNext(int index, String chars) {
        if (index == chars.length() - 1) {
            return true;
        }
        char c1 = chars.charAt(index);
        char c2 = chars.charAt(index + 1);
        return ((Character.isDigit(c1) && (!Character.isDigit(c2)))
        || (!Character.isDigit(c1)));
    }
    
    /**
     * Pads the data to be encoded to an even length by prepending "0" characters.
     * This is only valid for pure character set C barcodes.
     */
    private void padDataToEvenLength() {
        // Only for Code C
        if (startingMode == C && data.length() % 2 != 0 && !containsShiftOrChange(data)) {
            data = '0' + data;
        }
    }
    
    private boolean containsShiftOrChange(String data) {
        return ((data.indexOf(CHANGE_TO_A) >= 0)
        || (data.indexOf(CHANGE_TO_B) >= 0)
        || (data.indexOf(CHANGE_TO_C) >= 0)
        || (data.indexOf(SHIFT) >= 0));
    }
    
    private void clearShift() {
        if (shifted) {
            shifted = false;
            shiftNext = false;
            mode = shiftMode();
        }
    }
    
    private void checkShift(Module module) {
        if (module instanceof ShiftModule) {
            mode = shiftMode();
            shiftNext = true;
        } else if (shiftNext) {
            shifted = true;
        }
    }
    
    private int shiftMode() {
        if (mode == A) {
            return B;
        } else {
            return A;
        }
    }
    
    private void checkCodeChange(Module module) {
        if (module instanceof CodeChangeModule) {
            mode = ((CodeChangeModule) module).getCode();
            buf = new CharBuffer(BUF_SIZES[mode]);
        }
    }
    
    private void encode(List modules, String data) {
        Module module = ModuleFactory.getModule(data, mode);
        updateCheckSum(data);
        checkShift(module);
        checkCodeChange(module);
        modules.add(module);
        clearShift();
    }
    
    private void updateCheckSum(String data) {
        int code = ModuleFactory.getIndex(data, mode);
        sum.add(code * index.getValue());
        index.increment();
    }
    
    private double convertToMillimetres(double barWidth, int resolution) {
        //25.4 mm in 1 inch
        double pixelsPerMM = resolution / 25.4;
        return barWidth / pixelsPerMM;
    }
}
