/***********************************************************************************************************************
 Copyright (c) 2003, International Barcode Consortium
 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification,
 are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this list of
 conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of
 conditions and the following disclaimer in the documentation and/or other materials
 provided with the distribution.
 * Neither the name of the International Barcode Consortium nor the names of any contributors may be used to endorse
 or promote products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.
 ***********************************************************************************************************************/

package net.sourceforge.barbecue.linear.code39;

import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.CompositeModule;
import net.sourceforge.barbecue.Module;
import net.sourceforge.barbecue.SeparatorModule;
import net.sourceforge.barbecue.linear.LinearBarcode;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a concrete implementation of the Code 39 barcode, AKA 3of9,
 * USD-3.
 *
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 */
public class Code39Barcode extends LinearBarcode {
    /**
     * A list of type identifiers for the Code39 barcode format
     */
    public static final String[] TYPES = new String[]{
        "Code39", "USD3", "3of9"
    };
    private final boolean requiresChecksum;
    private final String label;

    /**
     * Constructs a basic mode Code 39 barcode with the specified data and an optional
     * checksum.
     *
     * @param data             The data to encode
     * @param requiresChecksum A flag indicating whether a checksum is required or not
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public Code39Barcode(String data, boolean requiresChecksum) throws BarcodeException {
        this(data, requiresChecksum, false);
    }

    /**
     * Constructs an extended mode Code 39 barcode with the specified data and an optional
     * checksum. The extended mode encodes all 128 ASCII characters using two character pairs
     * from the basic Code 39 character set. Note that most barcode scanners will need to
     * be configured to accept extended Code 39.
     *
     * @param data             The data to encode
     * @param requiresChecksum A flag indicating whether a checksum is required or not
     * @param extendedMode     Puts the barcode into extended mode, where all 128 ASCII characters can be encoded
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public Code39Barcode(String data, boolean requiresChecksum, boolean extendedMode) throws BarcodeException {
        super(extendedMode ? encodeExtendedChars(data) : validateBasicChars(data));
        this.requiresChecksum = requiresChecksum;
        this.label = data;
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
     * Returns the encoded data for the barcode.
     *
     * @return An array of modules that represent the data as a barcode
     */
    protected Module[] encodeData() {
        List modules = new ArrayList();
        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            modules.add(new SeparatorModule(1));
            Module module = ModuleFactory.getModule(String.valueOf(c));
            modules.add(module);
        }
        modules.add(new SeparatorModule(1));
        return (Module[]) modules.toArray(new Module[0]);
    }

    /**
     * Returns the checksum for the barcode, pre-encoded as a Module.
     *
     * @return Null if no checksum is required, a Mod-43 calculated checksum otherwise
     */
    protected Module calculateChecksum() {
        if (requiresChecksum) {
            int checkIndex = calculateMod43(data);
            CompositeModule compositeModule = new CompositeModule();
            compositeModule.add(ModuleFactory.getModuleForIndex(checkIndex));
            compositeModule.add(new SeparatorModule(1));
            return compositeModule;
        }
        return null;
    }

    /**
     * Returns the for the Mod-43 checkIndex for the barcode as an int
     *
     * @return Mod-43 checkIndex for the given data String
     */
    public static int calculateMod43(final String givenData) {
        int sum = 0;
        StringCharacterIterator iter = new StringCharacterIterator(givenData);
        for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
            sum += ModuleFactory.getIndex(String.valueOf(c));
        }
        int checkIndex = sum % 43;
        return checkIndex;
    }

    /**
     * Returns the pre-amble for the barcode.
     *
     * @return ModuleFactory.START_STOP
     */
    protected Module getPreAmble() {
        return ModuleFactory.START_STOP;
    }

    /**
     * Returns the post-amble for the barcode.
     *
     * @return ModuleFactory.START_STOP
     */
    protected Module getPostAmble() {
        return ModuleFactory.START_STOP;
    }

    private static String validateBasicChars(String data) throws BarcodeException {
        StringCharacterIterator iter = new StringCharacterIterator(data);
        for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
            if (!ModuleFactory.hasModule(String.valueOf(c), false)) {
                throw new BarcodeException("Illegal character - try using extended mode if you need "
                        + "to encode the full ASCII character set");
            }
        }
        return data;
    }

    private static String encodeExtendedChars(String data) {
        StringBuffer buf = new StringBuffer();
        StringCharacterIterator iter = new StringCharacterIterator(data);
        for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
            if (!ModuleFactory.hasModule(String.valueOf(c), true)) {
                buf.append(ModuleFactory.getExtendedCharacter(c));
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }
}
