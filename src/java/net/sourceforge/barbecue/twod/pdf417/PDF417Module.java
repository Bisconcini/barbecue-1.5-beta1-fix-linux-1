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

import net.sourceforge.barbecue.Module;
import net.sourceforge.barbecue.output.Output;
import net.sourceforge.barbecue.output.OutputException;

/**
 * Specific module implementation that draws an entire PDF417 barcode
 * as one barbecue module. This is not an ideal implementation, but was
 * the best way of integrating the PDF417 code short of re-writing it.
 *
 * <p/>Contributed by Alex Ferrer <alex@ftconsult.com>
 *
 * @author Alex Ferrer
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 *
 * @todo Do we really want to fix the DATACOLS to 12?
 */
public class PDF417Module extends Module {
	private static final int DATACOLS = 12;

	private final String data;
	private int[] out;
	private int outlen;
	private int outrows;
	private int col = 0;
	private int xp;
	private int yp;
	private int startX;
	private int wsize = 0;
	private int barWidth;

	/**
	 * Constructs the PDF417 barcode with the specified data.
	 * @param data The data to encode
	 */
	public PDF417Module(String data) {
		super(new int[0]);
		this.data = data;
	}

	/**
	 * Returns the barcode width;
	 * @return The barcode width
	 */
	private int getBarcodeWidth() {
		return wsize - startX;
	}

	/**
	 * Returns the barcode height.
	 * @return The barcode height
	 */
	int getBarcodeHeight() {
		return yp;
	}

	/**
	 * Draw the barcode to the specified outputter, at the specified origin.
	 * @param outputter The outputter
	 * @param x The X component of the origin
	 * @param y The Y component of the origin
	 * @param barWidth
	 * @param barHeight
	 * @return The total width drawn
	 */
	protected int draw(Output outputter, int x, int y, int barWidth, int barHeight) throws OutputException {
		this.xp = (int) x;
		this.startX = (int) x;
		this.yp = (int) y;
		this.barWidth = (int) barWidth;
		createCodewords(data.toCharArray(), data.length());
		createBits(out, outlen, outrows);
		encode(out, outrows, outputter);

		return getBarcodeWidth();
	}

	/**
	 * I have no idea what this does.
	 * @param data The barcode data
	 * @param length The length of the data
	 * @param ecLength The length of the EC (2)
	 */
	private void generateEC(int[] data, int length, int ecLength) {
		int b0 = 0;
		int b1 = 0;
		int g0 = 27;
		int g1 = 917;  /* (x-3)(x-9) = x^2+917x+27 mod 929 */

		/* Initialize */
		data[length] = 0;
		data[length + 1] = 0;

		/* We only know ecLength == 2 for now */
		if (ecLength != 2) {
			return;
		}

		/* Load up with data */
		for (int i = 0; i < length; ++i) {
			int wrap = (b1 + data[i]) % 929;

			if (wrap != 0) {
				wrap = 929 - wrap;
			}

			b1 = (b0 + g1 * wrap) % 929;
			b0 = (0 + g0 * wrap) % 929;
		}

		/* Read off the info */
		if (b0 != 0) {
			b0 = 929 - b0;
		}

		if (b1 != 0) {
			b1 = 929 - b1;
		}

		data[length] = b1;
		data[length + 1] = b0;
	}

	private void outbit(int bit, Output params) throws OutputException {
		params.drawBar(xp, yp, 1, 1, bit == 1);

		xp = xp + barWidth;
		if (col++ == wsize - 1) {
			col = 0;
			yp = yp + 1;
			xp = startX;
		}
	}

