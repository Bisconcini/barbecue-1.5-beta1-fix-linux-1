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

import net.sourceforge.barbecue.env.*;
import net.sourceforge.barbecue.linear.code39.Code39Barcode;
import net.sourceforge.barbecue.output.OutputException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * The query parameters for this servlet are:
 * <ol>
 * <li>data, required, example: "1234567890"
 * <li>type, optional, examples: "Code128A", "Code128B", "Code128C", if left blank will default to Code 128 B.
 * Note that the type here must be <b>exactly</b> the name of one of the createX methods on BarcodeFactory without
 * the "create" prefix. This is case sensitive.
 * <li>appid, required for UCC128 type, ignored for all other types - specifies the application identifier to use with
 * the UCC128 barcode, example: "420" for a US postal service barcode
 * <li>width, optional, in pixels
 * <li>height, optional, in pixels
 * <li>resolution, optional, in dpi
 * <li>headless, optional, set to "false" to force non-headless mode for the servlet - default is "true"
 * <li>drawText, optional and only takes effect if headless is "false", set to "false" for no text
 * </ol>
 *
 * <p>Example URL: <code>http://hostname:80/myapp/BarcodeServlet?data=12345&amp;type=Code128A</code>
 * </p>
 * 
 * <p>Contributed by Robert Chou &lt;rchou at users.sourceforge.net&gt;</p>
 *
 * @author Robert Chou
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 * @author Sean Sullivan
 * 
 */
public class BarcodeServlet extends HttpServlet {
	/**
	 * From HttpServlet.
	 * @return The literal string 'barbecue'
	 */
	public String getServletName() {
		return "barbecue";
	}

	/**
	 * From HttpServlet.
	 * @param req The servlet request
	 * @param res The servlet response
	 * @throws ServletException If an error occurs during processing
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException {
		doRequest(req, res);
	}

	/**
	 * From HttpServlet.
	 * @param req The servlet request
	 * @param res The servlet response
	 * @throws ServletException If an error occurs during processing
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException {
		doRequest(req, res);
	}

	private void doRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException {
		String data = getRequiredParameter(req, "data");
		String type = getParameter(req, "type");
		String appId = getParameter(req, "appid");
		Integer width = getParameterAsInteger(req, "width");
		Integer height = getParameterAsInteger(req, "height");
		Integer resolution = getParameterAsInteger(req, "resolution");
		boolean checksum = getParameterAsBoolean(req, "checksum", false);
		boolean headless = getParameterAsBoolean(req, "headless", true);
		boolean drawText = false;

		if (headless) {
			EnvironmentFactory.setHeadlessMode();
		} else {
			drawText = getParameterAsBoolean(req, "drawText", false);
		}

		Barcode barcode = getBarcode(type, data, appId, checksum);
		barcode.setDrawingText(drawText);

		if (width != null) {
			barcode.setBarWidth(width.intValue());
		}
		if (height != null) {
			barcode.setBarHeight(height.intValue());
		}
		if (resolution != null) {
			barcode.setResolution(resolution.intValue());
		}

		try {
			outputBarcodeImage(res, barcode);
		} catch (IOException e) {
			throw new ServletException("Could not output barcode", e);
		} catch (OutputException e) {
			throw new ServletException("Could not output barcode", e);
		}
	}

	private String getRequiredParameter(HttpServletRequest req, String name) throws ServletException {
		String value = getParameter(req, name);
		if (value == null) {
			throw new ServletException("Parameter " + name + " is required");
		}
		return value;
	}

	private boolean getParameterAsBoolean(HttpServletRequest req, String name, boolean def) {
		String value = getParameter(req, name);
		if (value == null) {
			return def;
		}
		return Boolean.valueOf(value).booleanValue();
	}

	private Integer getParameterAsInteger(HttpServletRequest req, String name) {
		String value = getParameter(req, name);
		if (value == null) {
			return null;
		}
		return new Integer(value);
	}

	private String getParameter(HttpServletRequest req, String name) {
		return req.getParameter(name);
	}

	/**
	 * Returns the appropriate barcode for the speficied parameters.
	 * @param type The barcode type
	 * @param data The data to encode
	 * @param appId The (optional) application ID - for UCC128 codes
	 * @param checkSum Flag indicating whether a checksum should be appended to the barcode - for Code 39 barcodes
	 * @return The barcode
	 * @throws ServletException If required data is missing
	 */
	protected Barcode getBarcode(String type, String data, String appId, boolean checkSum) throws ServletException {
		if (type == null || type.length() == 0) {
			try {
				return BarcodeFactory.createCode128B(data);
			} catch (BarcodeException e) {
				throw new ServletException("BARCODE ERROR", e);
			}
		} else if (isType(type, new String[] {"UCC128"})) {
			if (appId == null) {
				throw new ServletException("UCC128 barcode type requires the appid parameter");
			}
			try {
				return BarcodeFactory.createUCC128(appId, data);
			} catch (BarcodeException e) {
				throw new ServletException("BARCODE ERROR", e);
			}
		} else if (isType(type, Code39Barcode.TYPES)) {
			try {
				return BarcodeFactory.createCode39(data, checkSum);
			} catch (BarcodeException e) {
				throw new ServletException("BARCODE ERROR", e);
			}
		}

		try {
			return (Barcode) getMethod(type).invoke(null, new Object[] {data});
		} catch (NoSuchMethodException e) {
			throw new ServletException("Invalid barcode type: " + type);
		} catch (SecurityException e) {
			throw new ServletException("Could not create barcode of type: " + type
									   + "; Security exception accessing BarcodeFactory");
		} catch (IllegalAccessException e) {
			throw new ServletException("Could not create barcode of type: " + type
									   + "; Illegal access to BarcodeFactory");
		} catch (InvocationTargetException e) {
			throw new ServletException("Could not create barcode of type: " + type
									   + "; Could not invoke BarcodeFactory");
		}
	}

	private boolean isType(String value, String[] types) {
		for (int i = 0; i < types.length; i++) {
			String type = types[i];
			if (value.equalsIgnoreCase(type)) {
				return true;
			}
		}
		return false;
	}

	private Method getMethod(String type) throws NoSuchMethodException {
		Method[] methods = BarcodeFactory.class.getMethods();

		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if ((method.getParameterTypes().length == 1) && matches(method, type)) {
				return method;
			}
		}

		throw new NoSuchMethodException();
	}

	private boolean matches(Method method, String type) {
		return method.getName().startsWith("create") && method.getName().substring(6).equalsIgnoreCase(type);
	}

	private void outputBarcodeImage(HttpServletResponse res, Barcode barcode) throws IOException, OutputException {
		res.setContentType("image/png");
		ServletOutputStream out = res.getOutputStream();
		BarcodeImageHandler.writePNG(barcode, out);
		out.flush();
		out.close();
	}
}
