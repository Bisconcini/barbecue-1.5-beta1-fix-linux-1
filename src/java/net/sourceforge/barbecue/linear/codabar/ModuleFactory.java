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

package net.sourceforge.barbecue.linear.codabar;

import net.sourceforge.barbecue.Module;

import java.util.Map;
import java.util.HashMap;

/**
 * Codabar barcode module definitions.
 *
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 */
final class ModuleFactory {
	private static final Map SET = new HashMap();

	static {
		init();
	}

	///CLOVER:OFF
	/**
	 * No public access.
	 */
	private ModuleFactory() {
	}
	///CLOVER:ON

	/**
	 * Initialise the module definitions.
	 */
	private static void init() {
		SET.put("0", new Module(new int[] {1, 1, 1, 1, 1, 2, 2}));
		SET.put("1", new Module(new int[] {1, 1, 1, 1, 2, 2, 1}));
		SET.put("2", new Module(new int[] {1, 1, 1, 2, 1, 1, 2}));
		SET.put("3", new Module(new int[] {2, 2, 1, 1, 1, 1, 1}));
		SET.put("4", new Module(new int[] {1, 1, 2, 1, 1, 2, 1}));
		SET.put("5", new Module(new int[] {2, 1, 1, 1, 1, 2, 1}));
		SET.put("6", new Module(new int[] {1, 2, 1, 1, 1, 1, 2}));
		SET.put("7", new Module(new int[] {1, 2, 1, 1, 2, 1, 1}));
		SET.put("8", new Module(new int[] {1, 2, 2, 1, 1, 1, 1}));
		SET.put("9", new Module(new int[] {2, 1, 1, 2, 1, 1, 1}));
		SET.put("-", new Module(new int[] {1, 1, 1, 2, 2, 1, 1}));
		SET.put("$", new Module(new int[] {1, 1, 2, 2, 1, 1, 1}));
		SET.put(":", new Module(new int[] {2, 1, 1, 1, 2, 1, 2}));
		SET.put("/", new Module(new int[] {2, 1, 2, 1, 1, 1, 2}));
		SET.put(".", new Module(new int[] {2, 1, 2, 1, 2, 1, 1}));
		SET.put("+", new Module(new int[] {1, 1, 2, 2, 2, 2, 2}));
		SET.put("A", new Module(new int[] {1, 1, 2, 2, 1, 2, 1}));
		SET.put("B", new Module(new int[] {1, 1, 1, 2, 1, 2, 2}));
		SET.put("C", new Module(new int[] {1, 2, 1, 2, 1, 1, 2}));
		SET.put("D", new Module(new int[] {1, 1, 1, 2, 2, 2, 1}));
	}

	/**
	 * Returns the module that represents the specified character.
	 * @param key The data character to get the encoding module for
	 * @return The module that encodes the given char
	 */
	public static Module getModule(String key) {
		Module module = null;
		module = (Module) SET.get(key);
		module.setSymbol(key);
		return module;
	}

	/**
	 * Indicates whether the given character is valid for this barcode or not.
	 * This basically just checks to see whether the key is in the list of
	 * encoded characters.
	 * @param key The key to check for validity
	 * @return True if the key is valid, false otherwise
	 */
	public static boolean isValid(String key) {
		return SET.containsKey(key);
	}
}
