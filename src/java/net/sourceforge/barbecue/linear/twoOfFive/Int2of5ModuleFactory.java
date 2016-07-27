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

package net.sourceforge.barbecue.linear.twoOfFive;

import net.sourceforge.barbecue.Module;

import java.util.Map;
import java.util.HashMap;

/**
 * Interleave 2 of 5 barcode module definitions.
 *
 * @author <a href="mailto:james@metalskin.com">James Jenner</a>
 */
final class Int2of5ModuleFactory extends Std2of5ModuleFactory {
    public static final Module START_CHAR = new Module(new int[] {1, 1, 1, 1});
    public static final Module END_CHAR = new Module(new int[] {3, 1, 1});
    
    /**
     * No public access.
     */
    protected Int2of5ModuleFactory() {
    }
    
    /**
     * Returns the module that represents the specified characters.
     * Interleave uses one character for the bars and the other for
     * the spaces, so for each 'module' two characters are required 
     * to determine the resulting module.
     * @param barValue  The data character for the bars
     * @param spaceValue The data character for the spaces
     * @return The module that encodes the given characters
     */
    public static Module getModule(String barValue, String spaceValue) {
        Module module = null;
        int[] bar = null;
        int[] space = null;
        bar = (int[])SET.get(barValue);
        space = (int[])SET.get(spaceValue);
        
        // this should always be true, but best to check just in case...
        if(bar != null && bar.length == 5 && space != null && space.length == 5) {
            
            // create the new module based on the selected bar val and space val
            module = new Module(new int[] {
                bar[0], space[0], 
                bar[1], space[1], 
                bar[2], space[2], 
                bar[3], space[3], 
                bar[4], space[4]
            });
            module.setSymbol(barValue + spaceValue);
        }
        
        return module;
    }
}
