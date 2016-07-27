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

package net.sourceforge.barbecue.linear.code128;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeTestCase;
import net.sourceforge.barbecue.BlankModule;
import net.sourceforge.barbecue.CompositeModule;
import net.sourceforge.barbecue.GraphicsMock;
import net.sourceforge.barbecue.Module;
import net.sourceforge.barbecue.env.EnvironmentFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Code128BarcodeTest extends BarcodeTestCase {
	private Code128Barcode barcode;

	protected void setUp() throws Exception {
		super.setUp();
		EnvironmentFactory.setDefaultMode();
		barcode = new Code128Barcode("12345");
	}

	public void testDefaultCharSetIsOptimal() throws Exception {
		Code128Barcode barcode = new Code128Barcode("OPTIMAL");
		assertEquals(Code128Barcode.B, barcode.getCharacterSet());
	}

	public void testCheckDigitIsNotShownInText() throws Exception {
		Code128Barcode barcode = new Code128Barcode("CHECKDIGIT");
		assertEquals(10, barcode.getLabel().length());
	}

	public void testBarcodeWidthIsSameForAAndB() throws Exception {
		try {
			barcode = new Code128Barcode("12345", Code128Barcode.A);
			assertEquals(220, barcode.getWidth());
			barcode = new Code128Barcode("12345", Code128Barcode.B);
			assertEquals(220, barcode.getWidth());
		} catch (UnsupportedOperationException e) {
			// OK - tests running on headless machine
		}
	}

	public void testBarcodeWidthIsSameForAAndBInHeadlessMode() throws Exception {
		EnvironmentFactory.setHeadlessMode();
		assertEquals(220, barcode.getWidth());
	}

	public void testBarcodeWidthIsShorterForC() throws Exception {
		barcode = new Code128Barcode("12345", Code128Barcode.C);
		try {
			assertEquals(176, barcode.getWidth());
		} catch (UnsupportedOperationException e) {
			// OK - tests running on headless machine
		}
	}

	public void testBarcodeWidthIsShorterForCInHeadlesMode() throws Exception {
		EnvironmentFactory.setHeadlessMode();
		barcode = new Code128Barcode("12345", Code128Barcode.C);
		assertEquals(176, barcode.getWidth());
	}

	/** Verifies fix for bug: 742599 */
	public void testMultipleEncodingsAreConstant() throws Exception {
		barcode = new Code128Barcode("123456");
		assertEquals(4, barcode.encodeData().length);
		assertEquals(4, barcode.encodeData().length);
	}

	public void testMinimumHeightIsPoint25InchesIfCalculatedHeightIsSmallerThanPoint25Inches() throws Exception {
		barcode = new Code128Barcode("1");
		double point25Inches = EnvironmentFactory.getEnvironment().getResolution() * 0.25;
		assertEquals((int) point25Inches, barcode.calculateMinimumBarHeight(EnvironmentFactory.getEnvironment().getResolution()));
	}

	public void testMinimumHeightIsCalculatedIfBiggerThanPoint25Inches() throws Exception {
		barcode = new Code128Barcode("34098340968340680363089443634634634");
		double point25Inches = EnvironmentFactory.getEnvironment().getResolution() * 0.25;
		assertTrue(point25Inches < barcode.calculateMinimumBarHeight(72));
	}

	public void testAAndBSetsEncodeIndividualCharacters() throws Exception {
		barcode = new Code128Barcode("($$)");
		Module[] encoded = barcode.encodeData();
		Module[] expected = new Module[] {
			ModuleFactory.getModule("(", Code128Barcode.B),
			ModuleFactory.getModule("$", Code128Barcode.B),
			ModuleFactory.getModule("$", Code128Barcode.B),
			ModuleFactory.getModule(")", Code128Barcode.B)
		};
		assertTrue(Arrays.equals(expected, encoded));
	}

	public void testCSetEncodesDigitPairs() throws Exception {
		barcode = new Code128Barcode("01990199", Code128Barcode.C);
		Module[] encoded = barcode.encodeData();
		Module[] expected = new Module[] {
			ModuleFactory.getModule("01", Code128Barcode.C),
			ModuleFactory.getModule("99", Code128Barcode.C),
			ModuleFactory.getModule("01", Code128Barcode.C),
			ModuleFactory.getModule("99", Code128Barcode.C)
		};
		assertTrue(Arrays.equals(expected, encoded));
	}

	public void testCheckDigitForASetIsInASet() throws Exception {
		barcode = new Code128Barcode("Code 128", Code128Barcode.A);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.A), barcode.calculateChecksum());
	}

	public void testCheckDigitForBSetIsInBSet() throws Exception {
		barcode = new Code128Barcode("Code 128", Code128Barcode.B);
		assertEquals(ModuleFactory.getModule("`", Code128Barcode.B), barcode.calculateChecksum());
	}

	public void testCheckDigitForCSetIsInCSet() throws Exception {
		barcode = new Code128Barcode("Code 128", Code128Barcode.C);
		assertEquals(ModuleFactory.getModule("05", Code128Barcode.C), barcode.calculateChecksum());
	}

	public void testMultipleCheckDigitCalculationsAreSame() throws Exception {
		barcode = new Code128Barcode("Code 128");
		Module check1 = barcode.calculateChecksum();
		Code128Barcode barcode2 = new Code128Barcode("Code 128");
		Module check2 = barcode2.calculateChecksum();
		assertEquals(check1, check2);
	}

	public void testCodeCCheckDigitIsInBSetForDigitsOnly() throws Exception {
		barcode = new Code128Barcode("21121976", Code128Barcode.C);
		assertEquals(ModuleFactory.getModule("\307", Code128Barcode.B), barcode.calculateChecksum());
	}

	public void testPreAmbleIsQuietZoneAndStartChar() throws Exception {
		CompositeModule module = (CompositeModule) barcode.getPreAmble();
		assertEquals(2, module.size());
	}

	public void testPostAmbleIsStopCharAndQuietZone() throws Exception {
		CompositeModule module = (CompositeModule) barcode.getPostAmble();
		assertEquals(2, module.size());
	}

	public void testQuietZoneWidthIsAtLeast10BarWidths() throws Exception {
		assertTrue(barcode.getPreAmble().widthInBars() > 10);
	}

	public void testCanShiftFromAToB() throws Exception {
		Code128Barcode barcode = new Code128Barcode("1\306i6", Code128Barcode.A);
		Module[] data = barcode.encodeData();
		assertEquals(4, data.length);
		assertEquals(ModuleFactory.getModule("1", Code128Barcode.A), data[0]);
		assertEquals(ModuleFactory.getModule("\306", Code128Barcode.A), data[1]);
		assertEquals(ModuleFactory.getModule("i", Code128Barcode.B), data[2]);
		assertEquals(ModuleFactory.getModule("6", Code128Barcode.A), data[3]);

		assertEquals(ModuleFactory.getModule("%", Code128Barcode.B), barcode.calculateChecksum());
	}

	public void testCanShiftFromBToA() throws Exception {
		Code128Barcode barcode = new Code128Barcode("1\3061i", Code128Barcode.B);
		Module[] data = barcode.encodeData();
		assertEquals(4, data.length);
		assertEquals(ModuleFactory.getModule("1", Code128Barcode.B), data[0]);
		assertEquals(ModuleFactory.getModule("\306", Code128Barcode.B), data[1]);
		assertEquals(ModuleFactory.getModule("1", Code128Barcode.A), data[2]);
		assertEquals(ModuleFactory.getModule("i", Code128Barcode.B), data[3]);

		assertEquals(ModuleFactory.getModule("J", Code128Barcode.B), barcode.calculateChecksum());
	}

	public void testCanCodeChangeFromAtoB() throws Exception {
		Code128Barcode barcode = new Code128Barcode("11\310ii", Code128Barcode.A);
		Module[] data = barcode.encodeData();
		assertEquals(5, data.length);
		assertEquals(ModuleFactory.getModule("1", Code128Barcode.A), data[0]);
		assertEquals(ModuleFactory.getModule("1", Code128Barcode.A), data[1]);
		assertEquals(ModuleFactory.getModule("\310", Code128Barcode.A), data[2]);
		assertEquals(ModuleFactory.getModule("i", Code128Barcode.B), data[3]);
		assertEquals(ModuleFactory.getModule("i", Code128Barcode.B), data[4]);

		assertEquals(ModuleFactory.getModule("q", Code128Barcode.B), barcode.calculateChecksum());
	}

	public void testCanCodeChangeFromAtoC() throws Exception {
		Code128Barcode barcode = new Code128Barcode("11\3071111", Code128Barcode.A);
		Module[] data = barcode.encodeData();
		assertEquals(5, data.length);
		assertEquals(ModuleFactory.getModule("1", Code128Barcode.A), data[0]);
		assertEquals(ModuleFactory.getModule("1", Code128Barcode.A), data[1]);
		assertEquals(ModuleFactory.getModule("\307", Code128Barcode.A), data[2]);
		assertEquals(ModuleFactory.getModule("11", Code128Barcode.C), data[3]);
		assertEquals(ModuleFactory.getModule("11", Code128Barcode.C), data[4]);

		assertEquals(ModuleFactory.getModule("C", Code128Barcode.B), barcode.calculateChecksum());
	}

	public void testCanCodeChangeFromBtoA() throws Exception {
		Code128Barcode barcode = new Code128Barcode("ii\31111", Code128Barcode.B);
		Module[] data = barcode.encodeData();
		assertEquals(5, data.length);
		assertEquals(ModuleFactory.getModule("i", Code128Barcode.B), data[0]);
		assertEquals(ModuleFactory.getModule("i", Code128Barcode.B), data[1]);
		assertEquals(ModuleFactory.getModule("\311", Code128Barcode.B), data[2]);
		assertEquals(ModuleFactory.getModule("1", Code128Barcode.A), data[3]);
		assertEquals(ModuleFactory.getModule("1", Code128Barcode.A), data[4]);

		assertEquals(ModuleFactory.getModule("Z", Code128Barcode.B), barcode.calculateChecksum());
	}

	public void testCanCodeChangeFromBtoC() throws Exception {
		Code128Barcode barcode = new Code128Barcode("11\3071111", Code128Barcode.B);
		Module[] data = barcode.encodeData();
		assertEquals(5, data.length);
		assertEquals(ModuleFactory.getModule("1", Code128Barcode.B), data[0]);
		assertEquals(ModuleFactory.getModule("1", Code128Barcode.B), data[1]);
		assertEquals(ModuleFactory.getModule("\307", Code128Barcode.B), data[2]);
		assertEquals(ModuleFactory.getModule("11", Code128Barcode.C), data[3]);
		assertEquals(ModuleFactory.getModule("11", Code128Barcode.C), data[4]);

		assertEquals(ModuleFactory.getModule("D", Code128Barcode.B), barcode.calculateChecksum());
	}

    public void testCanCodeChangeFromBToCWithMixedAlphaNum() throws Exception {
        Code128Barcode barcode = new Code128Barcode("BBQ" + Code128Barcode.CHANGE_TO_C + "012591");
		Module[] data = barcode.encodeData();
		assertEquals(7, data.length);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[0]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[1]);
		assertEquals(ModuleFactory.getModule("Q", Code128Barcode.B), data[2]);
		assertEquals(ModuleFactory.getModule("\307", Code128Barcode.B), data[3]);
		assertEquals(ModuleFactory.getModule("01", Code128Barcode.C), data[4]);
		assertEquals(ModuleFactory.getModule("25", Code128Barcode.C), data[5]);
		assertEquals(ModuleFactory.getModule("91", Code128Barcode.C), data[6]);

		assertEquals(ModuleFactory.getModule("Ç", Code128Barcode.B), barcode.calculateChecksum());
    }

    public void testCanCodeChangeFromBToCWithOddNumberOfCCharsAndMixedAlphaNum() throws Exception {
        Code128Barcode barcode = new Code128Barcode("BBQ" + Code128Barcode.CHANGE_TO_C + "0125913");
		Module[] data = barcode.encodeData();
		assertEquals(9, data.length);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[0]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[1]);
		assertEquals(ModuleFactory.getModule("Q", Code128Barcode.B), data[2]);
		assertEquals(ModuleFactory.getModule(Code128Barcode.CHANGE_TO_C, Code128Barcode.B), data[3]);
		assertEquals(ModuleFactory.getModule("01", Code128Barcode.C), data[4]);
		assertEquals(ModuleFactory.getModule("25", Code128Barcode.C), data[5]);
		assertEquals(ModuleFactory.getModule("91", Code128Barcode.C), data[6]);
        assertEquals(ModuleFactory.getModule(Code128Barcode.CHANGE_TO_B, Code128Barcode.C), data[7]);
        assertEquals(ModuleFactory.getModule("3", Code128Barcode.B), data[8]);

		assertEquals(ModuleFactory.getModule("H", Code128Barcode.B), barcode.calculateChecksum());
    }

	public void testCanCodeChangeFromCtoA() throws Exception {
		Code128Barcode barcode = new Code128Barcode("1111" + Code128Barcode.CHANGE_TO_A + "11", Code128Barcode.C);
		Module[] data = barcode.encodeData();
		assertEquals(5, data.length);
		assertEquals(ModuleFactory.getModule("11", Code128Barcode.C), data[0]);
		assertEquals(ModuleFactory.getModule("11", Code128Barcode.C), data[1]);
		assertEquals(ModuleFactory.getModule(Code128Barcode.CHANGE_TO_A, Code128Barcode.C), data[2]);
		assertEquals(ModuleFactory.getModule("1", Code128Barcode.A), data[3]);
		assertEquals(ModuleFactory.getModule("1", Code128Barcode.A), data[4]);

		assertEquals(ModuleFactory.getModule("o", Code128Barcode.B), barcode.calculateChecksum());
	}

	public void testCanCodeChangeFromCtoB() throws Exception {
		Code128Barcode barcode = new Code128Barcode("1111" + Code128Barcode.CHANGE_TO_B + "111", Code128Barcode.C);
		Module[] data = barcode.encodeData();
		assertEquals(6, data.length);
		assertEquals(ModuleFactory.getModule("11", Code128Barcode.C), data[0]);
		assertEquals(ModuleFactory.getModule("11", Code128Barcode.C), data[1]);
		assertEquals(ModuleFactory.getModule(Code128Barcode.CHANGE_TO_B, Code128Barcode.C), data[2]);
		assertEquals(ModuleFactory.getModule("1", Code128Barcode.B), data[3]);
		assertEquals(ModuleFactory.getModule("1", Code128Barcode.B), data[4]);
		assertEquals(ModuleFactory.getModule("1", Code128Barcode.B), data[4]);

		assertEquals(ModuleFactory.getModule("k", Code128Barcode.B), barcode.calculateChecksum());
	}

	public void testDrawPaintsBars() throws Exception {
		Code128Barcode barcode = new DataOnlyCode128("1");
		int barHeight = 44;
		barcode.setBarHeight(barHeight);
		barcode.setBarWidth(2);
		barcode.setDrawingText(false);
		GraphicsMock g = new GraphicsMock();
		barcode.draw(g, 0, 0);
		Color[] expectedColors = new Color[] {
			Color.white,
			Color.black,
			Color.white,
			Color.black,
			Color.white,
			Color.black,
			Color.white,
			Color.white,
			Color.white,
			g.getColor()
		};
		List colors = g.getColors();
		assertEquals(10, colors.size());
		for (int i=0; i<colors.size(); i++) {
			Color color = (Color) colors.get(i);
			assertEquals(expectedColors[i], color);
		}
		int[] expectedRects = new int[] {0, 2, 4, 6, 4, 4, 2, 0, 0};
		int x = 0;
		List rects = g.getRects();
		assertEquals(9, rects.size());
		for (int i=0; i<rects.size(); i++) {
			Rectangle rect = (Rectangle) rects.get(i);
			assertEquals(expectedRects[i], new Double(rect.getWidth()).intValue());
			assertEquals(barHeight, new Double(rect.getHeight()).intValue());
			assertEquals(x, new Double(rect.getX()).intValue());
			x += expectedRects[i];
		}
	}

	public void testStartCharDrawnAsFourTwoTwoFourTwoEight() throws Exception {
		Code128Barcode barcode = new Code128Barcode("259103702331");
		GraphicsMock g = new GraphicsMock();
		barcode.setDrawingText(false);
		barcode.draw(g, 0, 0);
		int[] expected = new int[] {4, 2, 2, 4, 2, 8};
		List rects = g.getRects();
		assertEquals(expected[0], new Double(((Rectangle) rects.get(1)).getWidth()).intValue());
		assertEquals(expected[1], new Double(((Rectangle) rects.get(2)).getWidth()).intValue());
		assertEquals(expected[2], new Double(((Rectangle) rects.get(3)).getWidth()).intValue());
		assertEquals(expected[3], new Double(((Rectangle) rects.get(4)).getWidth()).intValue());
		assertEquals(expected[4], new Double(((Rectangle) rects.get(5)).getWidth()).intValue());
		assertEquals(expected[5], new Double(((Rectangle) rects.get(6)).getWidth()).intValue());
	}

    public void testSizeCalculationIsSameForDrawnAndNotDrawnBarcode() throws Exception {
        Barcode b = new Code128Barcode("123456789012345678901209284390690347603740673047602730496734567890", Code128Barcode.B);
        b.setBarWidth(15);
        int unDrawnWidth = b.getWidth();
        BufferedImage image = new BufferedImage(5000, 5000, BufferedImage.TYPE_BYTE_GRAY);
        b.draw((Graphics2D) image.getGraphics(), 0, 0);
        int drawnWidth = b.getWidth();
        assertEquals(drawnWidth, unDrawnWidth);
    }

    public void testDrawingSmallBarcodeDoesNotDrawBarsTooSmall() throws Exception {
        Barcode b = new Code128Barcode("123456789012345678901209284390690347603740673047602730496734567890");
        boolean done = false;
        int width = 1;
        while (! done) {
            width = width - 1;
            b.setBarWidth(width);
            GraphicsMock g = new GraphicsMock();
            b.draw(g, 0, 0);
            List rects = g.getRects();
            for (Iterator iterator = rects.iterator(); iterator.hasNext();) {
                Rectangle rectangle = (Rectangle) iterator.next();
                assertTrue(rectangle.getWidth() >= 1);
            }
            if (width < 1) {
                done = true;
            }
        }
    }

	public void testPureCCodePrepends0IfOddLength() throws Exception {
		Code128Barcode b = new Code128Barcode("12345", Code128Barcode.C);
		Module[] data = b.encodeData();
		assertEquals(3, data.length);
		assertEquals(ModuleFactory.getModule("01", Code128Barcode.C), data[0]);
		assertEquals(ModuleFactory.getModule("23", Code128Barcode.C), data[1]);
		assertEquals(ModuleFactory.getModule("45", Code128Barcode.C), data[2]);

		assertEquals(ModuleFactory.getModule("q", Code128Barcode.B), b.calculateChecksum());
	}

	public void testOptimalEncodingDoesNotSwapToCForLessThan4Digits() throws Exception {
		Code128Barcode b = new Code128Barcode("BBQ123");
		Module[] data = b.encodeData();
		assertEquals(6, data.length);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[0]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[1]);
		assertEquals(ModuleFactory.getModule("Q", Code128Barcode.B), data[2]);
		assertEquals(ModuleFactory.getModule("1", Code128Barcode.B), data[3]);
		assertEquals(ModuleFactory.getModule("2", Code128Barcode.B), data[4]);
		assertEquals(ModuleFactory.getModule("3", Code128Barcode.B), data[5]);

		assertEquals(ModuleFactory.getModule("'", Code128Barcode.B), b.calculateChecksum());
	}

	public void testOptimalEncodingSwapsToCodeCForGroupsOf4Digits() throws Exception {
		Code128Barcode b = new Code128Barcode("BBQ1234");
		Module[] data = b.encodeData();
		assertEquals(6, data.length);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[0]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[1]);
		assertEquals(ModuleFactory.getModule("Q", Code128Barcode.B), data[2]);
		assertEquals(ModuleFactory.getModule(Code128Barcode.CHANGE_TO_C, Code128Barcode.B), data[3]);
		assertEquals(ModuleFactory.getModule("12", Code128Barcode.C), data[4]);
		assertEquals(ModuleFactory.getModule("34", Code128Barcode.C), data[5]);

		assertEquals(ModuleFactory.getModule("v", Code128Barcode.B), b.calculateChecksum());
	}

	public void testOptimalEncodingSwapsToCForMoreThan4Digits() throws Exception {
		Code128Barcode b = new Code128Barcode("BBQ1234567890");
		Module[] data = b.encodeData();
		assertEquals(9, data.length);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[0]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[1]);
		assertEquals(ModuleFactory.getModule("Q", Code128Barcode.B), data[2]);
		assertEquals(ModuleFactory.getModule(Code128Barcode.CHANGE_TO_C, Code128Barcode.B), data[3]);
		assertEquals(ModuleFactory.getModule("12", Code128Barcode.C), data[4]);
		assertEquals(ModuleFactory.getModule("34", Code128Barcode.C), data[5]);
		assertEquals(ModuleFactory.getModule("56", Code128Barcode.C), data[6]);
		assertEquals(ModuleFactory.getModule("78", Code128Barcode.C), data[7]);
		assertEquals(ModuleFactory.getModule("90", Code128Barcode.C), data[8]);

		assertEquals(ModuleFactory.getModule("Z", Code128Barcode.B), b.calculateChecksum());
	}

	public void testOptimalEncodingSwapsBackToBForLetters() throws Exception {
		Code128Barcode b = new Code128Barcode("BBQ123456BBQ");
		Module[] data = b.encodeData();
		assertEquals(11, data.length);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[0]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[1]);
		assertEquals(ModuleFactory.getModule("Q", Code128Barcode.B), data[2]);
		assertEquals(ModuleFactory.getModule(Code128Barcode.CHANGE_TO_C, Code128Barcode.B), data[3]);
		assertEquals(ModuleFactory.getModule("12", Code128Barcode.C), data[4]);
		assertEquals(ModuleFactory.getModule("34", Code128Barcode.C), data[5]);
		assertEquals(ModuleFactory.getModule("56", Code128Barcode.C), data[6]);
		assertEquals(ModuleFactory.getModule(Code128Barcode.CHANGE_TO_B, Code128Barcode.C), data[7]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[8]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[9]);
		assertEquals(ModuleFactory.getModule("Q", Code128Barcode.B), data[10]);

		assertEquals(ModuleFactory.getModule("~", Code128Barcode.B), b.calculateChecksum());
	}

	public void testOptimalEncodingShiftsToBForOddDigitsAtBoundary() throws Exception {
		Code128Barcode b = new Code128Barcode("BBQ12345B");
		Module[] data = b.encodeData();
		assertEquals(9, data.length);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[0]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[1]);
		assertEquals(ModuleFactory.getModule("Q", Code128Barcode.B), data[2]);
		assertEquals(ModuleFactory.getModule(Code128Barcode.CHANGE_TO_C, Code128Barcode.B), data[3]);
		assertEquals(ModuleFactory.getModule("12", Code128Barcode.C), data[4]);
		assertEquals(ModuleFactory.getModule("34", Code128Barcode.C), data[5]);
		assertEquals(ModuleFactory.getModule(Code128Barcode.CHANGE_TO_B, Code128Barcode.C), data[6]);
		assertEquals(ModuleFactory.getModule("5", Code128Barcode.B), data[7]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[8]);

		assertEquals(ModuleFactory.getModule("8", Code128Barcode.B), b.calculateChecksum());

		b = new Code128Barcode("BBQ12345");
		data = b.encodeData();
		assertEquals(8, data.length);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[0]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[1]);
		assertEquals(ModuleFactory.getModule("Q", Code128Barcode.B), data[2]);
		assertEquals(ModuleFactory.getModule(Code128Barcode.CHANGE_TO_C, Code128Barcode.B), data[3]);
		assertEquals(ModuleFactory.getModule("12", Code128Barcode.C), data[4]);
		assertEquals(ModuleFactory.getModule("34", Code128Barcode.C), data[5]);
		assertEquals(ModuleFactory.getModule(Code128Barcode.CHANGE_TO_B, Code128Barcode.C), data[6]);
		assertEquals(ModuleFactory.getModule("5", Code128Barcode.B), data[7]);

		assertEquals(ModuleFactory.getModule(";", Code128Barcode.B), b.calculateChecksum());
	}

	public void testOptimalEncodingShiftsToAForControlChars() throws Exception {
		Code128Barcode b = new Code128Barcode("BBQ\012BBQ");
		Module[] data = b.encodeData();
		assertEquals(8, data.length);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[0]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[1]);
		assertEquals(ModuleFactory.getModule("Q", Code128Barcode.B), data[2]);
		assertEquals(ModuleFactory.getModule(Code128Barcode.SHIFT, Code128Barcode.B), data[3]);
		assertEquals(ModuleFactory.getModule("\012", Code128Barcode.A), data[4]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[5]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[6]);
		assertEquals(ModuleFactory.getModule("Q", Code128Barcode.B), data[7]);

		assertEquals(ModuleFactory.getModule("Ã", Code128Barcode.B), b.calculateChecksum());
	}

	public void testComplexOptimalBarcodeUsesCorrectShiftsAndChanges() throws Exception {
		Code128Barcode b = new Code128Barcode("BBQ\012BBQ1234567890\01212345678BBQ123BBQ");
		Module[] data = b.encodeData();
		assertEquals(31, data.length);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[0]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[1]);
		assertEquals(ModuleFactory.getModule("Q", Code128Barcode.B), data[2]);
		assertEquals(ModuleFactory.getModule(Code128Barcode.SHIFT, Code128Barcode.B), data[3]);
		assertEquals(ModuleFactory.getModule("\012", Code128Barcode.A), data[4]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[5]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[6]);
		assertEquals(ModuleFactory.getModule("Q", Code128Barcode.B), data[7]);
		assertEquals(ModuleFactory.getModule(Code128Barcode.CHANGE_TO_C, Code128Barcode.B), data[8]);
		assertEquals(ModuleFactory.getModule("12", Code128Barcode.C), data[9]);
		assertEquals(ModuleFactory.getModule("34", Code128Barcode.C), data[10]);
		assertEquals(ModuleFactory.getModule("56", Code128Barcode.C), data[11]);
		assertEquals(ModuleFactory.getModule("78", Code128Barcode.C), data[12]);
		assertEquals(ModuleFactory.getModule("90", Code128Barcode.C), data[13]);
		assertEquals(ModuleFactory.getModule(Code128Barcode.CHANGE_TO_A, Code128Barcode.C), data[14]);
		assertEquals(ModuleFactory.getModule("\012", Code128Barcode.A), data[15]);
		assertEquals(ModuleFactory.getModule(Code128Barcode.CHANGE_TO_C, Code128Barcode.A), data[16]);
		assertEquals(ModuleFactory.getModule("12", Code128Barcode.C), data[17]);
		assertEquals(ModuleFactory.getModule("34", Code128Barcode.C), data[18]);
		assertEquals(ModuleFactory.getModule("56", Code128Barcode.C), data[19]);
		assertEquals(ModuleFactory.getModule("78", Code128Barcode.C), data[20]);
		assertEquals(ModuleFactory.getModule(Code128Barcode.CHANGE_TO_B, Code128Barcode.C), data[21]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[22]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[23]);
		assertEquals(ModuleFactory.getModule("Q", Code128Barcode.B), data[24]);
		assertEquals(ModuleFactory.getModule("1", Code128Barcode.B), data[25]);
		assertEquals(ModuleFactory.getModule("2", Code128Barcode.B), data[26]);
		assertEquals(ModuleFactory.getModule("3", Code128Barcode.B), data[27]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[28]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[29]);
		assertEquals(ModuleFactory.getModule("Q", Code128Barcode.B), data[30]);

		assertEquals(ModuleFactory.getModule("u", Code128Barcode.B), b.calculateChecksum());
	}

	public void testSpaceIsEncoded() throws Exception {
		Code128Barcode b = new Code128Barcode("   ");
		Module[] data = b.encodeData();
		assertEquals(3, data.length);

		assertEquals(ModuleFactory.getModule(" ", Code128Barcode.B), data[0]);
		assertEquals(ModuleFactory.getModule(" ", Code128Barcode.B), data[1]);
		assertEquals(ModuleFactory.getModule(" ", Code128Barcode.B), data[2]);
	}

	public void testSpacesEncodedUsingOptimal() throws Exception {
		Code128Barcode b = new Code128Barcode("BBQ\012BBQ 1234567890");
		Module[] data = b.encodeData();
		assertEquals(15, data.length);

		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[0]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[1]);
		assertEquals(ModuleFactory.getModule("Q", Code128Barcode.B), data[2]);
		assertEquals(ModuleFactory.getModule(Code128Barcode.SHIFT, Code128Barcode.B), data[3]);
		assertEquals(ModuleFactory.getModule("\012", Code128Barcode.A), data[4]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[5]);
		assertEquals(ModuleFactory.getModule("B", Code128Barcode.B), data[6]);
		assertEquals(ModuleFactory.getModule("Q", Code128Barcode.B), data[7]);
		assertEquals(ModuleFactory.getModule(" ", Code128Barcode.B), data[8]);
		assertEquals(ModuleFactory.getModule(Code128Barcode.CHANGE_TO_C, Code128Barcode.B), data[9]);
		assertEquals(ModuleFactory.getModule("12", Code128Barcode.C), data[10]);
		assertEquals(ModuleFactory.getModule("34", Code128Barcode.C), data[11]);
		assertEquals(ModuleFactory.getModule("56", Code128Barcode.C), data[12]);
		assertEquals(ModuleFactory.getModule("78", Code128Barcode.C), data[13]);
		assertEquals(ModuleFactory.getModule("90", Code128Barcode.C), data[14]);
	}

	/**
	 * This test case is getting pretty big. It is basically a dumping ground for programmatically
	 * checking all cases where bugs have been reported that specific data strings were not scannable.
	 * Verifies fix for bug: 772528. Also verifies that other reported encoding
	 * problems (with no bug numbers) are fixed.
	 */
	public void testCompressedBarcodeIsScannable() throws Exception {
		Code128Barcode b = new Code128Barcode("707FAAA3-D19A-B114");
		String[] expectedKey = new String[] {
			"7", "0", "7", "F", "A", "A", "A", "3", "-",
			"D", "1", "9", "A",	"-", "B", "1", "1", "4"
		};
		int[] expectedMode = new int[] {
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B
		};
		Module[] modules = b.encodeData();
		assertEquals(18, modules.length);
		for (int i=0; i<modules.length; i++) {
			assertEquals(ModuleFactory.getModule(expectedKey[i], expectedMode[i]), modules[i]);
		}

		b = new Code128Barcode("-0DAB-4B7C0446D15B");
		expectedKey = new String[] {
			"-", "0", "D", "A", "B", "-", "4", "B", "7",
			"C", Code128Barcode.CHANGE_TO_C, "04", "46",
			Code128Barcode.CHANGE_TO_B, "D", "1", "5", "B"
		};
		expectedMode = new int[] {
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B
		};
		modules = b.encodeData();
		assertEquals(18, modules.length);
		for (int i=0; i<modules.length; i++) {
			assertEquals(ModuleFactory.getModule(expectedKey[i], expectedMode[i]), modules[i]);
		}

		b = new Code128Barcode("0301A081710902001024.90 ");
		expectedKey = new String[] {
			Code128Barcode.CHANGE_TO_C, "03", "01", Code128Barcode.CHANGE_TO_B,
			"A", Code128Barcode.CHANGE_TO_C, "08", "17", "10", "90", "20", "01",
			"02", Code128Barcode.CHANGE_TO_B, "4", ".", "9", "0", " "
		};
		expectedMode = new int[] {
			Code128Barcode.B,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B
		};
		modules = b.encodeData();
		assertEquals(19, modules.length);
		for (int i=0; i<modules.length; i++) {
			assertEquals(ModuleFactory.getModule(expectedKey[i], expectedMode[i]), modules[i]);
		}

		b = new Code128Barcode("0301A081710902001024.90");
		expectedKey = new String[] {
			Code128Barcode.CHANGE_TO_C, "03", "01", Code128Barcode.CHANGE_TO_B,
			"A", Code128Barcode.CHANGE_TO_C, "08", "17", "10", "90", "20", "01",
			"02", Code128Barcode.CHANGE_TO_B, "4", ".", "9", "0"
		};
		expectedMode = new int[] {
			Code128Barcode.B,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B
		};
		modules = b.encodeData();
		assertEquals(18, modules.length);
		for (int i=0; i<modules.length; i++) {
			assertEquals(ModuleFactory.getModule(expectedKey[i], expectedMode[i]), modules[i]);
		}

        b = new Code128Barcode("A201343507");
		expectedKey = new String[] {
			"A", Code128Barcode.CHANGE_TO_C, "20", "13", "43", "50",
			Code128Barcode.CHANGE_TO_B, "7"
		};
		expectedMode = new int[] {
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.B,
		};
		modules = b.encodeData();
		assertEquals(8, modules.length);
		for (int i=0; i<modules.length; i++) {
			assertEquals(ModuleFactory.getModule(expectedKey[i], expectedMode[i]), modules[i]);
		}

		b = new Code128Barcode("A123456789");
		expectedKey = new String[] {
			"A", Code128Barcode.CHANGE_TO_C, "12", "34", "56", "78",
			Code128Barcode.CHANGE_TO_B, "9"
		};
		expectedMode = new int[] {
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.B,
		};
		modules = b.encodeData();
		assertEquals(8, modules.length);
		for (int i=0; i<modules.length; i++) {
			assertEquals(ModuleFactory.getModule(expectedKey[i], expectedMode[i]), modules[i]);
		}

		// TODO: Annoying - apparently this string doesn't scan when encoded, but it is encoded as I would expect
		b = new Code128Barcode("020145+K");
		expectedKey = new String[] {
			Code128Barcode.CHANGE_TO_C, "02", "01", "45",
			Code128Barcode.CHANGE_TO_B, "+", "K"
		};
		expectedMode = new int[] {
			Code128Barcode.B,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.C,
			Code128Barcode.B,
			Code128Barcode.B
		};
		modules = b.encodeData();
		assertEquals(7, modules.length);
		for (int i=0; i<modules.length; i++) {
			assertEquals(ModuleFactory.getModule(expectedKey[i], expectedMode[i]), modules[i]);
		}
	}

	public void testBCharacterSetEncodedBarcodeIsScannable() throws Exception {
		Code128Barcode b = new Code128Barcode("F123456789", Code128Barcode.B);
		String[] expectedKey = new String[] {
			"F", "1", "2", "3", "4", "5", "6", "7", "8", "9"
		};
		int[] expectedMode = new int[] {
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B,
			Code128Barcode.B
		};
		Module[] modules = b.encodeData();
		assertEquals(10, modules.length);
		for (int i=0; i<modules.length; i++) {
			assertEquals(ModuleFactory.getModule(expectedKey[i], expectedMode[i]), modules[i]);
		}
	}

	/**
	 * To ensure that bug 909533 does not re-occur
	 */
	public void testAlphaOnlyDataCanBeEncodedWithoutThrowingException() throws Exception {
		try {
			new Code128Barcode("Invalid data length", Code128Barcode.A);
		}
		catch (BarcodeException e) {
			fail();
		}
	}

	private class DataOnlyCode128 extends Code128Barcode {
		public DataOnlyCode128(String data) throws BarcodeException {
			super(data);
		}

		public Module calculateChecksum() {
			return new BlankModule(0);
		}

		protected Module getPreAmble() {
			return new BlankModule(0);
		}

		protected Module getPostAmble() {
			return new BlankModule(0);
		}
	}
}
