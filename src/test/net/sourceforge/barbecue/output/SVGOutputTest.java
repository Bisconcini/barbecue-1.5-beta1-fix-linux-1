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

import net.sourceforge.barbecue.BarcodeTestCase;
import net.sourceforge.barbecue.env.DefaultEnvironment;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.font.TextLayout;
import java.awt.font.FontRenderContext;
import java.io.StringWriter;
import java.text.StringCharacterIterator;
import java.text.CharacterIterator;

public class SVGOutputTest extends BarcodeTestCase {
	private SVGOutput output;
	private StringWriter svg;
	private Color fgColour;
	private Color bgColour;
	public static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20001102//EN\" \"http://www.w3.org/TR/2000/CR-SVG-20001102/DTD/svg-20001102.dtd\">\n<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"10.0in\" height=\"10.0in\"";
	public static final String FOOTER = "</svg>";
	private LabelLayout centeredLayout;

	protected void setUp() throws Exception {
		super.setUp();
		svg = new StringWriter();
		fgColour = Color.black;
		bgColour = Color.white;
		output = new SVGOutput(svg, DefaultEnvironment.DEFAULT_FONT, fgColour, bgColour, 1, "in");
		output.beginDraw();
		centeredLayout = LabelLayoutFactory.createCenteredLayout(10, 10, 100);
		centeredLayout.setTextLayout(new TextLayout("FOO", DefaultEnvironment.DEFAULT_FONT, new FontRenderContext(new AffineTransform(0, 0, 0, 0, 0, 0), false, false)));
	}

	public void testBeginDrawWritesSVGHeader() throws Exception {
		output.endDraw(10, 10);
		String expected = HEADER + " />\n";
		assertSVGEquals(expected, svg.toString());
	}

	public void testUnitsAreCorrectInSVG() throws Exception {
		output = new SVGOutput(svg, DefaultEnvironment.DEFAULT_FONT, fgColour, bgColour, 1, "cm");
		output.beginDraw();
		output.endDraw(10, 10);
		assertSVGEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
					 "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20001102//EN\" \"http://www.w3.org/TR/2000/CR-SVG-20001102/DTD/svg-20001102.dtd\">\n<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"10.0cm\" height=\"10.0cm\" />", svg.toString().trim());
	}

	public void testDrawBarOutputsSVGRectangle() throws Exception {
		output.drawBar(10, 10, 1, 50, true);
		output.endDraw(10, 10);
		assertSVGEquals(HEADER + "><rect xmlns=\"\" x=\"10.0in\" y=\"10.0in\" width=\"1.0in\" height=\"50.0in\" style=\"fill:#000000;\" />" + FOOTER, svg.toString().trim());
	}

	public void testDrawBarInBackGroundColourChangesSVGFill() throws Exception {
		output.drawBar(10, 10, 1, 50, false);
		output.endDraw(10, 10);
		assertSVGEquals(HEADER + "><rect xmlns=\"\" x=\"10.0in\" y=\"10.0in\" width=\"1.0in\" height=\"50.0in\" style=\"fill:#FFFFFF;\" />" + FOOTER, svg.toString().trim());
	}

	public void testDrawTextOutputsSVGText() throws Exception {
		output.drawText("FOO", centeredLayout);
		output.endDraw(10, 10);
		assertSVGEquals(HEADER + "><text xmlns=\"\" x=\"10.0in\" y=\"10.0in\" style=\"font-family: Arial; font-size: 20pt; font-style: normal; \">FOO</text>" + FOOTER, svg.toString().trim());
	}

	public void testFontFaceIsReflectedInSVG() throws Exception {
		Font font = new Font("Times New Roman", Font.PLAIN, 20);
		output = new SVGOutput(svg, font, fgColour, bgColour, 1, "in");
		centeredLayout.setTextLayout(new TextLayout("FOO", font, new FontRenderContext(new AffineTransform(0, 0, 0, 0, 0, 0), false, false)));
		output.beginDraw();
		output.drawText("FOO", centeredLayout);
		output.endDraw(10, 10);
		assertSVGEquals(HEADER + "><text xmlns=\"\" x=\"10.0in\" y=\"10.0in\" style=\"font-family: Times New Roman; font-size: 20pt; font-style: normal; \">FOO</text>" + FOOTER, svg.toString());
	}

	public void testBoldFontIsDecoractedCorrectlyInSVG() throws Exception {
		Font font = new Font("Times New Roman", Font.BOLD, 20);
		output = new SVGOutput(svg, font, fgColour, bgColour, 1, "in");
		centeredLayout.setTextLayout(new TextLayout("FOO", font, new FontRenderContext(new AffineTransform(0, 0, 0, 0, 0, 0), false, false)));
		output.beginDraw();
		output.drawText("FOO", centeredLayout);
		output.endDraw(10, 10);
		assertSVGEquals(HEADER + "><text xmlns=\"\" x=\"10.0in\" y=\"10.0in\" style=\"font-family: Times New Roman; font-size: 20pt; font-weight: bold; \">FOO</text>" + FOOTER, svg.toString());
	}
	
	public void testItalicFontIsDecoractedCorrectlyInSVG() throws Exception {
		Font font = new Font("Times New Roman", Font.ITALIC, 20);
		output = new SVGOutput(svg, font, fgColour, bgColour, 1, "in");
		centeredLayout.setTextLayout(new TextLayout("FOO", font, new FontRenderContext(new AffineTransform(0, 0, 0, 0, 0, 0), false, false)));
		output.beginDraw();
		output.drawText("FOO", centeredLayout);
		output.endDraw(10, 10);
		assertSVGEquals(HEADER + "><text xmlns=\"\" x=\"10.0in\" y=\"10.0in\" style=\"font-family: Times New Roman; font-size: 20pt; font-style: italic; \">FOO</text>" + FOOTER, svg.toString());
	}

	public void testUnknownFontDecorationIsShownAsNormalInSVG() throws Exception {
		Font font = new Font("Times New Roman", 99, 20);
		output = new SVGOutput(svg, font, fgColour, bgColour, 1, "in");
		centeredLayout.setTextLayout(new TextLayout("FOO", font, new FontRenderContext(new AffineTransform(0, 0, 0, 0, 0, 0), false, false)));		
		output.beginDraw();
		output.drawText("FOO", centeredLayout);
		output.endDraw(10, 10);
		assertSVGEquals(HEADER + "><text xmlns=\"\" x=\"10.0in\" y=\"10.0in\" style=\"font-family: Times New Roman; font-size: 20pt; font-style: normal; \">FOO</text>" + FOOTER, svg.toString());
	}
    
    public void testDefaultFontInformationUsedIfFontIsNull() throws Exception {
        output = new SVGOutput(svg, null, fgColour, bgColour, 1, "in");
		output.beginDraw();
		output.drawText("FOO", centeredLayout);
		output.endDraw(10, 10);
		assertSVGEquals(HEADER + "><text xmlns=\"\" x=\"10.0in\" y=\"10.0in\" style=\"font-family: Arial; font-size: 20pt; font-style: normal; \">FOO</text>\n</svg>\n", svg.toString());
    }

	private void assertSVGEquals(String s1, String s2) {
		s1 = normalise(s1);
		s2 = normalise(s2);
		assertEquals(s1, s2);
	}

	private String normalise(String s) {
		StringBuffer buf = new StringBuffer();
		StringCharacterIterator iter = new StringCharacterIterator(s);
		for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
			if (Character.isDefined(c) && !Character.isISOControl(c)) {
				buf.append(c);
			}
		}
		return buf.toString();
	}
}
