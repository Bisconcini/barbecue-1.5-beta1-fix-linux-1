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

package net.sourceforge.barbecue;

import junit.framework.TestCase;
import net.sourceforge.barbecue.Modulo10;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.List;
import java.awt.*;

public class Modulo10Test extends TestCase {
    
    public void testInvalidCharactersThrowsException() throws Exception {
        try {
            Modulo10.getMod10CheckDigit("L32774300042", 1, 3, true);
            fail();
        } catch(java.lang.NumberFormatException e) {
        }
        try {
            Modulo10.getMod10CheckDigit("L32774300042", 1, 3);
            fail();
        } catch(java.lang.NumberFormatException e) {
        }
        try {
            Modulo10.getMod10CheckDigit("L32774300042", 3);
            fail();
        } catch(java.lang.NumberFormatException e) {
        }
    }
    
    
    public void testCheckDigitCalculatedUsingMod10() throws Exception {
        // check the mod 10 checksum for '32774300042'
        assertEquals("6", String.valueOf(Modulo10.getMod10CheckDigit("32774300042", 1, 3, true)));
        
        // check the mod 10 checksum for '07567816412'
        // (0 * 1) + (7 * 3) + (5 * 1) + (6 * 3) + (7 * 1) + (8 * 3) + 
        // (1 * 1) + (6 * 3) + (4 * 1) + (1 * 3) + (2 * 1)
        // = 0 + 21 + 5 + 18 + 7 + 24 + 1 + 18 + 4 + 3 + 2
        // = 103
        // round up to 10 means it's 7
        assertEquals("7", String.valueOf(Modulo10.getMod10CheckDigit("07567816412", 1, 3, true)));
        
        // check the mod 10 checksum for '07567816412'
        // (0 * 3) + (7 * 1) + (5 * 3) + (6 * 1) + (7 * 3) + (8 * 1) + 
        // (1 * 3) + (6 * 1) + (4 * 3) + (1 * 1) + (2 * 3)
        // = 0 + 7 + 15 + 6 + 21 + 8 + 3 + 6 + 12 + 1 + 6
        // = 85
        // round up to 10 means it's 5
        assertEquals("5", String.valueOf(Modulo10.getMod10CheckDigit("07567816412", 1, 3, false)));
        
        // check the mod 10 checksum for '87070000002'
        // (8 * 1) + (7 * 3) + (0 * 1) + (7 * 3) + (0 * 1) + (0 * 3) + 
        // (0 * 1) + (0 * 3) + (0 * 1) + (0 * 3) + (2 * 1)
        // = 8 + 21 + 0 + 21 + 0 + 0 + 0 + 0 + 0 + 0 + 2
        // = 52
        // round up to 10 means it's 8
        assertEquals("8", String.valueOf(Modulo10.getMod10CheckDigit("87070000002", 1, 3, true)));
        
        // check the mod 10 checksum for '87070000002'
        // (8 * 3) + (7 * 1) + (0 * 3) + (7 * 1) + (0 * 3) + (0 * 1) + 
        // (0 * 3) + (0 * 1) + (0 * 3) + (0 * 1) + (2 * 3)
        // = 24 + 7 + 0 + 7 + 0 + 0 + 0 + 0 + 0 + 0 + 6
        // = 44
        // round up to 10 means it's 6
        assertEquals("6", String.valueOf(Modulo10.getMod10CheckDigit("87070000002", 1, 3, false)));
        
        // check the mod 10 checksum for '123456789'
        // (1 * 3) + (2 * 2) + (3 * 3) + (4 * 2) + (5 * 3) + (6 * 2) + 
        // (7 * 3) + (8 * 2) + (9 * 3)
        // = 3 + 4 + 9 + 8 + 15 + 12 + 21 + 16 + 27
        // = 115
        // round up to 10 means it's 5
        assertEquals("5", String.valueOf(Modulo10.getMod10CheckDigit("123456789", 3, 2, true)));

        // check the mod 10 checksum for '123456789'
        // (1 * 2) + (2 * 3) + (3 * 2) + (4 * 3) + (5 * 2) + (6 * 3) + 
        // (7 * 2) + (8 * 3) + (9 * 2)
        // = 2 + 6 + 6 + 12 + 10 + 18 + 14 + 24 + 18
        // = 110
        // round up to 10 means it's 0
        assertEquals("0", String.valueOf(Modulo10.getMod10CheckDigit("123456789", 3, 2, false)));
    }
 }
