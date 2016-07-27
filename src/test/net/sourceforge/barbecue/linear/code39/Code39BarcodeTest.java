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

import net.sourceforge.barbecue.*;

import java.awt.*;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.List;

public class Code39BarcodeTest extends BarcodeTestCase {
    private Code39Barcode barcode;

    protected void setUp() throws Exception {
        super.setUp();
        barcode = new Code39Barcode("12345", false);
    }

    public void testWidthIsCalculatedCorrectly() throws Exception {
        Code39Barcode barcode = new Code39Barcode("12345", true);
        assertEquals(206, barcode.getWidth());
        barcode = new Code39Barcode("12345", false);
        assertEquals(180, barcode.getWidth());
        barcode = new Code39Barcode("12345", false);
        barcode.setBarWidth(1);
        assertEquals(90, barcode.getWidth());
        barcode = new Code39Barcode("12345", true);
        barcode.setBarWidth(1);
        assertEquals(103, barcode.getWidth());
        barcode = new Code39Barcode("12345", true);
        barcode.setBarWidth(2);
        assertEquals(206, barcode.getWidth());
    }

    public void testChecksumAddedIfRequired() throws Exception {
        barcode = new Code39Barcode("123245", true);
        assertNotNull(barcode.calculateChecksum());
    }

    public void testChecksumNotAddedIfNotRequired() throws Exception {
        assertNull(barcode.calculateChecksum());
    }

    public void testChecksumCalculatedCorrectly() throws Exception {
        barcode = new Code39Barcode("12345", true);
        assertEquals("F", barcode.calculateChecksum().getSymbol());
    }

    public void testPreAmbleIsStartCharOnly() throws Exception {
        assertEquals(ModuleFactory.START_STOP, barcode.getPreAmble());
    }

    public void testPostAmbleIsStopCharOnly() throws Exception {
        assertEquals(ModuleFactory.START_STOP, barcode.getPostAmble());
    }

    public void testEncodingIsCorrect() throws Exception {
        String data = "1234567890AZ-. $/+%";
        Code39Barcode barcode = new Code39Barcode(data, true);
        Module[] modules = barcode.encodeData();
        assertEquals(39, modules.length);
        int index = 0;
        StringCharacterIterator iter = new StringCharacterIterator(data);
        for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
            assertEquals(new SeparatorModule(), modules[index]);
            assertEquals(ModuleFactory.getModule(String.valueOf(c)), modules[index + 1]);
            index += 2;
        }
        assertEquals(new SeparatorModule(), modules[index]);
    }

    public void testCannotConstructBarcodeWithExtendedCharactersInBasicMode() throws Exception {
        try {
            new Code39Barcode("12(12", false);
            fail();
        } catch (BarcodeException e) {
            // OK
        }

        try {
            new Code39Barcode("12!12", false);
            fail();
        } catch (BarcodeException e) {
            // OK
        }

        try {
            new Code39Barcode("12~12", false);
            fail();
        } catch (BarcodeException e) {
            // OK
        }
    }

    public void testCannotUseAsteriskCharacterInBasicMode() throws Exception {
        try {
            new Code39Barcode("12*12", false);
            fail();
        } catch (BarcodeException e) {
            // OK
        }
    }

    public void testCanEncodeExtendedASCIICharactersInFullASCIIMode() throws Exception {
        try {
            String rawData = "1$%%%23(287_)!*!124a";
            String[] expected = new String[]{"1", "/", "D", "/", "E",
                                             "/", "E", "/", "E", "2",
                                             "3", "/", "H", "2", "8",
                                             "7", "%", "O", "/", "I",
                                             "/", "A", "/", "J", "/",
                                             "A", "1", "2", "4", "+", "A"};
            Code39Barcode barcode = new Code39Barcode(rawData, false, true);
            Module[] modules = barcode.encodeData();
            assertEquals(63, modules.length);
            int expIndex = 0;
            for (int i = 0; i < modules.length; i += 2) {
                assertEquals(new SeparatorModule(), modules[i]);
                if (i < 62) {
                    assertEquals(ModuleFactory.getModule(expected[expIndex]), modules[i + 1]);
                }
                expIndex++;
            }
            assertEquals(new SeparatorModule(), modules[62]);
            assertEquals(rawData, barcode.getLabel());
        } catch (BarcodeException e) {
            fail();
        }
    }

    public void testDrawPaintsCorrectBars() throws Exception {
        Code39Barcode barcode = new Code39Barcode("A123Z+ ", false);
        barcode.setBarWidth(1);
        barcode.setDrawingText(false);
        GraphicsMock g = new GraphicsMock();
        barcode.draw(g, 0, 0);
        int[] expected = new int[]{
            1, 2, 1, 1, 2, 1, 2, 1, 1, // START
            1, // Separator
            2, 1, 1, 1, 1, 2, 1, 1, 2, // A
            1, // Separator
            2, 1, 1, 2, 1, 1, 1, 1, 2, // 1
            1, // Separator
            1, 1, 2, 2, 1, 1, 1, 1, 2, // 2
            1, // Separator
            2, 1, 2, 2, 1, 1, 1, 1, 1, // 3
            1, // Separator
            1, 2, 2, 1, 2, 1, 1, 1, 1, // Z
            1, // Separator
            1, 2, 1, 1, 1, 2, 1, 2, 1, // +
            1, // Separator
            1, 2, 2, 1, 1, 1, 2, 1, 1, // ' '
            1, // Separator
            1, 2, 1, 1, 2, 1, 2, 1, 1 	// STOP
        };
        List rects = g.getRects();
        assertEquals(89, rects.size());
        for (int i = 0; i < rects.size(); i++) {
            Rectangle rectangle = (Rectangle) rects.get(i);
            assertEquals(expected[i], (int) rectangle.getWidth());
        }
    }

    /**
     * Bug 1000145
     */
    public void testSeparatorIsAppendedToCheckSum() throws Exception {
        barcode = new Code39Barcode("10001", true);
        barcode.setBarWidth(1);
        barcode.setDrawingText(false);
        GraphicsMock g = new GraphicsMock();
        barcode.draw(g, 0, 0);
        int[] expected = new int[]{
            1, 2, 1, 1, 2, 1, 2, 1, 1, // START
            1, // Separator
            2, 1, 1, 2, 1, 1, 1, 1, 2, // 1
            1, // Separator
            1, 1, 1, 2, 2, 1, 2, 1, 1, // 0
            1, // Separator
            1, 1, 1, 2, 2, 1, 2, 1, 1, // 0
            1, // Separator
            1, 1, 1, 2, 2, 1, 2, 1, 1, // 0
            1, // Separator
            2, 1, 1, 2, 1, 1, 1, 1, 2, // 1
            1, // Separator
            1, 1, 2, 2, 1, 1, 1, 1, 2, // 2 - checksum
            1, // Separator
            1, 2, 1, 1, 2, 1, 2, 1, 1 	// STOP
        };
        List rects = g.getRects();
        assertEquals(79, rects.size());
        for (int i = 0; i < rects.size(); i++) {
            Rectangle rectangle = (Rectangle) rects.get(i);
            assertEquals("Bit " + i, expected[i], (int) rectangle.getWidth());
        }
    }

    public void testCalculateMod43() throws Exception {
        assertEquals(24, Code39Barcode.calculateMod43("I050000001"));
        assertEquals("O", ModuleFactory.getModuleForIndex(Code39Barcode.calculateMod43("I050000001")).getSymbol());
    }
}
