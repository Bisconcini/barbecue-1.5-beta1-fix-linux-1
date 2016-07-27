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

import net.sourceforge.barbecue.output.Output;
import net.sourceforge.barbecue.output.OutputException;

/**
 * Specific implementation of Module that draws a blank bar (of configurable width).
 * This can be used to insert inter-module spaces (as required by some barcode formats)
 * without having to modify the data to be encoded to include the spaces.
 * <p/>Note: You should not instantiate this class directly.
 *
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 */
public class SeparatorModule extends Module {

	/**
	 * Constructs a new SeparatorModule with a width of 1.
	 */
	public SeparatorModule() {
		super(new int[] {1});
	}

	/**
	 * Constructs a new SeparatorModule with the specified width.
	 * @param width The width of the separator in bar widths.
	 */
	public SeparatorModule(int width) {
		super(new int[] {width});
	}

	/**
	 * Draws the module to the specified output.
	 * @param output The barcode output to draw to
	 * @param x The starting X co-ordinate
	 * @param y The starting Y co-ordinate
	 * @param barWidth
	 * @param barHeight
	 * @return The total width drawn
	 */
	protected int draw(Output output, int x, int y, int barWidth, int barHeight) throws OutputException {
		int w = bars[0] * barWidth;
		output.drawBar((int) x, (int) y, (int) w, (int) barHeight, false);
		return w;
	}

	/**
	 * Returns the symbol that this module encodes.
	 * @return A blank string
	 */
	public String getSymbol() {
		return "";
	}
}
