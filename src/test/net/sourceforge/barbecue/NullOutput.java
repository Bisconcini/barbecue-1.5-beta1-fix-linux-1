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
import net.sourceforge.barbecue.output.LabelLayout;
import net.sourceforge.barbecue.output.OutputException;

public class NullOutput implements Output {

	/** Draws a bar (or outputs the SVG) at the given coordinates.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param width the width
	 * @param height the height
	 * @param foregroundColor if true, use the foreground color, otherwise use the background color
	 */
	public int drawBar(int x, int y, int width, int height, boolean foregroundColor) {
		return width;
	}

	/**
	 *  Sets up the drawing environment.  Called before drawBar().  Matched with call to
	 * endDraw() at the end.  This allows for caching as needed.
	 */
	public void beginDraw() {
	}

	/**
	 * Balanced with startDraw() above, used for caching, output of epilogues (for
	 * SVG), etc.
	 * @param width The output width (in pixels) of the barcode
	 * @param height The output height (in pixels) of the barcode.
	 */
	public void endDraw(int width, int height) {
	}

	public int drawText(String text, LabelLayout layout) throws OutputException {
		return 0;
	}

    public void paintBackground(int x, int y, int width, int height) {
        // uncertain if anything should occur here...
    }

	public void toggleDrawingColor() {
		// Do nothing
	}
}
