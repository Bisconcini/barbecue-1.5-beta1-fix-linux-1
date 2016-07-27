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

import net.sourceforge.barbecue.BlankModule;
import net.sourceforge.barbecue.Module;
import net.sourceforge.barbecue.linear.upc.ModuleFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The EAN 13 barcode module definitions.
 *
 * @author <a href="mailto:james@metalskin.com">James Jenner</a>
 */
final class EAN13ModuleFactory extends ModuleFactory {

    protected static final List KEYS_LEFT_ODD = new ArrayList();
    protected static final List KEYS_LEFT_EVEN = new ArrayList();

    protected static final Map SET_LEFT_ODD = new HashMap();
    protected static final Map SET_LEFT_EVEN = new HashMap();

    protected static final Module RIGHT_MARGIN = new BlankModule(7);

    protected static final Map PARITY_TABLE = new HashMap();

    protected static final int ODD = 0;
    protected static final int EVEN = 1;

    public static final int LEFT_WIDTH = 7;
    public static final int GUARD_CHAR_SIZE = 0;

    static {
        initBaseSet();
    }

    /**
     * Cannot construct.
     */
    protected EAN13ModuleFactory() {
    }

    /**
     * Initialise the module definitions.
     */
    protected static void initBaseSet() {
        initRightSet();

        initLeftOddSet();
        initLeftEvenSet();
        
        /* map - First number system digit
         * 0 - second number system digit
         * 1 -> 5 - manufacturer code characters
         */
        PARITY_TABLE.put("0", new int[]{ODD, ODD, ODD, ODD, ODD, ODD});
        PARITY_TABLE.put("1", new int[]{ODD, ODD, EVEN, ODD, EVEN, EVEN});
        PARITY_TABLE.put("2", new int[]{ODD, ODD, EVEN, EVEN, ODD, EVEN});
        PARITY_TABLE.put("3", new int[]{ODD, ODD, EVEN, EVEN, EVEN, ODD});
        PARITY_TABLE.put("4", new int[]{ODD, EVEN, ODD, ODD, EVEN, EVEN});
        PARITY_TABLE.put("5", new int[]{ODD, EVEN, EVEN, ODD, ODD, EVEN});
        PARITY_TABLE.put("6", new int[]{ODD, EVEN, EVEN, EVEN, ODD, ODD});
        PARITY_TABLE.put("7", new int[]{ODD, EVEN, ODD, EVEN, ODD, EVEN});
        PARITY_TABLE.put("8", new int[]{ODD, EVEN, ODD, EVEN, EVEN, ODD});
        PARITY_TABLE.put("9", new int[]{ODD, EVEN, EVEN, ODD, EVEN, ODD});
    }

    protected static void initLeftOddSet() {
        // left side even parity
        KEYS_LEFT_ODD.add("0");
        SET_LEFT_ODD.put("0", new Module(new int[]{0, 3, 2, 1, 1})); // 0001101
        KEYS_LEFT_ODD.add("1");
        SET_LEFT_ODD.put("1", new Module(new int[]{0, 2, 2, 2, 1})); // 0011001
        KEYS_LEFT_ODD.add("2");
        SET_LEFT_ODD.put("2", new Module(new int[]{0, 2, 1, 2, 2})); // 0010011
        KEYS_LEFT_ODD.add("3");
        SET_LEFT_ODD.put("3", new Module(new int[]{0, 1, 4, 1, 1})); // 0111101
        KEYS_LEFT_ODD.add("4");
        SET_LEFT_ODD.put("4", new Module(new int[]{0, 1, 1, 3, 2})); // 0100011
        KEYS_LEFT_ODD.add("5");
        SET_LEFT_ODD.put("5", new Module(new int[]{0, 1, 2, 3, 1})); // 0110001
        KEYS_LEFT_ODD.add("6");
        SET_LEFT_ODD.put("6", new Module(new int[]{0, 1, 1, 1, 4})); // 0101111
        KEYS_LEFT_ODD.add("7");
        SET_LEFT_ODD.put("7", new Module(new int[]{0, 1, 3, 1, 2})); // 0111011
        KEYS_LEFT_ODD.add("8");
        SET_LEFT_ODD.put("8", new Module(new int[]{0, 1, 2, 1, 3})); // 0110111
        KEYS_LEFT_ODD.add("9");
        SET_LEFT_ODD.put("9", new Module(new int[]{0, 3, 1, 1, 2})); // 0001011
    }

