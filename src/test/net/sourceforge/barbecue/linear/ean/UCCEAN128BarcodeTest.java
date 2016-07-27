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

package net.sourceforge.barbecue.linear.ean;

import junit.framework.TestCase;
import net.sourceforge.barbecue.CompositeModule;
import net.sourceforge.barbecue.Module;

public class UCCEAN128BarcodeTest extends TestCase {
    
    public void testConstructingWithNoAppIDThrowsException() throws Exception {
        try {
            new UCCEAN128Barcode(null, "35436");
            fail();
        } catch (IllegalArgumentException e) {
            // OK
        }
        
        try {
            new UCCEAN128Barcode("", "35436");
            fail();
        } catch (IllegalArgumentException e) {
            // OK
        }
    }
    
    public void testPreAmbleIsStartC() throws Exception {
        UCCEAN128Barcode barcode = new UCCEAN128Barcode("00", "00");
        CompositeModule m = (CompositeModule) barcode.getPreAmble();
        assertEquals(2, m.size());
        // Module at pos 0 is quiet section
        assertEquals(UCCEAN128Barcode.START_C, m.getModule(1));
    }
    
    public void testLabelIsAIInBracketsFollowedByDataWithChecksum() throws Exception {
        String data = "99999";
        UCCEAN128Barcode barcode = new UCCEAN128Barcode("010", data);
        assertEquals("(010) " + data + UCCEAN128Barcode.getMod10CheckDigit("99999", true), barcode.getLabel());
    }
    
    public void testCheckDigitCalculatedUsingStandardCode128Algorithm() throws Exception {
        UCCEAN128Barcode b = new UCCEAN128Barcode("00", "00030017000043516");
        assertEquals("8", b.getData().substring(b.getData().length() - 1));
    }
    
    public void testCalculateChecksumReturnsNullForNoCheckDigitConstructor() throws Exception {
        UCCEAN128Barcode b = new UCCEAN128Barcode("00", "00030017000043516", false);
        assertEquals(UCCEAN128Barcode.FNC_1 + "0000030017000043516", b.getData());
    }
    
    public void testBarcodeIncludesMod10CheckDigitAndEncodingAlsoIncludesCode128ymbologyCheckDigit() throws Exception {
        UCCEAN128Barcode b = new UCCEAN128Barcode("00", "12345");
        Module[] modules = b.encodeData();
        assertEquals(5, modules.length);
        String[] expected = new String[] {
            UCCEAN128Barcode.FNC_1,
            "00",
            "12",
            "34",
            '5' + UCCEAN128Barcode.getMod10CheckDigit("12345", true)
        };
        for (int i = 0; i < modules.length; i++) {
            Module module = modules[i];
            assertEquals(expected[i], module.getSymbol());
        }
        assertEquals("36", b.calculateChecksum().getSymbol());
    }
}
