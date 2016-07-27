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

package net.sourceforge.barbecue.twod.pdf417;

import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BlankModule;
import net.sourceforge.barbecue.Module;
import net.sourceforge.barbecue.linear.LinearBarcode;
import net.sourceforge.barbecue.output.Output;
import net.sourceforge.barbecue.output.GraphicsOutput;
import net.sourceforge.barbecue.output.OutputException;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Implementation of the PDF417 two dimensional barcode format.
 *
 * <p/>Contributed by Alex Ferrer <alex@ftconsult.com>
 *
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 */
// TODO: Make this extend TwoDimensionalBarcode (and implement that) - NOT linear barcode
public class PDF417Barcode extends LinearBarcode {
	private PDF417Module module;
	
	/**
	 * Constructs a new new PDF417 barcode with the specified data.
	 * @param data The data to encode
	 * @throws BarcodeException If the data to be encoded is invalid
	 */
	public PDF417Barcode(String data) throws BarcodeException {
		super(data);
		this.barWidth = 1;
		this.drawingText = false;
	}

	/**
	 * Does nothing. Text is never drawn for PDF417.
	 * @param drawingText Ignored
	 */
	public void setDrawingText(boolean drawingText) {
		// Do nothing - we never draw the text for PDF417
	}

	/**
	 * Does nothing. Fixed width for PDF417.
	 * @param barWidth Ignored
	 */
	public void setBarWidth(int barWidth) {
		// Fixed width
	}

	/**
	 * Does nothing. Fixed height for PDF417.
	 * @param barHeight Ignored
	 */
	public void setBarHeight(int barHeight) {
		// Fixed height
	}

	/**
	 * Does nothing. Fixed resolution for PDF417.
	 * @param resolution Ignored
	 */
	public void setResolution(int resolution) {
		// Fixed res
	}

	/**
	 * Returns the minimum allowed height for the barcode for the given resolution.
	 * @param resolution The output resolution
	 * @return The minimum allowed barcode height
	 */
	protected int calculateMinimumBarHeight(int resolution) {
		initBarcode(data);
		return module.getBarcodeHeight();
	}

	/**
	 * Returns the encoded data for the barcode.
	 * @return An array of modules that represent the data as a barcode
	 */
	protected Module[] encodeData() {
		initBarcode(data);
		return new Module[] {new PDF417Module(data)};
	}

	/**
	 * Returns the checksum for the barcode, pre-encoded as a Module.
	 * @return A blank module
	 */
	protected Module calculateChecksum() {
		return new BlankModule(0);
	}

	/**
	 * Returns the pre-amble for the barcode.
	 * @return A blank module
	 */
	protected Module getPreAmble() {
		return new BlankModule(0);
	}

	/**
	 * Returns the post-amble for the barcode.
	 * @return A blank module
	 */
	protected Module getPostAmble() {
		return new BlankModule(0);
	}
	
	private void initBarcode(String data) {
		if (module == null) {
			this.module = new PDF417Module(data);
			Output params = new GraphicsOutput(
					(Graphics2D) new BufferedImage(1000, 1000,
					                 BufferedImage.TYPE_BYTE_GRAY).getGraphics(),
					null, Color.black, Color.white);
			try {
				module.draw(params, 0, 0, barWidth, barHeight);
			}
			catch (OutputException e) {
				// TODO: Something
			}
		}
	}
}
