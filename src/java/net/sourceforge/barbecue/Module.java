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

import net.sourceforge.barbecue.output.Output;
import net.sourceforge.barbecue.output.OutputException;

import java.util.Arrays;

/**
 * Internal class that is used to organise barcode data into groups of bars.
 * <p/>Note: You should not instantiate this class directly.
 *
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 */
public class Module {
    /** The specification of bars that makes up this module, in a list of bar widths in on, off order) */
    protected final int[] bars;
    private String symbol;
    
    /**
     * Constructs a new Module with the given bar specification.
     * @param bars The bar specification
     */
    public Module(int[] bars) {
        this.bars = bars;
    }
    
    /**
     * Returns the symbol being encoded by this module.
     * @return The symbol encoded by this module
     */
    public String getSymbol() {
        return symbol;
    }
    
    /**
     * Sets the symbol that this module encodes.
     * @param symbol The symbol being encoded by this module
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    /**
     * Returns the underlying total width of the bars from the bar
     * specification (that is, the sum of original bar widths in base
     * bar units).
     * @return The total width of bars in base (unscaled) units
     */
    public int widthInBars() {
        int sum = 0;
        for (int i = 0; i < bars.length; i++) {
            sum += bars[i];
        }
        return sum;
    }
    
    /**
     * Draws the module to the given outputter at the specified origin.
     * @param output The outputter to draw to
     * @param x The X component of the origin
     * @param y The Y component of the origin
     * @param barWidth
     * @param barHeight
     * @return The total width drawn
     */
    protected int draw(Output output, int x, int y, int barWidth, int barHeight) throws OutputException {
        int sum = 0;
        for (int i = 0; i < bars.length; i++) {
            int bar = bars[i];
            int w = bar * barWidth;
            // sum += w;
            // output.drawBar(x, y, w, barHeight, (i % 2 == 0));
            sum += output.drawBar(x, y, w, barHeight, (i % 2 == 0));
            x += w;
        }

        return sum;
    }
    
    /**
     * See Object.
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (!(o instanceof Module)) {
            return false;
        }
        
        final Module module = (Module) o;
        
        if (!Arrays.equals(bars, module.bars)) {
            return false;
        }
        
        return true;
    }
    
    
    
    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hashtables such as those provided by
     * <code>java.util.Hashtable</code>.
     * <p>
     * The general contract of <code>hashCode</code> is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during
     *     an execution of a Java application, the <tt>hashCode</tt> method
     *     must consistently return the same integer, provided no information
     *     used in <tt>equals</tt> comparisons on the object is modified.
     *     This integer need not remain consistent from one execution of an
     *     application to another execution of the same application.
     * <li>If two objects are equal according to the <tt>equals(Object)</tt>
     *     method, then calling the <code>hashCode</code> method on each of
     *     the two objects must produce the same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal
     *     according to the {@link Object#equals(Object)}
     *     method, then calling the <tt>hashCode</tt> method on each of the
     *     two objects must produce distinct integer results.  However, the
     *     programmer should be aware that producing distinct integer results
     *     for unequal objects may improve the performance of hashtables.
     * </ul>
     * <p>
     * As much as is reasonably practical, the hashCode method defined by
     * class <tt>Object</tt> does return distinct integers for distinct
     * objects. (This is typically implemented by converting the internal
     * address of the object into an integer, but this implementation
     * technique is not required by the
     * Java<font size="-2"><sup>TM</sup></font> programming language.)
     *
     * @return  a hash code value for this object.
     * @see     Object#equals(Object)
     */
    public int hashCode() {
        int sum = 0;
        for (int i = 0; i < bars.length; i++) {
            sum += (i + 1) * bars[i];
        }
        return sum;
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < bars.length; i++) {
            if (i > 0) {
                buf.append(", ");
            }
            buf.append(bars[i]);
        }
        return buf.toString();
    }
}
