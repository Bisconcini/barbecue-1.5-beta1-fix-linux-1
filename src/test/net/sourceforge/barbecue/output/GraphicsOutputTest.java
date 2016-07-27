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
import net.sourceforge.barbecue.GraphicsMock;
import net.sourceforge.barbecue.env.DefaultEnvironment;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.util.List;

public class GraphicsOutputTest extends BarcodeTestCase {
	private Color fgColour;
	private Color bgColour;
	private GraphicsMock g;
	private GraphicsOutput output;

	protected void setUp() throws Exception {
		super.setUp();
		fgColour = Color.black;
		bgColour = Color.pink;
		g = new GraphicsMock();
		output = new GraphicsOutput(g, DefaultEnvironment.DEFAULT_FONT, fgColour, bgColour);
	}

	public void testDrawBarDrawsRectangle() throws Exception {
		output.drawBar(0, 0, 10, 100, true);
		List rects = g.getRects();
		assertEquals(1, rects.size());
		assertEquals(new Rectangle(0, 0, 10, 100), rects.get(0));
		assertEquals(g.getColor(), fgColour);
	}

	public void testDrawBarUsingBackgroundColourActuallyDrawsWithBackgroundColour() throws Exception {
		output.drawBar(0, 0, 10, 100, false);
		assertEquals(g.getColor(), bgColour);
	}

	public void testDrawTextFillsRectangleBehindText() throws Exception {
		String text = "FOO";
		TextLayout layout = new TextLayout(text, DefaultEnvironment.DEFAULT_FONT, g.getFontRenderContext());
		output.drawText(text, LabelLayoutFactory.createCenteredLayout(0, 0, 100));
		List rects = g.getRects();
		assertEquals(1, rects.size());
		assertEquals(new Rectangle(0, 0, 100, (int) layout.getBounds().getHeight() + CenteredLabelLayout.BARS_TEXT_VGAP + 1), rects.get(0));
	}

	public void testDrawTextRendersString() throws Exception {
		output.drawText("FOO", LabelLayoutFactory.createCenteredLayout(0, 0, 100));
		Rectangle r = g.getTextBounds();
		assertTrue(r.getWidth() > 0);
		assertTrue(r.getHeight() > 0);
	}

	public void testDrawTextReturnsHeightOfTextAreaDrawn() throws Exception {
		String text = "FOO";
		TextLayout layout = new TextLayout(text, DefaultEnvironment.DEFAULT_FONT, g.getFontRenderContext());
		int height = (int) layout.getBounds().getHeight() + CenteredLabelLayout.BARS_TEXT_VGAP + 1;
		assertEquals(height, (int) output.drawText(text, LabelLayoutFactory.createCenteredLayout(0, 0, 100)));
	}

	public void testBeginThenEndDrawRestoresoriginalGraphicsColour() throws Exception {
		Graphics2D g = (Graphics2D) new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_GRAY).getGraphics();
		g.setColor(Color.cyan);
		output = new GraphicsOutput(g, DefaultEnvironment.DEFAULT_FONT, fgColour, bgColour);
		output.beginDraw();
		output.endDraw(100, 100);
		assertEquals(Color.cyan, g.getColor());
    }

    public void testTextIsNotDrawnIfFontIsNull() throws Exception {
        output = new GraphicsOutput(g, null, fgColour, bgColour);
        double height = output.drawText("FOO", LabelLayoutFactory.createCenteredLayout(0, 0, 100));
        assertEquals(0, (int) height);
		assertNull(g.getTextBounds());
    }

	public void testTextHeightStillCalculatedWhenNotPainting() throws Exception {
		String text = "FOO";
		TextLayout layout = new TextLayout(text, DefaultEnvironment.DEFAULT_FONT, g.getFontRenderContext());
		int height = (int) layout.getBounds().getHeight() + CenteredLabelLayout.BARS_TEXT_VGAP + 1;
		output = new GraphicsOutput(g, DefaultEnvironment.DEFAULT_FONT, fgColour, bgColour);
		assertEquals(height, (int) output.drawText(text, LabelLayoutFactory.createCenteredLayout(0, 0, 100)));
	}
}
