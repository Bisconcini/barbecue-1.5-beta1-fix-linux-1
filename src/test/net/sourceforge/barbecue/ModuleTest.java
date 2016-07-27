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

package net.sourceforge.barbecue;

import junit.framework.TestCase;

import java.util.List;
import java.awt.*;
import java.awt.image.BufferedImage;

import net.sourceforge.barbecue.output.Output;
import net.sourceforge.barbecue.output.GraphicsOutput;

public class ModuleTest extends TestCase {
	
	private Output getTestDrawingEnvironment() {
		return new GraphicsOutput((Graphics2D) new BufferedImage(10, 10, 1).getGraphics(), null, Color.black, Color.white);
	}

	public void testDrawReturnsCorrectWidth() throws Exception {
		Module mod = new Module(new int[] {2, 2, 2, 1, 2, 4});
		assertEquals(221, mod.draw(getTestDrawingEnvironment(), 0, 0, 17, 0));
	}

	public void testDrawingEmptyDrawsNothing() throws Exception {
		GraphicsMock g = new GraphicsMock();
		Output environ= new GraphicsOutput(g, null, Color.black, Color.white);
		Module mod = new Module(new int[0]);
		mod.draw(environ, 0, 0, 2, 44);
		assertEquals(0, g.getRects().size());
	}

	public void testBarsAreDrawnCorrectly() throws Exception {
		GraphicsMock g = new GraphicsMock();
		Output environ= new GraphicsOutput(g, null, Color.black, Color.white);

		Module mod = new Module(new int[] {2, 4});
		mod.draw(environ, 0, 0, 2, 44);
		List rects = g.getRects();
		assertEquals(2, rects.size());
		assertEquals(0, new Double(((Rectangle) rects.get(0)).getX()).intValue());
		assertEquals(4, new Double(((Rectangle) rects.get(0)).getWidth()).intValue());
		assertEquals(4, new Double(((Rectangle) rects.get(1)).getX()).intValue());
		assertEquals(8, new Double(((Rectangle) rects.get(1)).getWidth()).intValue());
		assertEquals(44, new Double(((Rectangle) rects.get(1)).getHeight()).intValue());
	}

	public void testEqualsComparesBarWidths() throws Exception {
		Module mod = new Module(new int[] {2, 2, 2, 1, 2, 4});
		Module mod2 = new Module(new int[] {2, 2, 2, 1, 2, 4});
		assertEquals(mod, mod2);
	}

	public void testEqualsFailsForDifferentBarSpecs() throws Exception {
		Module mod = new Module(new int[] {2, 2, 2, 1, 2, 4});
		Module mod2 = new Module(new int[] {2, 2, 2, 1, 2, 3});
		assertFalse(mod.equals(mod2));
	}

	public void testEqualsFailsForWrongClass() throws Exception {
		assertFalse(new Module(new int[] {2, 2, 2, 1, 2, 4}).equals("foo"));
	}

	public void testHashCodesAreEqualIfEquals() throws Exception {
		Module mod = new Module(new int[] {2, 2, 2, 1, 2, 4});
		Module mod2 = new Module(new int[] {2, 2, 2, 1, 2, 4});
		assertEquals(mod.hashCode(), mod2.hashCode());
	}

	public void testHashCodesAreNotEqualsIfNotEquals() throws Exception {
		Module mod = new Module(new int[] {2, 2, 2, 1, 2, 4});
		Module mod2 = new Module(new int[] {2, 2, 2, 1, 4, 2});
		assertFalse(mod.hashCode() == mod2.hashCode());
	}

	public void testHashcodeIsSumOfAscendingMultiplicationOfBars() throws Exception {
		Module mod = new Module(new int[] {1, 1, 1, 1, 1});
		assertEquals(15, mod.hashCode());
	}

	public void testToStringIsBarDefinitions() throws Exception {
		Module mod = new Module(new int[] {1, 2, 3, 4, 5, 6});
		assertEquals("1, 2, 3, 4, 5, 6", mod.toString());
	}
}