    protected static void initLeftEvenSet() {
        // left side odd parity
        KEYS_LEFT_EVEN.add("0");
        SET_LEFT_EVEN.put("0", new Module(new int[]{0, 1, 1, 2, 3})); // 0100111
        KEYS_LEFT_EVEN.add("1");
        SET_LEFT_EVEN.put("1", new Module(new int[]{0, 1, 2, 2, 2})); // 0110011
        KEYS_LEFT_EVEN.add("2");
        SET_LEFT_EVEN.put("2", new Module(new int[]{0, 2, 2, 1, 2})); // 0011011
        KEYS_LEFT_EVEN.add("3");
        SET_LEFT_EVEN.put("3", new Module(new int[]{0, 1, 1, 4, 1})); // 0100001
        KEYS_LEFT_EVEN.add("4");
        SET_LEFT_EVEN.put("4", new Module(new int[]{0, 2, 3, 1, 1})); // 0011101
        KEYS_LEFT_EVEN.add("5");
        SET_LEFT_EVEN.put("5", new Module(new int[]{0, 1, 3, 2, 1})); // 0111001
        KEYS_LEFT_EVEN.add("6");
        SET_LEFT_EVEN.put("6", new Module(new int[]{0, 4, 1, 1, 1})); // 0000101
        KEYS_LEFT_EVEN.add("7");
        SET_LEFT_EVEN.put("7", new Module(new int[]{0, 2, 1, 3, 1})); // 0010001
        KEYS_LEFT_EVEN.add("8");
        SET_LEFT_EVEN.put("8", new Module(new int[]{0, 3, 1, 2, 1})); // 0001001
        KEYS_LEFT_EVEN.add("9");
        SET_LEFT_EVEN.put("9", new Module(new int[]{0, 2, 1, 1, 3})); // 0010111
    }

    /**
     * Returns the module that represents the specified character.
     *
     * @param key      The data character to get the encoding module for
     * @param position The position of the data character, starts at 0
     * @return The module that encodes the given char
     */
    public static Module getModule(String firstChar, String key, int position) {
        Module module = null;

        /* 
         * with the human readble, the left side has 7 chars, but the encoding
         * only has 6.  this is due to the fact that the first char of the
         * left side is not encoded.
         */
        
        if (position + 1 > LEFT_WIDTH) {
            module = (Module) SET_RIGHT.get(key);
        } else {
            int[] parityRef = (int[]) PARITY_TABLE.get(firstChar);

            if (parityRef[position - 1] == ODD) {
                module = (Module) SET_LEFT_ODD.get(key);
            } else {
                module = (Module) SET_LEFT_EVEN.get(key);
            }
        }

        module.setSymbol(key);
        return module;
    }

    /**
     * Indicates whether the given key is represented in the default encoding
     * table that this module factory contains.
     *
     * @return True if the key has a direct module encoding, false if not
     */
    public static boolean hasModule(String key) {
        if (KEYS_RIGHT.indexOf(key) > -1) {
            return true;
        }

        if (KEYS_LEFT_ODD.indexOf(key) > -1) {
            return true;
        }

        if (KEYS_LEFT_EVEN.indexOf(key) > -1) {
            return true;
        }

        return false;
    }

    /**
     * Returns the encoded module at the given index position. This is used to
     * get the encoded checksum character.
     *
     * @param index The index of the module required
     * @return The module at the specified index
     */
    public static Module getModuleForIndex(String firstChar, int index) {
        if (index + 1 > LEFT_WIDTH) {
            return getModule((String) KEYS_RIGHT.get(index), index);
        } else {
            int[] parityRef = (int[]) PARITY_TABLE.get(firstChar);

            if (parityRef[index - 1] == ODD) {
                return getModule((String) KEYS_LEFT_ODD.get(index), index);
            } else {
                return getModule((String) KEYS_LEFT_EVEN.get(index), index);
            }
        }
    }

    /**
     * Indicates whether the given character is valid for this barcode or not.
     * This basically just checks to see whether the key is in the list of
     * encoded characters.
     *
     * @param key The key to check for validity
     * @return True if the key is valid, false otherwise
     */
    public static boolean isValid(String key) {
        if (KEYS_RIGHT.indexOf(key) > -1) {
            return true;
        }

        if (KEYS_LEFT_ODD.indexOf(key) > -1) {
            return true;
        }

        if (KEYS_LEFT_EVEN.indexOf(key) > -1) {
            return true;
        }

        return false;
    }
}
