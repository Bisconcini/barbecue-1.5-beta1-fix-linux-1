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

package net.sourceforge.barbecue.linear.codabar;

import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BlankModule;
import net.sourceforge.barbecue.Module;
import net.sourceforge.barbecue.SeparatorModule;
import net.sourceforge.barbecue.linear.LinearBarcode;

import java.util.ArrayList;
import java.util.List;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * This is a concrete implementation of the Codabar barcode, AKA USD-4,
 * Monarch, NW-7 and 2of7. This implementation imposes no restrictions
 * on your choice of start and stop characters, so you will need to check
 * that you are using chars acceptable to your barcode scanner. This
 * implementation does support the traditional a, b, c, d, e, t, n and * start/stop chars.
 * Omitting the start/stop chars from your data will cause the barcode to use
 * the default start/stops chars, A and C, respectively.
 * <br/>
 * This implementation provides no support for check digits (as they are not
 * included in the Codabar specification). However, many uses of Codabars
 * mandate the use of a check digit, and the algorithms used vary from
 * application to application. The most common algorithm is Mod-16. If you wish
 * to implement a check digit in your Codabar barcode, you must calculate it
 * yourself and insert it into the data to be encoded before the Stop char (or at
 * the end of the data if you are letting barbecue insert start and stop chars for
 * you).
 *
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 */
public class CodabarBarcode extends LinearBarcode {
    /** The default codabar start character */
    public static final String DEFAULT_START = "A";
    /** The default codabar stop character */
    public static final String DEFAULT_STOP = "C";
    
    private String label;
    
    /**
     * Constructs a new Codabar barcode with thte specified data.
     * @param data The data to encode
     * @throws BarcodeException If the data is invalid
     */
    public CodabarBarcode(String data) throws BarcodeException {
        super(data);
        this.label = data;
        validateData();
    }
    
    /**
     * Returns the text label to be displayed underneath the barcode.
     * @return The barcode label
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * Encodes the data of the barcode into bars.
     * @return The encoded bar data
     */
    protected Module[] encodeData() {
        List modules = new ArrayList();
        for (int i = 0; i < data.length(); i++) {
            if (i > 0) {
                modules.add(new SeparatorModule(1));
            }
            char c = data.charAt(i);
            Module module = ModuleFactory.getModule(String.valueOf(c));
            modules.add(module);
        }
        return (Module[]) modules.toArray(new Module[0]);
    }
    
    /**
     * Calculates the check sum digit for the barcode.
     * @return Null - Codabar has no checksum
     */
    protected Module calculateChecksum() {
        return null; // No checksum - return null
    }
    
    /**
     * Returns the pre-amble for the barcode.
     * @return A BlankModule
     */
    protected Module getPreAmble() {
        if(drawingQuietSection) {
            return new BlankModule(0);
        } else {
            return null;
        }
    }
    
    /**
     * Returns the post-amble for the barcode.
     * @return A BlankModule
     */
    protected Module getPostAmble() {
        if(drawingQuietSection) {
            return new BlankModule(0);
        } else {
            return null;
        }
    }
    
    private void validateData() throws BarcodeException {
        replaceTraditionalStartStopWithModern();
        addDefaultStartStopIfRequired();
        int index = 0;
        StringBuffer buf = new StringBuffer();
        StringCharacterIterator iter = new StringCharacterIterator(data);
        for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
            if (!Character.isWhitespace(c)) {
                if (!ModuleFactory.isValid(String.valueOf(c))) {
                    throw new BarcodeException(c
                    + " is not a valid character for Codabar encoding");
                }
                checkStartStop(c, index);
                buf.append(c);
            }
            index += 1;
        }
        data = buf.toString();
    }
    
    private void addDefaultStartStopIfRequired() {
        StringBuffer newData = new StringBuffer();
        if (!Character.isLetter(data.charAt(0))) {
            newData.append(DEFAULT_START);
        }
        newData.append(data);
        if (!Character.isLetter(data.charAt(data.length() - 1))) {
            newData.append(DEFAULT_STOP);
        }
        data = newData.toString();
    }
    
    private void replaceTraditionalStartStopWithModern() {
        data = data.replace('a', 'A');
        data = data.replace('t', 'A');
        data = data.replace('b', 'B');
        data = data.replace('n', 'B');
        data = data.replace('c', 'C');
        data = data.replace('*', 'C');
        data = data.replace('d', 'D');
        data = data.replace('e', 'D');
    }
    
    private void checkStartStop(char c, int index) throws BarcodeException {
        if (Character.isLetter(c) && index > 0 && index < data.length() - 1) {
            throw new BarcodeException(c
            + " is only allowed as the first and last characters for Codabar barcodes");
        }
    }
}
