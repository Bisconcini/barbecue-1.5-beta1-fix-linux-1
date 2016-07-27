package net.sourceforge.barbecue.formatter;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.output.SVGOutput;
import net.sourceforge.barbecue.output.OutputException;

import java.io.Writer;
import java.io.StringWriter;

public class SVGFormatter implements BarcodeFormatter {
	private static final String [] UNITS = {"in", "px", "cm", "mm"};

	private final Writer out;
	private String units;
	private double scalar;

	public static String formatAsSVG(Barcode barcode) throws FormattingException {
		StringWriter writer = new StringWriter();
		new SVGFormatter(writer).format(barcode);
		return writer.toString();
	}

	public SVGFormatter(Writer out) {
		this(out, 1.0 / 128, "in");
	}

	public SVGFormatter(Writer out, double scalar, String units) {
		this.out = out;
		setSVGScalar(scalar, units);
	}

	public void format(Barcode barcode) throws FormattingException {
		try {
			barcode.output(new SVGOutput(out, barcode.getFont(),
										 barcode.getForeground(), barcode.getBackground(),
										 scalar, units));
		}
		catch (OutputException e) {
			throw new FormattingException(e.getMessage(), e);
		}
	}

	public void setSVGScalar(double scalar, String units) {
		validateUnits(units);
		this.scalar = scalar;
		this.units = units;
	}

	private void validateUnits(String units) {
		boolean found = false;

		for (int i = 0; i < UNITS.length; i++) {
			if (units.equals(UNITS[i])) {
				found = true;
				break;
			}
		}

		if (!found) {
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < UNITS.length; i++) {
				String unit = UNITS[i];
				if (i > 0) {
					buf.append(", ");
				}
				buf.append(unit);
			}
			throw new IllegalArgumentException("SVG Units must be one of " + buf.toString());
		}
	}
}
