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

package net.sourceforge.barbecue.linear.twoOfFive;

import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BlankModule;
import net.sourceforge.barbecue.Module;
import net.sourceforge.barbecue.Modulo10;
import net.sourceforge.barbecue.SeparatorModule;
import net.sourceforge.barbecue.linear.LinearBarcode;

import java.util.ArrayList;
import java.util.List;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * This is a concrete implementation of the Standard 2 of 5 barcode
 * 
 * The Standard 2 of 5 barcode requires an even number of characters. At
 * the same time, a modulo 10 check digit can be used.  The default constructor
 * presumes that if the barcode is of an odd length, then a check digit is 
 * required.  This will automaticaly be generated.
 * If the constructor with the check digit flag is used, the check digit flag is
 * flase and the length of the barcode is odd, then an exception will be thrown.
 *
 * @author <a href="mailto:james@metalskin.com">James Jenner</a>
 */
public class Std2of5Barcode extends LinearBarcode {
    private String label;
    
    /**
     * Constructs a new Standard 2 of 5 barcode with the specified data.
     * No check digit will be added
     * @param data The data to encode
     * @throws BarcodeException If the data is invalid
     */
    public Std2of5Barcode(String data) throws BarcodeException {
        this(data, false);
    }
    
    /**
     * Constructs a new Standard 2 of 5 barcode with thte specified data.
     * @param data The data to encode
     * @param checkDigit if true then a check digit is automaticaly appened to data
     * @throws BarcodeException If the data is invalid
     */
    public Std2of5Barcode(String data, boolean checkDigit) throws BarcodeException {
        super(checkDigit ? data + Modulo10.getMod10CheckDigit(data, 3) : data);
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
        
        for(int i = 0; i < data.length(); i++) {
            Module module = Std2of5ModuleFactory.getModule(String.valueOf(data.charAt(i)));
            
            modules.add(module);
        }
        
        return (Module[])modules.toArray(new Module[0]);
    }
    
    /**
     * Calculates the check sum digit for the barcode.
     * @return Null - Standard 2 of 5 has no builtin checksum
     */
    protected Module calculateChecksum() {
        return null; // No checksum - return null
    }
    
    /**
     * Returns the pre-amble for the barcode.
     * @return A BlankModule
     */
    protected Module getPreAmble() {
        return Std2of5ModuleFactory.START_CHAR;
    }
    
    /**
     * Returns the post-amble for the barcode.
     * @return A BlankModule
     */
    protected Module getPostAmble() {
        return Std2of5ModuleFactory.END_CHAR;
    }
    
    protected void validateData() throws BarcodeException {
        int index = 0;
        StringBuffer buf = new StringBuffer();
        StringCharacterIterator iter = new StringCharacterIterator(data);
        for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
            if (!Character.isWhitespace(c)) {
                if (!Std2of5ModuleFactory.isValid(String.valueOf(c))) {
                    throw new BarcodeException(c
                    + " is not a valid character for Standard 2 of 5 encoding");
                }
                buf.append(c);
            }
            index += 1;
        }
        data = buf.toString();
    }
}
