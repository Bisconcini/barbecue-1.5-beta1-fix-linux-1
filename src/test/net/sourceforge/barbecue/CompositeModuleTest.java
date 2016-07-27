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

import java.awt.*;
import java.awt.image.BufferedImage;

import net.sourceforge.barbecue.output.Output;
import net.sourceforge.barbecue.output.GraphicsOutput;

public class CompositeModuleTest extends TestCase {
	private ModuleMock m1;
	private ModuleMock m2;
	private ModuleMock m3;
	private CompositeModule mod;

	protected void setUp() throws Exception {
		super.setUp();
		m1 = new ModuleMock(new int[0]);
		m2 = new ModuleMock(new int[0]);
		m3 = new ModuleMock(new int[0]);
		mod = new CompositeModule();
		mod.add(m1);
		mod.add(m2);
		mod.add(m3);
	}
	
	private Output getTestDrawingEnvironment() {
		return new GraphicsOutput((Graphics2D) new BufferedImage(1, 1, 1).getGraphics(), null, Color.black, Color.white);
	}

	public void testAllSubModulesAreDrawn() throws Exception {
		mod.draw(getTestDrawingEnvironment(), 0, 0, 0, 0);
		assertTrue(m1.wasDrawn());
		assertTrue(m2.wasDrawn());
		assertTrue(m3.wasDrawn());
	}

	public void testReportedWidthIsSumOfSubModuleWidths() throws Exception {
		assertEquals(3, (int) mod.draw(getTestDrawingEnvironment(), 0, 0, 1, 1));
	}

	public void testGetSymbolConcatenatesChildModuleSymbols() throws Exception {
		assertEquals(concat(new Module[] {m1, m2, m3}), mod.getSymbol());
	}

	private String concat(Module[] modules) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < modules.length; i++) {
			Module module = modules[i];
			buf.append(module.getSymbol());
		}
		return buf.toString();
	}

	private class ModuleMock extends Module {
		private boolean drawn;

		public ModuleMock(int[] bars) {
			super(bars);
			drawn = false;
		}

		public int draw(Output params, int x, int y, int barWidth, int barHeight) {
			drawn = true;
			return 1;
		}

		public boolean wasDrawn() {
			return drawn;
		}
	}
}
