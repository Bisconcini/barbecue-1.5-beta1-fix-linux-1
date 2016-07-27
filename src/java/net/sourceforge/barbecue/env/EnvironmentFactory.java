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

package net.sourceforge.barbecue.env;

/**
 * Factory class for getting hold of the current operating environment.
 *
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 */
public class EnvironmentFactory {

	private static Environment env;
	private static Environment defaultEnvironment;

	///CLOVER:OFF
	/** Cannot construct directly */
	protected EnvironmentFactory() {
	}
	///CLOVER:ON

	/**
	 * Returns the current operating environment.
	 * @return The current environment
	 */
	public static Environment getEnvironment() {
		if (env == null) {
			determineCurrentEnvironment();
		}
		return env;
	}

	/**
	 * Forces the factory to assume headless mode, regardless of whether
	 * this is actually true or not.
	 */
	public static void setHeadlessMode() {
		env = new HeadlessEnvironment();
	}

    /**
	 * Forces the factory to use the environment that does not access the AWT.
	 */
	public static void setNonAWTMode() {
		env = new NonAWTEnvironment(HeadlessEnvironment.DEFAULT_RESOLUTION);
	}

	/**
	 * Forces the factory to use the environment that does not access the AWT.
	 * @param resolution The desired (or current) screen/output resolution
	 */
	public static void setNonAWTMode(int resolution) {
		env = new NonAWTEnvironment(resolution);
	}

	/**
	 * Sets the factory to use the default (discovered) environment.
	 */
	public static void setDefaultMode() {
		determineCurrentEnvironment();
	}

	/**
	 * Sets the default environment for the factory.
	 * Use this to set your own environment implementation.
	 * @param newEnv The new default environment
	 */
	public static void setDefaultEnvironment(Environment newEnv) {
		env = null;
		defaultEnvironment = newEnv;
	}

	private static void determineCurrentEnvironment() {
		Environment current;
		if (defaultEnvironment != null) {
			current = defaultEnvironment;
		} else {
			current = new DefaultEnvironment();
		}
		try {
			// Try to get the res, this will fail in headless mode
			current.getResolution();
		} catch (UnsupportedOperationException e) {
			current = new HeadlessEnvironment();
		} catch (InternalError e) {
			current = new HeadlessEnvironment();
		}
		env = current;
	}
}
