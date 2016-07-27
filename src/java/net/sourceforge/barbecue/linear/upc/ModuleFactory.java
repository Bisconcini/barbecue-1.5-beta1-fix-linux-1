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

package net.sourceforge.barbecue.linear.upc;

import net.sourceforge.barbecue.Module;
import net.sourceforge.barbecue.BlankModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The UPC-A barcode module definitions.
 *
 * @author <a href="mailto:james@metalskin.com">James Jenner</a>
 */
public class ModuleFactory {
    
    /** The guards for the barcode */
    public static final Module LEFT_GUARD = new Module(new int[] {1, 1, 1});
    public static final Module CENTRE_GUARD = new Module(new int[] {0, 1, 1, 1, 1, 1});
    public static final Module RIGHT_GUARD = new Module(new int[] {1, 1, 1});
    
    public final static Module LEFT_MARGIN = new BlankModule(11);
    public final static Module RIGHT_MARGIN = new BlankModule(11);;
    
    protected static final List KEYS_LEFT = new ArrayList();
    protected static final List KEYS_RIGHT = new ArrayList();
    
    protected static final Map SET_LEFT = new HashMap();
    protected static final Map SET_RIGHT = new HashMap();
    
    public static final int LEFT_WIDTH = 6;
    public static final int GUARD_CHAR_SIZE = 1;
    
    static {
        initBaseSet();
    }

    /**
     * Cannot construct.
     */
    protected ModuleFactory() {
    }
    
    /*
     *
     */
    /**
     * Initialise the module definitions.
     */
    protected static void initBaseSet() {
        initRightSet();
        initLeftSet();
    }
    
    protected static void initRightSet() {
        // right side
        KEYS_RIGHT.add("0"); SET_RIGHT.put("0", new Module(new int[] {3, 2, 1, 1}));
        KEYS_RIGHT.add("1"); SET_RIGHT.put("1", new Module(new int[] {2, 2, 2, 1}));
        KEYS_RIGHT.add("2"); SET_RIGHT.put("2", new Module(new int[] {2, 1, 2, 2}));
        KEYS_RIGHT.add("3"); SET_RIGHT.put("3", new Module(new int[] {1, 4, 1, 1}));
        KEYS_RIGHT.add("4"); SET_RIGHT.put("4", new Module(new int[] {1, 1, 3, 2}));
        KEYS_RIGHT.add("5"); SET_RIGHT.put("5", new Module(new int[] {1, 2, 3, 1}));
        KEYS_RIGHT.add("6"); SET_RIGHT.put("6", new Module(new int[] {1, 1, 1, 4}));
        KEYS_RIGHT.add("7"); SET_RIGHT.put("7", new Module(new int[] {1, 3, 1, 2}));
        KEYS_RIGHT.add("8"); SET_RIGHT.put("8", new Module(new int[] {1, 2, 1, 3}));
        KEYS_RIGHT.add("9"); SET_RIGHT.put("9", new Module(new int[] {3, 1, 1, 2}));
    }
    
    protected static void initLeftSet() {
        // left side
        KEYS_LEFT.add("0"); SET_LEFT.put("0", new Module(new int[] {0, 3, 2, 1, 1}));
        KEYS_LEFT.add("1"); SET_LEFT.put("1", new Module(new int[] {0, 2, 2, 2, 1}));
        KEYS_LEFT.add("2"); SET_LEFT.put("2", new Module(new int[] {0, 2, 1, 2, 2}));
        KEYS_LEFT.add("3"); SET_LEFT.put("3", new Module(new int[] {0, 1, 4, 1, 1}));
        KEYS_LEFT.add("4"); SET_LEFT.put("4", new Module(new int[] {0, 1, 1, 3, 2}));
        KEYS_LEFT.add("5"); SET_LEFT.put("5", new Module(new int[] {0, 1, 2, 3, 1}));
        KEYS_LEFT.add("6"); SET_LEFT.put("6", new Module(new int[] {0, 1, 1, 1, 4}));
        KEYS_LEFT.add("7"); SET_LEFT.put("7", new Module(new int[] {0, 1, 3, 1, 2}));
        KEYS_LEFT.add("8"); SET_LEFT.put("8", new Module(new int[] {0, 1, 2, 1, 3}));
        KEYS_LEFT.add("9"); SET_LEFT.put("9", new Module(new int[] {0, 3, 1, 1, 2}));
    }
    
    /**
     * Returns the module that represents the specified character.
     * @param key The data character to get the encoding module for
     * @param position The position of the data character, starts at 0
     * @return The module that encodes the given char
     */
    public static Module getModule(String key, int position) {
        Module module = null;

        if(position + 1 > LEFT_WIDTH) {
            module = (Module)SET_RIGHT.get(key);
        } else {
            module = (Module)SET_LEFT.get(key);
        }
        module.setSymbol(key);
        return module;
    }
    
    /**
     * Indicates whether the given key is represented in the default encoding
     * table that this module factory contains.
     * @return True if the key has a direct module encoding, false if not
     */
    public static boolean hasModule(String key) {
        if(KEYS_RIGHT.indexOf(key) > -1) {
            return true;
        }

        if(KEYS_LEFT.indexOf(key) > -1) {
            return true;
        }

        return false;
    }
    
    /**
     * Returns the encoded module at the given index position. This is used to
     * get the encoded checksum character.
     * @param index The index of the module required
     * @return The module at the specified index
     */
    public static Module getModuleForIndex(int index) {
        if(index + 1 > LEFT_WIDTH) {
            return getModule((String)KEYS_RIGHT.get(index), index);
        } else {
            return getModule((String)KEYS_LEFT.get(index), index);
        }
    }

    /**
     * Indicates whether the given character is valid for this barcode or not.
     * This basically just checks to see whether the key is in the list of
     * encoded characters.
     * @param key The key to check for validity
     * @return True if the key is valid, false otherwise
     */
    public static boolean isValid(String key) {
        if(KEYS_RIGHT.indexOf(key) > -1) {
            return true;
        }

        if(KEYS_LEFT.indexOf(key) > -1) {
            return true;
        }

        return false;
    }
}
