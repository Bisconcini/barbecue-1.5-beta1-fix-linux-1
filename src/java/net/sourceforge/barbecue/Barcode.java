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

package net.sourceforge.barbecue;

import net.sourceforge.barbecue.env.EnvironmentFactory;
import net.sourceforge.barbecue.env.HeadlessEnvironment;
import net.sourceforge.barbecue.output.GraphicsOutput;
import net.sourceforge.barbecue.output.Output;
import net.sourceforge.barbecue.output.OutputException;
import net.sourceforge.barbecue.output.SizingOutput;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * Abstract barcode class that provides functionality that is common to
 * all barcodes. Specific barcode implementations must subclass
 * this and provide information specific to the barcode type they are implementing.
 *
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 */
public abstract class Barcode extends JComponent 
	implements Printable {

    private static final int DEFAULT_BAR_HEIGHT = 50;

    protected String data;
    protected String label;
    protected boolean drawingText;
    protected boolean drawingQuietSection = true;
    protected int barWidth = 2;
    protected int barHeight;
    private Font font;
    private Dimension size;
    private int x;
    private int y;
    private int resolution = -1;

	protected Barcode(String data) throws BarcodeException {
        if (data == null || data.length() == 0) {
            throw new BarcodeException("Data to encode cannot be empty");
        }
        this.data = data;
        int minHeight = calculateMinimumBarHeight(getResolution());
        if (minHeight > 0) {
            this.barHeight = minHeight;
        } else {
            this.barHeight = Barcode.DEFAULT_BAR_HEIGHT;
        }
        
      	// MINHA ALTERACAO COMECA AQUI
        //this.font = EnvironmentFactory.getEnvironment().getDefaultFont();
        Font f = new Font("Arial", Font.PLAIN, 18);
        this.font = f;
        // MINHA ALTERACAO TERMINA AQUI

        this.drawingText = true;
        setBackground(Color.white);
        setForeground(Color.black);
        setOpaque(true);

        
        invalidateSize();
    }

    /**
     * Returns the data that the barcode is coding for.
     *
     * @return The barcode raw data
     */
    public String getData() {
        return data;
    }

    /**
     * Sets the font to use when drawing the barcode data string underneath the barcode.
     * <p/> Note that changing this setting after a barcode has been drawn will invalidate the
     * component and may force a refresh.
     *
     * @param font The font to use
     */
    public void setFont(Font font) {
        this.font = font;
        invalidateSize();
    }

    /**
     * Indicates whether the barcode data should be shown as a string underneath the
     * barcode or not.
     * <p/> Note that changing this setting after a barcode has been drawn will invalidate the
     * component and may force a refresh.
     *
     * @param drawingText True if the text should be shown, false if not
     */
    public void setDrawingText(boolean drawingText) {
        this.drawingText = drawingText;
        invalidateSize();
    }

    /**
     * Indicates whether the barcode is drawing a text label underneath the barcode or not.
     *
     * @return True if the text is drawn, false otherwise
     */
    public boolean isDrawingText() {
        return drawingText;
    }

    /**
     * Indicates whether the leading and trailing white space should be rendered.
     * <p/> Note that changing this setting after a barcode has been drawn will invalidate the
     * component and may force a refresh.
     *
     * @param drawingQuietSection True if the quiet area/white space should be shown, false if not
     */
    public void setDrawingQuietSection(boolean drawingQuietSection) {
        this.drawingQuietSection = drawingQuietSection;
        invalidateSize();
    }

    /**
     * Indicates whether the barcode is drawing leading and trailing white space/quiet area.
     *
     * @return True if the quiet area/white space is drawn, false otherwise
     */
    public boolean isDrawingQuietSection() {
        return drawingQuietSection;
    }

    /**
     * Sets the desired bar width for the barcode. This is the width (in pixels) of the
     * thinnest bar in the barcode. Other bars will change their size relative to this.
     * <p/> Note that changing this setting after a barcode has been drawn will invalidate the
     * component and may force a refresh.
     *
     * @param barWidth The desired width of the thinnest bar in pixels
     */
    public void setBarWidth(int barWidth) {
        if (barWidth >= 1) {
            this.barWidth = barWidth;
        } else {
            this.barWidth = 1;
        }
        invalidateSize();
    }

    /**
     * Sets the desired height for the bars in the barcode (in pixels). Note that some
     * barcode implementations will not allow the height to go below a minimum size. This
     * is not the height of the component as a whole, as it does not specify the height of
     * any text that may be drawn and does not include borders.
     * <p/> Note that changing this setting after a barcode has been drawn will invalidate the
     * component and may force a refresh.
     *
     * @param barHeight The desired height of the barcode bars in pixels
     */
    public void setBarHeight(int barHeight) {
        // There is a minimum bar height that we must enforce
        if (barHeight > calculateMinimumBarHeight(getResolution())) {
            this.barHeight = barHeight;
            invalidateSize();
        }
    }

    /**
     * Sets the desired output resolution for the barcode. This method should
     * be used in cases where the barcode is either being outputted to a device
     * other than the screen, or the barcode is being generated on a headless
     * machine (e.g. a rack mounted server) and the screen resolution cannot be
     * determined. Note that is the barcode is generated in either of these situations
     * and this method has not been called, the resolution is assumed to be 72 dots
     * per inch.
     *
     * @param resolution The desired output resolution (in dots per inch)
     */
    public void setResolution(int resolution) {
        if (resolution > 0) {
            this.resolution = resolution;
            int newHeight = calculateMinimumBarHeight(getResolution());
            if (newHeight > this.barHeight) {
                this.barHeight = newHeight;
            }
            invalidateSize();
        }
    }

    /**
     * From {@link javax.swing.JComponent JComponent}.
     *
     * @return The X co-ordinate of the component's origin
     */
    public int getX() {
        return x;
    }

    /**
     * From {@link javax.swing.JComponent JComponent}.
     *
     * @return The Y co-ordinate of the component's origin
     */
    public int getY() {
        return y;
    }

    /**
     * From {@link javax.swing.JComponent JComponent}.
     *
     * @return The width of this component
     */
    public int getWidth() {
        return (int) getActualSize().getWidth();
    }

    /**
     * From {@link javax.swing.JComponent JComponent}.
     *
     * @return The height of this component
     */
    public int getHeight() {
        return (int) getActualSize().getHeight();
    }

    /**
     * From {@link javax.swing.JComponent JComponent}.
     *
     * @return The bounds of this component
     */
    public Rectangle getBounds() {
        return getBounds(new Rectangle());
    }

    /**
     * From {@link javax.swing.JComponent JComponent}.
     *
     * @param rv The rectangle to set the bounds on
     * @return The updated rv
     */
    public Rectangle getBounds(Rectangle rv) {
        rv.setBounds(getX(), getY(), (int) getActualSize().getWidth() + getX(),
                (int) getActualSize().getHeight() + getY());
        return rv;
    }

    /**
     * From {@link javax.swing.JComponent JComponent}.
     *
     * @return The preferred size of this component
     */
    public Dimension getPreferredSize() {
        return getActualSize();
    }

    /**
     * From {@link javax.swing.JComponent JComponent}.
     *
     * @return The minimum size of this component
     */
    public Dimension getMinimumSize() {
        return getActualSize();
    }

    /**
     * From {@link javax.swing.JComponent JComponent}.
     *
     * @return The maximum size of this component
     */
    public Dimension getMaximumSize() {
        return getActualSize();
    }

    /**
     * From {@link javax.swing.JComponent JComponent}.
     *
     * @return The actual size of this component
     */
    public Dimension getSize() {
        return getActualSize();
    }

    /**
     * Renders this <code>Barcode</code> at the specified location in
     * the specified {@link java.awt.Graphics2D Graphics2D} context.
     * The origin of the layout is placed at x,&nbsp;y.  Rendering may touch
     * any point within <code>getBounds()</code> of this position.  This
     * leaves the <code>g2</code> unchanged.
     *
     * @param g The graphics context
     * @param x The horizontal value of the upper left co-ordinate of the bounding box
     * @param y The vertical value of the upper left co-ordinate of the bounding box
     */
    public void draw(Graphics2D g, int x, int y) throws OutputException {
        this.x = x;
        this.y = y;

        Output output = new GraphicsOutput(g, font, getForeground(), getBackground());
        size = draw(output, x, y, barWidth, barHeight);
    }

    public void output(Output output) throws OutputException {
        draw(output, 0, 0, barWidth, barHeight);
    }

    protected abstract Module[] encodeData();

    protected abstract Module calculateChecksum();

    protected abstract Module getPreAmble();

    protected abstract Module getPostAmble();

    protected abstract Dimension draw(Output output, int x, int y, int barWidth, int barHeight) throws OutputException;

    /**
     * Returns the text that will be displayed underneath the barcode (if requested).
     *
     * @return The text label for the barcode
     */
    public String getLabel() {
        if (label != null) {
            return label;
        } else {
            return beautify(data);
        }
    }

    /**
     * Sets the human readable text to be displayed underneath the barcode.
     * If set to null then the text will automaticaly be generated.
     *
     * @param label the human readable barcode text
     * @see #getLabel()
     */
    public void setLabel(String label) {
        this.label = label;
    }

    protected int calculateMinimumBarHeight(int resolution) {
        return 0;
    }

    /**
     * From {@link javax.swing.JComponent JComponent}.
     *
     * @param g The graphics to paint the component onto
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Insets insets = getInsets();
        try {
            draw((Graphics2D) g, insets.left, insets.top);
        } catch (OutputException e) {
            // Don't draw anything
        }
    }

    // TODO: Move this to the output
    protected int getResolution() {
        if (resolution > 0) {
            return resolution;
        }
        return EnvironmentFactory.getEnvironment().getResolution();
    }

    protected int drawModule(Module module, Output output, int x, int y, int barWidth, int barHeight) throws OutputException {
        if (module == null) {
            return 0;
        }
        return module.draw(output, x, y, barWidth, barHeight);
    }

    protected String beautify(String s) {
        StringBuffer buf = new StringBuffer();
        StringCharacterIterator iter = new StringCharacterIterator(s);
        for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
            if (Character.isDefined(c) && !Character.isISOControl(c)) {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    private void invalidateSize() {
        size = null;
    }

    private Dimension getActualSize() {
        if (size == null) {
            size = calculateSize();
        }
        return size;
    }

    private Dimension calculateSize() {
    	// CODIGO ORIGINAL AQUI
    	///////////////////////////////////////////
      /*  Dimension d = new Dimension();
        if (EnvironmentFactory.getEnvironment() instanceof HeadlessEnvironment) {
            try {
                d = draw(new SizingOutput(font, getForeground(), getBackground()), 0, 0, barWidth, barHeight);
            } catch (OutputException e) {
            }
        } else {
            try {
                FontMetrics fontMetrics = null;
                if (font != null) {
                    fontMetrics = getFontMetrics(font);
                }
                d = draw(new SizingOutput(font, fontMetrics, getForeground(), getBackground()), 0, 0, barWidth, barHeight);
            } catch (OutputException e) {
            }
        }

        return d;*/
    	
    	Dimension d = new Dimension();
        if(EnvironmentFactory.getEnvironment() instanceof HeadlessEnvironment)
            try
            {
                
                if(font == null)
                {
                    d = draw(new SizingOutput(font, getForeground(), getBackground()), 0, 0, barWidth, barHeight);
                } else
                {
                    java.awt.FontMetrics fontMetrics = getFontMetrics(font);
                    d = draw(new SizingOutput(font, fontMetrics, getForeground(), getBackground()), 0, 0, barWidth, barHeight);
                }
            }
            catch(OutputException e)
            {
                e.printStackTrace();
            }
        else
            try
            {
                java.awt.FontMetrics fontMetrics = null;
                if(font != null)
                    fontMetrics = getFontMetrics(font);
                d = draw(new SizingOutput(font, fontMetrics, getForeground(), getBackground()), 0, 0, barWidth, barHeight);
            }
            catch(OutputException e) { }
        return d;
    }
    
    public int print(Graphics g, PageFormat pageFormat, int pageIndex)
    				throws PrinterException {
    	
    	if (pageIndex >= 1) {
            return Printable.NO_SUCH_PAGE;
        }
    	
    	try
    	{
    		this.draw( (Graphics2D) g, 0, 0);
            return Printable.PAGE_EXISTS;    	
    	}
    	catch (OutputException ex)
    	{
    		throw new PrinterException(ex.getMessage()); 
    	}
        
    }
    
    public String toString()
    {
    	return this.getData();
    }
    

    
}