	private void createCodewords(char[] data, int len) {
		int ecLength = 2; /* Number of codewords for error correction */
		/* Check args */

		if (DATACOLS < 1 || DATACOLS > 30) {
			return;
		}
		/* Calculate the length of the eventual sequence */
		outlen = 2 + (len / 6) * 5 + (len % 6) + ecLength;

		/* Pad to an integer number of rows, at least 3 */
		outrows = outlen / DATACOLS;
		if ((outlen % DATACOLS) != 0) {
			++outrows;
		}
		if (outrows < 3) {
			outrows = 3;
		}
		if (outrows > 90) {
			return;
		}
		outlen = outrows * DATACOLS;
		/* We don't do multipart symbols (Macro PDF 417) */
		if (outlen > 928) {
			return;
		}

		/* The first two codewords are the length and the BC mode latch
		   The mode latch is 924 if len is a multiple of 6, 901 otherwise */
		out = new int[outlen];              // dimension the array
		out[0] = 2 + (len / 6) * 5 + (len % 6);   // 1st value s size of sequence
		if (len % 6 != 0) {
			out[1] = 901;  					// if len not a multiple of 6
		} else {
			out[1] = 924;                    // if len *is* a multiple of 6
		}

		/* Map blocks of 6 bytes to block of 5 codewords */
		int inp = 0;
		int outp = 2;
		while (inp + 5 < len) {
			/* Treat the 6 bytes as a big-endian base 256 number */
			long codeval = 0;
			for (int i = 0; i < 6; ++i) {
				codeval <<= 8;
				codeval += data[inp++];
			}
			/* Convert the number to base 900 */
			for (int i = 0; i < 5; i++) {
				out[outp + 4 - i] = new Long(codeval % 900).intValue();
				codeval /= 900;
			}
			outp += 5;
		}

		/* Finish up the data */
		while (inp < len) {
			out[outp++] = data[inp++];
		}

		/* Do padding */
		while (outp < outlen - ecLength) {
			out[outp++] = 900;
		}

		generateEC(out, outp, ecLength);
	}

	private void createBits(int[] codes, int codelen, int datarows) {
		int row, inp, outp;
		if (DATACOLS < 1 || DATACOLS > 30
		 				 || datarows < 3 || datarows > 90
		 				 || codelen != DATACOLS * datarows) {
			return;
		}
		/* Each row has start, left, data, right, stop */
		int outlen = datarows * (DATACOLS + 4);
		int[] out = new int[outlen];
		outp = 0;
		inp = 0;

		for (row = 0; row < datarows; ++row) {
			/* Do each row */
			int v = DATACOLS - 1;
			int w = row % 3;
			int x = row / 3;
			int y = datarows / 3;
			int z = 0 * 3 + datarows % 3;  /* The 0 is the error correction level */
			out[outp++] = PDF417Data.PDF417_START;
			switch (w) {
				case 0:
					out[outp++] = PDF417Data.PDF417_BITS[w][30 * x + y];
					break;
				case 1:
					out[outp++] = PDF417Data.PDF417_BITS[w][30 * x + z];
					break;
				case 2:
					out[outp++] = PDF417Data.PDF417_BITS[w][30 * x + v];
					break;
			}
			for (int i = 0; i < DATACOLS; ++i) {
				out[outp++] = PDF417Data.PDF417_BITS[w][codes[inp++]];
			}
			switch (w) {
				case 0:
					out[outp++] = PDF417Data.PDF417_BITS[w][30 * x + v];
					break;
				case 1:
					out[outp++] = PDF417Data.PDF417_BITS[w][30 * x + y];
					break;
				case 2:
					out[outp++] = PDF417Data.PDF417_BITS[w][30 * x + z];
					break;
			}
			out[outp++] = PDF417Data.PDF417_STOP;
		}
		this.out = out;
		this.outlen = outlen;
	}

	private void encode(int[] data, int datarows, Output params) throws OutputException {
		int bitpattern;
		int row_height = 7;
		int npix = 2;
		wsize = ((DATACOLS + 4) * 17 + barWidth + 4) * npix;

		/* Top quiet zone */
		for (int i = 0; i < 2 * npix; i++) {
			for (int j = 0; j < ((DATACOLS + 4) * 17 + 1 + 4) * npix; j++) {
				outbit(0, params);
			}
		}

		for (int i = 0; i < datarows; i++) {
			for (int k = 0; k < row_height; k++) {

				/* Left quiet zone */
				for (int pixn = 0; pixn < 2 * npix; pixn++) {
					outbit(0, params);
				}

				for (int j = 0; j < (DATACOLS + 4); j++) {
					bitpattern = data[(DATACOLS + 4) * i + j];

					for (int bitm = 16; bitm >= 0; bitm--) {
						for (int pixn = 0; pixn < npix; pixn++) {

							if ((bitpattern & (1 << bitm)) != 0) {
								outbit(1, params);
							} else {
								outbit(0, params);
							}
						}
					}
				}

				for (int pixn = 0; pixn < npix; pixn++) {
					outbit(1, params);
				}

				/* Right quiet zone */
				for (int pixn = 0; pixn < 2 * npix; pixn++) {
					outbit(0, params);
				}
			}
		}

		/* Bottom quiet zone */
		for (int i = 0; i < 2 * npix; ++i) {
			for (int j = 0; j < ((DATACOLS + 4) * 17 + 1 + 4) * npix; ++j) {
				outbit(0, params);
			}
		}
	}
}
