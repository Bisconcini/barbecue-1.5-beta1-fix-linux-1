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

import junit.framework.TestCase;

import java.awt.*;

// Note: Deliberately not a BarcodeTestCase
public class EnvironmentFactoryTest extends TestCase {

	public void testHeadlessEnvironmentReturnedOnHeadlessMachine() throws Exception {
		EnvironmentFactory.setDefaultEnvironment(new UnsupportedEnvironment());
		assertTrue(EnvironmentFactory.getEnvironment() instanceof HeadlessEnvironment);
		EnvironmentFactory.setDefaultEnvironment(new ErrorEnvironment());
		assertTrue(EnvironmentFactory.getEnvironment() instanceof HeadlessEnvironment);
	}

	public void testDefaultEnvironmentCanBeOverridden() throws Exception {
		EnvironmentFactory.setDefaultEnvironment(new TestEnvironment());
		assertTrue(EnvironmentFactory.getEnvironment() instanceof TestEnvironment);
		assertEquals(69, EnvironmentFactory.getEnvironment().getResolution());
	}

    public void testNonAWTModeAlwaysReturnsNonAWTEnvironment() throws Exception {
        EnvironmentFactory.setNonAWTMode(68);
        assertTrue(EnvironmentFactory.getEnvironment() instanceof NonAWTEnvironment);
    }

    public void testSettingNonAWTModeWithNoResolutionUsesHeadlessEnvironmentDefaultResolution() throws Exception {
        EnvironmentFactory.setNonAWTMode();
        assertEquals(HeadlessEnvironment.DEFAULT_RESOLUTION, EnvironmentFactory.getEnvironment().getResolution());
    }

	private class UnsupportedEnvironment implements Environment {
		public int getResolution() {
			throw new UnsupportedOperationException();
		}

        public Font getDefaultFont() {
            throw new UnsupportedOperationException();
        }
	}

	private class ErrorEnvironment implements Environment {
		public int getResolution() {
			throw new InternalError();
		}

        public Font getDefaultFont() {
            throw new InternalError();
        }
	}

	private class TestEnvironment implements Environment {
		public int getResolution() {
			return 69;
		}

        public Font getDefaultFont() {
            return null;
        }
	}
}
