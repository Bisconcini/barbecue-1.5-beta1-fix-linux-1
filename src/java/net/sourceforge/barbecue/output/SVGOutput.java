/***********************************************************************************************************************
 * Copyright (c) 2003, International Barcode Consortium
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 * Neither the name of the International Barcode Consortium nor the names of any contributors may be used to endorse
 * or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 ***********************************************************************************************************************/

package net.sourceforge.barbecue.output;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.DocType;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.awt.*;

/**
 * SVG outputter to output barcodes as SVG files.
 *
 * Contributed by Ryan Martell.
 *
 * @author Ryan Martell
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 */
public class SVGOutput extends AbstractOutput {
    
    private static final String DEFAULT_FAMILY = "Arial";
    private static final int DEFAULT_SIZE = 20;
    
    private static final String[] FONT_STYLES = new String[] {
        "font-style: normal", "font-weight: bold", "font-style: italic"
    };
    
    private String units;
    private final Writer writer;
    private Element root;
    private Document doc;
    
    /**
     * Creates a new instance of SVGOutput.
     * @param writer The Writer to output the SVG text to
     * @param font The font for text rendering (only if Barcode has drawText set to true)
     * @param fgColor Foreground color
     * @param bgColor Background color
     * @param scalar The scalar value to convert to units.  if barWidth is 1, and you want the
     * smallest bar to be 1/128 of an inch, this should be set to 1.0/128, and units
     * should be set to "in"
     * @param units The units for the scalar, above.  "in", "cm", "mm", "px" are acceptable values.
     */
    public SVGOutput(java.io.Writer writer, Font font, Color fgColor, Color bgColor, double scalar, String units) {
        super(font, true, scalar, fgColor, bgColor);
        this.writer = writer;
        this.units = units;
    }
    
    /**
     * From AbstractOutput - sets up the SVG output.
     */
    public void beginDraw() {
        root = createElement("svg");
        doc = new Document(root);
    }
    
    /**
     * From AbstractOutput - finished up the SVG output.
     * @param width The output width (in pixels) of the barcode
     * @param height The output height (in pixels) of the barcode.
     */
    public void endDraw(int width, int height) throws OutputException {
        root.setNamespace(Namespace.getNamespace("svg", "http://www.w3.org/2000/svg"));
        root.setAttribute("width", getScaledDimension(width));
        root.setAttribute("height", getScaledDimension(height));
        doc.setDocType(new DocType("svg", "-//W3C//DTD SVG 1.1//EN", "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd"));
        
        try {
            BufferedWriter bw = new BufferedWriter(writer);
            XMLOutputter outputter = new XMLOutputter();
            outputter.output(doc, bw);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new OutputException(e.getMessage(), e);
        }
    }
    
    protected Element createElement(String name) {
    	Element e = new Element(name);
        e.setNamespace(Namespace.getNamespace("svg", "http://www.w3.org/2000/svg"));
    	return e;
    }
    
    /**
     * From AbstractOutput - outputs the correct rectangle to the SVG output.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width
     * @param height the height
     * @param paintWithForegroundColor if true, use the foreground color, otherwise use the background color
     */
    public int drawBar(int x, int y, int width, int height, boolean paintWithForegroundColor) {
        Element rectElement = createElement("rect");
        rectElement.setAttribute("x", getScaledDimension(x));
        rectElement.setAttribute("y", getScaledDimension(y));
        rectElement.setAttribute("width", getScaledDimension(width));
        rectElement.setAttribute("height", getScaledDimension(height));
        rectElement.setAttribute("style", "fill:" + getColorAsCSS(paintWithForegroundColor ? foregroundColour : backgroundColour) + ";");
        root.addContent(rectElement);
        return width;
    }

	public int drawText(String text, LabelLayout layout) throws OutputException {
		Element textElement = createElement("text");
        textElement.setAttribute("x", getScaledDimension((int) layout.getBackgroundX()));
        textElement.setAttribute("y", getScaledDimension((int) layout.getBackgroundY()));
        textElement.setAttribute("style", constructStyleText());
        textElement.addContent(text);
        root.addContent(textElement);
		return 0;
	}

	public void paintBackground(int x, int y, int width, int height) {
        // uncertain if anything should occur here...
    }
    
    private String constructStyleText() {
        String family = DEFAULT_FAMILY;
        int size = DEFAULT_SIZE;
        int style = Font.PLAIN;
        
        if (font != null) {
            family = font.getFamily();
            size = font.getSize();
            style = font.getStyle();
        }
        
        StringBuffer buffer = new StringBuffer();
        buffer.append("font-family: ");
        buffer.append(family);
        buffer.append("; ");
        buffer.append("font-size: ");
        buffer.append(size);
        buffer.append("pt; ");
        buffer.append(getFontStyle(style));
        buffer.append("; ");
        return buffer.toString();
    }
    
    private String getFontStyle(int style) {
        if (style > FONT_STYLES.length && style >= 0) {
            return FONT_STYLES[Font.PLAIN];
        } else {
            return FONT_STYLES[style];
        }
    }
    
    private String getScaledDimension(int value) {
        return "" + (value * scalar + units);
    }
    
    private String getColorAsCSS(Color c) {
        StringBuffer sb = new StringBuffer("#");
        float [] components = c.getColorComponents(null);
        for (int ii = 0; ii < components.length; ii++) {
            String s = Integer.toHexString((int) (255 * components[ii])).toUpperCase();
            if (s.length() == 1) {
                sb.append('0');
            }
            sb.append(s);
        }
        return sb.toString();
    }
}
