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

import java.awt.*;

/**
 * An environment used when absolutely no AWT activity is desired when
 * handling barcodes.
 *
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 */
public class NonAWTEnvironment implements Environment {
	private final int resolution;

    /**
     * Constructs the environment with the mandatory output resolution.
     * The resolution must be specified because this environment cannot
     * look it up anywhere.
     * @param resolution The output resolution
     */
	public NonAWTEnvironment(int resolution) {
		this.resolution = resolution;
	}

    /**
	 * Returns the specified resolution for
	 * outputting barcodes.
	 * @return The resolution for the environment
	 */
	public int getResolution() {
		return resolution;
	}

    /**
     * Returns the default font for the environment.
     * @return null
     */
	public Font getDefaultFont() {
		return null;
	}
}
