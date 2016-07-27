
package net.sourceforge.barbecue;

import net.sourceforge.barbecue.env.DefaultEnvironment;
import net.sourceforge.barbecue.output.SVGOutput;
import net.sourceforge.barbecue.output.EPSOutput;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
	Provide a command line interface to simulate GNU barcode. Options available are

	<h2>Command Line Options</h2>
	<ul>
	<li> -b barcode_data. [Required] Specify the barcode data. If the EAN128 encoding is
			specified, then this data will be parsed (See EAN128 parsing below).
	<li> -o filename. Write the barcode output to the specified file.
	<li> -e encoding. Use the specified encoding for the barcode.<br>
		 Options available are:
		 <ul><li> 'ean128'
		 <li> 'code128'
		 </ul>
	<li> -E. Write the output as Encapsulated Postscript (EPS). The default
	output format is a PNG image.
	<li> -label label_file. Write the human readable text output to the specified file.
	This is useful for situations where barbecue calculates a check digit.
	</ul>

	<h2>EAN 128 Parsing</h2>
	Barbecue will parse barcode data if the encoding is specified as ean128. The
	application identifiers should be enclosed in round brackets. For example:<br>
	<pre>(01)0941919600001(10)012004(21)000001</pre>
	Specifies a GTIN (application id 01) with a lot number of 012004 and an item number
	of 000001.

	<h2>Examples</h2>
	<pre>java -jar barbecue.jar -e ean128 -b "(01)0941919600001(10)012004(21)000001" -E -o test.eps<pre>
*/
public class Main
{
	public static void main(String[] args)
	{
		String barcode_text = null;
		String encoding = null;
		String outfile = null;
		String label_outfile = null;

		boolean output_EPS = false;;

		if (args.length == 0)
		{
			Usage();
		}

		int i=0;
		while (i<args.length)
		{
			String command = args[i++];

			if (command.equals("-b"))
				barcode_text = args[i++];
			if (command.equals("-e"))
				encoding = args[i++];
			if (command.equals("-o"))
				outfile = args[i++];
			if (command.equals("-E"))
				output_EPS = true;
			if (command.equals("-label"))
				label_outfile = args[i++];
		}

		if (null == barcode_text)
		{
				System.err.println("Some barcode text must be specified with the -b option");
				Usage();
				return;
		}
		try
		{
			Barcode barcode = null;

			if (encoding.equals("ean128"))
			{
				barcode = BarcodeFactory.parseEAN128(barcode_text);
			}
			else if (encoding.equals("code128"))
			{
				barcode = BarcodeFactory.createCode128(barcode_text);
			}
			else if (encoding.equals("upca"))
			{
				barcode = BarcodeFactory.createUPCA(barcode_text);
			}
			else if (encoding.equals("codabar"))
			{
				barcode = BarcodeFactory.createCodabar(barcode_text);
			}
			else if (encoding.equals("ean13"))
			{
				barcode = BarcodeFactory.createEAN13(barcode_text);
			}
			else
			{
				System.err.println("Unknown encoding: " + encoding);
			}

			if (null == barcode)
			{
				return;
			}

			OutputStream fos = null;
			if (null != outfile)
			{
				fos = new FileOutputStream(outfile);
			}
			else
			{
				fos = System.out;
			}

			if (output_EPS)
			{
				outputEPS(barcode, fos);
			}
			else
			{
				outputPNG(barcode, fos);
			}

			if (null != label_outfile)
			{
				OutputStreamWriter pw = new OutputStreamWriter(new FileOutputStream(label_outfile));
				pw.write(barcode.getLabel());
				pw.flush();
				pw.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void Usage()
	{
		System.err.println("Barbecue Barcode generator");
		System.err.println("Usage:");
		System.err.println("java -jar barbecue.jar -b <barcode text> -e <encoding> -o <outfile> {-E}");
		System.err.println("\tAvailable Encodings are:");
		System.err.println("\t\t \"ean128\"");
		System.err.println("\t-E: Write output as EPS.");
		System.err.println("\t-label <filename>: Write human readable (including check digit) text to the file.");
		System.err.println("\t-o <filename>: Write the barcode image to the file.");
		System.err.println("Example:\n\tjava -jar barbecue.jar -E -e ean128 -o test.eps\\ \n\t\t-label test_label.txt  -b \"(01)0941919600001(10)012004(21)000123\"");
	}

	public static void outputPNG(Barcode barcode, OutputStream fos)
	{
		try
		{
			BarcodeImageHandler.writePNG(barcode, fos);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void outputSVG(Barcode barcode,OutputStream fos)
	{
		try
		{
			// We need an output stream to write the image to...
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			SVGOutput svg_out = new SVGOutput(osw, DefaultEnvironment.DEFAULT_FONT, java.awt.Color.black, java.awt.Color.white, 1, "in");

			barcode.output(svg_out);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void outputEPS(Barcode barcode, OutputStream fos)
	{
		try
		{
			// We need an output stream to write the image to...
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			EPSOutput eps_out = new EPSOutput(osw);

			barcode.output(eps_out);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
