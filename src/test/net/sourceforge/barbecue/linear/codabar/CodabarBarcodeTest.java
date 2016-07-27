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

package net.sourceforge.barbecue.linear.codabar;

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

public class CodabarBarcodeTest extends TestCase {
	private CodabarBarcode b;

	protected void setUp() throws Exception {
		super.setUp();
		b = new CodabarBarcode("A12345B");
	}

	public void testPreAmbleIsEmpty() throws Exception {
		assertEquals(new BlankModule(0), b.getPreAmble());
	}

	public void testPostAmbleIsEmpty() throws Exception {
		assertEquals(new BlankModule(0), b.getPostAmble());
	}

	public void testChecksumIsNull() throws Exception {
		assertNull(b.calculateChecksum());
	}

	public void testWidthCalculatedCorrectly() throws Exception {
		assertWidth("A", 10);
	}

	public void testStartStopCharsMustBeFirstAndLastChars() throws Exception {
		assertCanConstruct("A12345B");
		assertCanConstruct("A12345D");
		assertCanConstruct("A12345C");
		assertCanConstruct("B12345C");
		assertCanConstruct("B");

		assertCannotConstruct("1A2345");
		assertCannotConstruct("123A45");
		assertCannotConstruct("A123A45");
		assertCannotConstruct("123A45B");
		assertCannotConstruct("A12345BB");
		assertCannotConstruct("AB12345B");
		assertCannotConstruct("ABCD");
	}

	public void testAAndCUsedAsDefaultStartAndStopCharsIfDataContainsNoAlphas() throws Exception {
		Barcode barcode = new CodabarBarcode("1234");
		assertEquals("A1234C", barcode.getData());
	}

	public void testExtendedStartStopCharsAreAccepted() throws Exception {
		assertCanConstruct("a1234t");
		assertCanConstruct("b1234e");
		assertCanConstruct("c1234*");
		assertCanConstruct("d1234n");
	}

	public void testExtendedStartAndStopCharsAreShownInLabelAsOriginalCharsEvenThoughEncodedAsABCD() throws Exception {
		CodabarBarcode barcode = new CodabarBarcode("a1234*");
		assertEquals("a1234*", barcode.getLabel());
	}

	public void testBarcodeCannotContainCharsOutsideCodabarSet() throws Exception {
		assertCannotConstruct("B12345F");
	}

	public void testSpacesInDataAreIgnoredForEncodingButPreservedInTextOutput() throws Exception {
		String data = "123 123 123 123";
		assertCanConstruct(data);
		CodabarBarcode barcode = new CodabarBarcode(data);
		assertEquals(data, barcode.getLabel());
	}

	public void testSeparatorAddedBetweenModules() throws Exception {
		CodabarBarcode barcode = new CodabarBarcode("A");
		Module[] modules = barcode.encodeData();
		assertEquals(1, modules.length);
		assertEquals(new Module(new int[] {1, 1, 2, 2, 1, 2, 1}), modules[0]);

		barcode = new CodabarBarcode("AB");
		modules = barcode.encodeData();
		assertEquals(3, modules.length);
		assertEquals(new Module(new int[] {1, 1, 2, 2, 1, 2, 1}), modules[0]);
		assertEquals(new SeparatorModule(1), modules[1]);
		assertEquals(new Module(new int[] {1, 1, 1, 2, 1, 2, 2}), modules[2]);

		barcode = new CodabarBarcode("A123B");
		modules = barcode.encodeData();
		assertEquals(9, modules.length);
		assertEquals(new Module(new int[] {1, 1, 2, 2, 1, 2, 1}), modules[0]);
		assertEquals(new SeparatorModule(1), modules[1]);
		assertEquals(new SeparatorModule(1), modules[3]);
		assertEquals(new SeparatorModule(1), modules[5]);
		assertEquals(new SeparatorModule(1), modules[7]);
		assertEquals(new Module(new int[] {1, 1, 1, 2, 1, 2, 2}), modules[8]);
	}

	public void testNullAndEmptyDataRejected() throws Exception {
		assertCannotConstruct(null);
		assertCannotConstruct("");
	}

	public void testEncodingIsCorrect() throws Exception {
		String data = "A1234567890.+/:D";
		CodabarBarcode barcode = new CodabarBarcode(data);
		Module[] modules = barcode.encodeData();
		assertEquals(31, modules.length);
		int index = 0;
		StringCharacterIterator iter = new StringCharacterIterator(data);
		for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
			assertEquals(ModuleFactory.getModule(String.valueOf(c)), modules[index]);
			index += 2;
		}
	}

	public void testDrawPaintsCorrectBars() throws Exception {
		CodabarBarcode barcode = new CodabarBarcode("A123B");
		barcode.setBarWidth(1);
		barcode.setDrawingText(false);
		GraphicsMock g = new GraphicsMock();
		barcode.draw(g, 0, 0);
		int[] expected = new int[] {
			0, // Quiet zone
			1, 1, 2, 2, 1, 2, 1,	// A
			1, // Separator
			1, 1, 1, 1, 2, 2, 1,	// 1
			1, // Separator
			1, 1, 1, 2, 1, 1, 2,	// 2
			1, // Separator
			2, 2, 1, 1, 1, 1, 1,	// 3
			1, // Separator
			1, 1, 1, 2, 1, 2, 2,	// B
			0 // Quiet zone
		};
		List rects = g.getRects();
		assertEquals(41, rects.size());
		for (int i=0; i<rects.size(); i++) {
			Rectangle rectangle = (Rectangle) rects.get(i);
			assertEquals(expected[i], (int) rectangle.getWidth());
		}
	}

	private void assertWidth(String data, int expectedWidth) throws Exception {
		CodabarBarcode barcode = new CodabarBarcode(data);
		barcode.setBarWidth(1);
		assertEquals(expectedWidth, barcode.getWidth());
	}

	protected void assertCanConstruct(String data) {
		try {
			new CodabarBarcode(data);
		} catch (BarcodeException e) {
			e.printStackTrace();
			fail("Exception thrown attempting to construct barcode with data: " + data);
		}
	}

	protected void assertCannotConstruct(String data) {
		try {
			new CodabarBarcode(data);
			fail("Exception should have been thrown attempting to construct barcode with data: " + data);
		} catch (BarcodeException e) {
			// OK
		}
	}
}
