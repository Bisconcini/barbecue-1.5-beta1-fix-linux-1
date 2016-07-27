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

import junit.framework.TestCase;
import net.sourceforge.barbecue.Module;

public class ModuleFactoryTest extends TestCase {

	public void testCorrectModuleReturnedForKey() throws Exception {
		assertEquals(new Module(new int[] {1, 3, 2, 2, 1, 2}), ModuleFactory.getModule("(", Code128Barcode.A));
	}

	public void testCorrectModuleReturnedForIndex() throws Exception {
		assertEquals(new Module(new int[] {1, 3, 2, 2, 1, 2}), ModuleFactory.getModuleForIndex(8, Code128Barcode.A));
	}

	public void testCorrectIndexReturnedForChar() throws Exception {
		assertEquals(8, ModuleFactory.getIndex("(", Code128Barcode.A));
	}

	public void testCharPositionsAreCorrect() throws Exception {
		assertEquals(58, ModuleFactory.getIndex("Z", Code128Barcode.B));
		assertEquals(7, ModuleFactory.getIndex("'", Code128Barcode.B));
		assertEquals(64, ModuleFactory.getIndex("`", Code128Barcode.B));
		assertEquals(94, ModuleFactory.getIndex("~", Code128Barcode.B));
	}
	
	public void testCSetHasCorrectConstants() throws Exception {
		for (int i=0; i<100; i++) {
			assertEquals(i, ModuleFactory.getIndex(makeDoubleChar(i), Code128Barcode.C));
			assertEquals(makeDoubleChar(i), ModuleFactory.getModuleForIndex(i, Code128Barcode.C).getSymbol());
		}
	}

	private String makeDoubleChar(int i) {
		if (i < 10) {
			return "0" + i;
		} else {
			return String.valueOf(i);
		}
	}
}
