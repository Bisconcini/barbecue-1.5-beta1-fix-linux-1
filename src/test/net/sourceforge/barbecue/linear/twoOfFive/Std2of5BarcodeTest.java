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

public class Std2of5BarcodeTest extends TestCase {
    private Std2of5Barcode b;
    
    protected void setUp() throws Exception {
        super.setUp();
        b = new Std2of5Barcode("1234567890");
    }
    
    public void testPreAmble() throws Exception {
        assertEquals(new Module(new int[] {2, 1, 2, 1, 1, 1}), b.getPreAmble());
    }
    
    public void testPostAmble() throws Exception {
        assertEquals(new Module(new int[] {2, 1, 1, 1, 2, 1}), b.getPostAmble());
    }
    
    public void testChecksumIsNull() throws Exception {
        assertNull(b.calculateChecksum());
    }
    
    public void testInvalidCharacters() throws Exception {
        try {
            new Std2of5Barcode("1234567890A");
            fail("Non numeric data was allowed via constructor");
        } catch(BarcodeException e) {
        }
    }

    public void testEncodingIsCorrect() throws Exception {
        String data = "12345670";
        Std2of5Barcode barcode = new Std2of5Barcode(data);
        Module[] modules = barcode.encodeData();
        assertEquals(8, modules.length);
        int index = 0;
        StringCharacterIterator iter = new StringCharacterIterator(data);

        for(char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
            assertEquals(Std2of5ModuleFactory.getModule(String.valueOf(c)), modules[index]);
            index++;
        }
    }
    
    public void testDrawPaintsCorrectBars() throws Exception {
        Std2of5Barcode barcode = new Std2of5Barcode("12345670");
        barcode.setBarWidth(1);
        barcode.setDrawingText(false);
        GraphicsMock g = new GraphicsMock();
        barcode.draw(g, 0, 0);
        int[] expected = new int[] {
            2, 1, 2, 1, 1, 1, // start char
            3, 1, 1, 1, 1, 1, 1, 1, 3, 1, // 1
            1, 1, 3, 1, 1, 1, 1, 1, 3, 1, // 2
            3, 1, 3, 1, 1, 1, 1, 1, 1, 1, // 3 
            1, 1, 1, 1, 3, 1, 1, 1, 3, 1, // 4
            3, 1, 1, 1, 3, 1, 1, 1, 1, 1, // 5
            1, 1, 3, 1, 3, 1, 1, 1, 1, 1, // 6
            1, 1, 1, 1, 1, 1, 3, 1, 3, 1, // 7
            1, 1, 1, 1, 3, 1, 3, 1, 1, 1, // 0
            2, 1, 1, 1, 2, 1 // stop char
        };
        List rects = g.getRects();
        assertEquals(92, rects.size());
        for (int i=0; i<rects.size(); i++) {
            Rectangle rectangle = (Rectangle) rects.get(i);
            assertEquals(expected[i], (int) rectangle.getWidth());
        }
    }
    
    private void assertWidth(String data, int expectedWidth) throws Exception {
        Std2of5Barcode barcode = new Std2of5Barcode(data);
        barcode.setBarWidth(1);
        assertEquals(expectedWidth, barcode.getWidth());
    }
}
