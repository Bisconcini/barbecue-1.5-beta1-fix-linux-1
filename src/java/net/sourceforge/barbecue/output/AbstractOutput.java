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

package net.sourceforge.barbecue.output;

import java.awt.*;

/**
 * Abstract class of which concrete implementations provide means for outputting
 * barcodes to different output formats.
 *
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 */
public abstract class AbstractOutput implements Output {

	/** Flag indicating whether the barcode will actually be outputted, or is just being sized. */
	protected final boolean painting;
	/** The scaling factor to correctly size the barcode in the output units. */
	protected final double scalar;
	/** The font to draw any text labels with. */
	protected final Font font;
	/** The background colour for drawing */
	protected Color backgroundColour;
	/** The foreground colour for drawing */
	protected Color foregroundColour;

	/**
	 * Populates this abstract outputter with common values.
	 * @param font The font to draw text labels with
	 * @param painting Flag indicating whether painting will actually occur
	 * @param scalar The scaling factor to size the barcode into the correct units
	 * @param foregroundColour The colour to paint in
	 * @param backgroundColour The background colour
	 */
	protected AbstractOutput(Font font, boolean painting,
							 double scalar, Color foregroundColour, Color backgroundColour) {
		this.painting = painting;
		this.scalar = scalar;
		this.font = font;
		this.foregroundColour = foregroundColour;
		this.backgroundColour = backgroundColour;
	}

	/**
	 * Disables any drawing to the Output. Useful for sizing calculations.
	 */
	public void toggleDrawingColor() {
		Color temp = foregroundColour;
		foregroundColour = backgroundColour;
		backgroundColour = temp;
	}
}
