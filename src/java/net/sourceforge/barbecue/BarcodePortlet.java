
package net.sourceforge.barbecue;

import javax.portlet.*;

import java.io.*;
import java.util.*;
import net.sourceforge.barbecue.output.*;

/**
 * 
 *  Barcode portlet
 *
 *  Note: this portlet uses features from  
 *        the Portlet 2.0 specification (JSR-286).
 *        This portlet will not run in a Portlet 1.0 container.
 *        
 *  @author Sean Sullivan
 *
 */
public class BarcodePortlet 
		extends GenericPortlet
		implements ResourceServingPortlet
		
{
	private static final String PARAM_BARCODE_DATA = "barcode_data";
	private static final String SESSION_KEY = "barcode_data";

	protected java.lang.String getTitle(RenderRequest request)
	{
		return "Barcode portlet";
	}
	
	
	protected void doEdit(RenderRequest req, RenderResponse resp)
		throws PortletException, IOException
	{
		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		PortletURL url = resp.createActionURL();
		writer.println("<form method=\"POST\" action=\"" + url + "\">");
		writer.println("Enter string: <input name=\"" + PARAM_BARCODE_DATA + "\" type=text size=30></input>");
		writer.println("<input type=submit value=\"Submit\"></input>");
		writer.println("</form>");
		
	}
	
	protected void doView(RenderRequest req, RenderResponse resp)
		throws PortletException, IOException
	{
		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		ResourceURL url = resp.createResourceURL();
		writer.println("<img src=\"" + url + "\" />");
	}

	protected void doHelp(RenderRequest req, RenderResponse resp)
		throws PortletException, IOException
	{
		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		writer.println("<a target=\"_blank\" href=\"http://en.wikipedia.org/wiki/Barcode\">What is a barcode?</a>");
	}

	public void processAction(ActionRequest req,
							ActionResponse resp)
		     throws PortletException,
		            java.io.IOException
    {
			storeDataInSession(req);
	}

	static private void storeDataInSession(PortletRequest req)
	{
		PortletSession session = req.getPortletSession(true);
		session.setAttribute(SESSION_KEY, req.getParameter(PARAM_BARCODE_DATA));
	}
	
	static private Barcode createBarcode(String data) 
	{
		if (data == null)
		{
			data = "Barcode";
		}

		try
		{
			Barcode b = BarcodeFactory.createCode128(data);
			return b;
		}
		catch (BarcodeException ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	static private Barcode createBarcode(PortletRequest req)
	{
		PortletSession sess = req.getPortletSession(true);
		return createBarcode( (String) sess.getAttribute(SESSION_KEY));
	}
	
	
	public void serveResource(ResourceRequest req, ResourceResponse resp) 
				throws PortletException, IOException
	{

		Barcode b = createBarcode(req);
		
		if (b == null)
		{
			b = createBarcode("Barcode");
		}
		
		resp.setContentType("image/png");
		OutputStream out = resp.getPortletOutputStream();
		try
		{
			BarcodeImageHandler.writePNG(b, out);
			out.flush();
		}
		catch (OutputException ex)
		{
			throw new PortletException(ex);
		}
		
		
	}
}
