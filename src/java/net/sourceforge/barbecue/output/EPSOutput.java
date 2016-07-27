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

import net.sourceforge.barbecue.env.DefaultEnvironment;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.awt.*;

/**
 * EPS outputter to output barcodes as Encapsulated Postscript  files.
 *
 * Contributed by Tim Molteno.
 *
 * @author <a href="mailto:tim@molteno.net">Tim Molteno</a>
 */
public class EPSOutput extends AbstractOutput {
    private StringBuffer epsHeader;
    private StringBuffer epsBody;
    private final Writer writer;
    private boolean backgroundDrawing = false;
    
    /**
     * Creates a new instance of EPSOutput.
     * @param writer The Writer to output the EPS text to
     */
    public EPSOutput(Writer writer) {
        super(DefaultEnvironment.DEFAULT_FONT, true, 1.0, Color.black, Color.white);
        this.writer = new BufferedWriter(writer);
        epsBody = new StringBuffer();
        epsHeader = new StringBuffer();
        backgroundDrawing = false;
    }
    
    public void beginDraw() {
    }
    
    /**
     * From AbstractOutput - finished up the EPS output.
     */
    public void endDraw(int width, int height) {
        epsHeader.setLength(0);
        epsHeader.append("%!PS-Adobe-2.0 EPSF-1.2\n");
        epsHeader.append("%%Creator: barbeque\n");
        epsHeader.append("%%BoundingBox: 0 0 ");
        epsHeader.append((int)getScaledDimension(width));
        epsHeader.append(" ");
        epsHeader.append((int)getScaledDimension(height));
        epsHeader.append("\n");
        epsHeader.append("%%EndComments\n");
        epsHeader.append("% Printing barcode for \"");
        epsHeader.append("\", scaled  1.00\n");
        
        try {
            writer.write(epsHeader.toString());
            writer.write(epsBody.toString());
            writer.write("% End barcode\n");
        }
        catch (java.io.IOException ex) {
            System.err.println("IO Exception writing EPS epilogue: " + ex.toString());
        }
        finally {
            try {
                writer.flush();
                writer.close();
            }
            catch (IOException e) {
                System.err.println("IO Exception closing EPS stream: " + e.toString());
            }
        }
    }
    
    /**
     * From AbstractOutput - outputs the correct rectangle to the EPS output.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width
     * @param height the height
     * @param paintWithForegroundColor if true, use the foreground color, otherwise use the background color
     */
    public int drawBar(int x, int y, int  width, int height, boolean paintWithForegroundColor) {
        return (int)drawBarEPS( x,  y,  width,  height,  paintWithForegroundColor);
    }
    
    /**
     * From AbstractOutput - outputs the correct rectangle to the EPS output.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width
     * @param height the height
     * @param paintWithForegroundColor if true, use the foreground color, otherwise use the background color
     */
    public double drawBarEPS(double x, double y, double width, double height, boolean paintWithForegroundColor) {
        //	Put a postscript comment in for the moment.
        //
        {
            epsBody.append("%");
        }
        epsBody.append("\t["); 	epsBody.append(getScaledDimension(height));
        epsBody.append("  "); 	epsBody.append(getScaledDimension(x));
        epsBody.append("  "); 	epsBody.append(getScaledDimension(y));
        epsBody.append("  "); 	epsBody.append(getScaledDimension(width));
        epsBody.append("]\n");
        
        if (true == paintWithForegroundColor && (false == backgroundDrawing)) {
            epsBody.append("newpath\n");
            epsBody.append(x);  epsBody.append(" ");  epsBody.append(y);  epsBody.append(" moveto\n");
            epsBody.append(0);  epsBody.append(" ");  epsBody.append(height);  epsBody.append(" rlineto\n");
            epsBody.append(width);  epsBody.append(" ");  epsBody.append(0);  epsBody.append(" rlineto\n");
            epsBody.append(0);  epsBody.append(" ");  epsBody.append(-height);  epsBody.append(" rlineto\n");
            epsBody.append("closepath\n");
            epsBody.append("fill\n\n");
        }
        
        return width;
    }
    
    public int drawText(String text, LabelLayout layout) throws OutputException {
        return 0;
    }
    
    public void toggleDrawingColor() {
        backgroundDrawing = !backgroundDrawing;
    }
    
    /**
     * Paint the background the background colour, based on the height and the width.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width to be painted
     * @param height the height to be painted
     */
    public void paintBackground(int x, int y, int width, int height) {
    }
    
    private double getScaledDimension(double value) {
        return scalar*value;
    }
}
