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

import junit.framework.TestCase;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BlankModule;
import net.sourceforge.barbecue.GraphicsMock;
import net.sourceforge.barbecue.Module;
import net.sourceforge.barbecue.SeparatorModule;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.List;
import java.awt.*;

public class UPCABarcodeTest extends TestCase {
    private UPCAMock b;
    
    protected void setUp() throws Exception {
        super.setUp();
        b = new UPCAMock("075678164125");
    }
    
    public void testInvalidCharactersThrowsException() throws Exception {
        try {
            new UPCABarcode("A12345678901");
            fail();
        } catch(BarcodeException e) {
        }
        try {
            new UPCABarcode(null);
            fail();
        } catch(java.lang.IllegalArgumentException e) {
        }
    }
    
    public void testInvalidLengthThrowsException() throws Exception {
        try {
            new UPCABarcode("1234567890");
            fail();
        } catch(BarcodeException e) {
        }
        try {
            new UPCABarcode("");
            fail();
        } catch(BarcodeException e) {
        }
    }
    
    public void testPreAmble() throws Exception {
        assertEquals(new Module(new int[] {1, 1, 1}), b.getPreAmble());
    }
    
    public void testCentreGuard() throws Exception {
        assertEquals(new Module(new int[] {0, 1, 1, 1, 1, 1}), b.getCentreGuard());
    }
    
    public void testPostAmble() throws Exception {
        assertEquals(new Module(new int[] {1, 1, 1}), b.getPostAmble());
    }
    
    public void testCheckDigitCalculatedUsingMod10() throws Exception {
        // check the mod 10 checksum for '327743000426'
        assertEquals("6", String.valueOf(UPCABarcode.getMod10CheckDigit("32774300042")));
        
        // check the mod 10 checksum for '075678164125'
        assertEquals("5", String.valueOf(UPCABarcode.getMod10CheckDigit("07567816412")));
        
        // check the mod 10 checksum for '870700000026'
        assertEquals("6", String.valueOf(UPCABarcode.getMod10CheckDigit("87070000002")));
    }
    
    private class UPCAMock extends UPCABarcode {
        public UPCAMock(String data) throws BarcodeException {
            super(data);
        }
        
        public String getLabel() {
            return super.getLabel();
        }
        
        public Module getPostAmble() {
            return super.getPostAmble();
        }
        
        public Module getPreAmble() {
            return super.getPreAmble();
        }
    }
}
