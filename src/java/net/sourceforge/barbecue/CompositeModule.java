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

import net.sourceforge.barbecue.output.Output;
import net.sourceforge.barbecue.output.OutputException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Specific implementation of Module that allows the grouping of multiple Modules into
 * one parent module.
 * <p/>Note: You should not instantiate this class directly.
 *
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 */
public class CompositeModule extends Module {
	private final List modules;

	/**
	 * Constructs a new Composite module that is initially empty.
	 */
	public CompositeModule() {
		super(new int[0]);
		modules = new ArrayList();
	}

	/**
	 * Adds the given module to this composite module.
	 * @param module The module to add
	 */
	public void add(Module module) {
		modules.add(module);
	}

	/**
	 * Returns the number of modules currently contained within this composite module.
	 * @return The number of child modules
	 */
	public int size() {
		return modules.size();
	}

	/**
	 * Returns the child module at the specified index.
	 * @param index The module index
	 * @return The module at the given index
	 */
	public Module getModule(int index) {
		return (Module) modules.get(index);
	}

	/**
	 * Returns the symbol group encoded by this module. This is actually a concatenation
	 * of the symbols encoded by each child module.
	 * @return The symbol encoded by this composite module
	 */
	public String getSymbol() {
		StringBuffer buf = new StringBuffer();
		for (Iterator iterator = modules.iterator(); iterator.hasNext();) {
			Module module = (Module) iterator.next();
			buf.append(module.getSymbol());
		}
		return buf.toString();
	}

	/**
	 * Returns the underlying total width of the bars from the bar specification (that is, the sum of original bar widths
	 * in base bar units).
	 *
	 * @return The total width of bars in base (unscaled) units
	 */
	public int widthInBars() {
		int width = 0;
		for (Iterator iterator = modules.iterator(); iterator.hasNext();) {
			Module module = (Module) iterator.next();
			width += module.widthInBars();
		}
		return width;
	}

	/**
	 * Draws the module to the given outputter at the specified origin. This actually
	 * draws each child module in turn.
	 * @param output The outputter to draw to
	 * @param x The X component of the origin
	 * @param y The Y component of the origin
	 * @param barWidth
	 * @param barHeight
	 * @return The total width drawn
	 */
	protected int draw(Output output, int x, int y, int barWidth, int barHeight) throws OutputException {
		int sum = 0;
		int currentX = x;
		
		for (Iterator iterator = modules.iterator(); iterator.hasNext();) {
			Module module = (Module) iterator.next();
			int result = module.draw(output, currentX, y, barWidth, barHeight);
			currentX += result;
			sum += result;
		}

		return sum;
	}
}
