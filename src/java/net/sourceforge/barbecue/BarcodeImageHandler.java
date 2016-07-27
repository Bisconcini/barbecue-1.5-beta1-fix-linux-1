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

import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.*;
import net.sourceforge.barbecue.output.OutputException;

/**
 * Utility class to provide convenience methods for converting barcodes
 * to images and other misc barcode handling.
 *
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 * @author Sean Sullivan
 * 
 */
public final class BarcodeImageHandler {

	private static Set formats;
	
	static
	{
		Set s = new HashSet();
		s.add("gif");
		s.add("jpeg");
		s.add("png");
		formats = Collections.unmodifiableSet(s);
	}
	
	static public Set getImageFormats()
	{
		return formats;
	}
	
	private BarcodeImageHandler() {
	}


	/**
	 * Creates an image for a barcode that can be used in other GUI
	 * components (e.g. ImageIcon, JLabel etc).
	 * @param barcode The barcode to convert into an image
	 * @return The image
	 */
	public static BufferedImage getImage(Barcode barcode) throws OutputException {
        BufferedImage bi = new BufferedImage(barcode.getWidth(), barcode.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
        Graphics2D g = bi.createGraphics();
        barcode.draw(g, 0, 0);
        bi.flush();
        return bi;
    }

	/**
	 * write a JPEG image to an OutputStream
	 * 
	 * @param barcode The barcode to output
	 * @param os The output stream to write the image to
	 * @throws OutputException If the image cannot be written to the output stream correctly
	 */
	public static void writeJPEG(Barcode barcode, OutputStream os) throws OutputException {
		writeImage(barcode, "jpeg", os);
	}

	public static void saveJPEG(Barcode barcode, File f) throws OutputException {
		saveImage(barcode, "jpeg", f);
	}

	public static void savePNG(Barcode barcode, File f) throws OutputException {
		saveImage(barcode, "png", f);
	}
	
	public static void saveGIF(Barcode barcode, File f) throws OutputException {
		saveImage(barcode, "gif", f);
	}
	
	private static void saveImage(Barcode barcode, String format, File f) throws OutputException {
	
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(f);
			writeImage(barcode, format, fos);
			fos.flush();
		}
		catch (IOException ex) {
			throw new OutputException(ex);
		}
		finally {
			try {
				fos.close();
			}
			catch (IOException ignored) {
				// ignored
			}
		}
	}
	
	
	/**
	 * write a PNG image to an OutputStream
	 * 
	 * @param barcode The barcode to output
	 * @param os The output stream to write the image to
	 * @throws OutputException If the image cannot be written to the output stream correctly
	 */
	public static void writePNG(Barcode barcode, OutputStream os) throws OutputException {
		writeImage(barcode, "png", os);
	}

	/**
	 * write a GIF image to an OutputStream
	 * 
	 * @param barcode The barcode to output
	 * @param os The output stream to write the image to
	 * @throws OutputException If the image cannot be written to the output stream correctly
	 */
	public static void writeGIF(Barcode barcode, OutputStream os) throws OutputException {
		writeImage(barcode, "gif", os);
	}
	
	
	/**
	 * 
	 * @param barcode The barcode to output
	 * @param formatName
	 * @param os The output stream
	 * @throws OutputException If an error occurred writing to the stream
	 */
	private static void writeImage(Barcode barcode, String formatName, OutputStream os) throws OutputException {
		BufferedImage image = getImage(barcode);
		try {
			ImageIO.write(image, formatName, os);
		}
		catch (IOException e) {
			throw new OutputException("exception while writing image", e);
		}
	}
}
