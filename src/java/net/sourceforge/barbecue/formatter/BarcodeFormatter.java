package net.sourceforge.barbecue.formatter;

import net.sourceforge.barbecue.Barcode;

public interface BarcodeFormatter {

	void format(Barcode barcode) throws FormattingException;
}
