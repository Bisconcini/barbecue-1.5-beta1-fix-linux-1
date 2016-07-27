package net.sourceforge.barbecue.linear.ean;

import net.sourceforge.barbecue.BarcodeException;

public class BooklandBarcode extends EAN13Barcode {

    public BooklandBarcode(String isbn) throws BarcodeException {
        super(processIsbn(isbn));
    }

    private static String processIsbn(String isbn) throws BarcodeException {
        // remove any '-' characters, used in ISBN formatting
        if (isbn.indexOf('-') > -1) {
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < isbn.length(); i++) {
                if (isbn.charAt(i) != '-') {
                    sb.append(isbn.charAt(i));
                }
            }
            isbn = sb.toString();
        }

        // check the length of the value of the ISBN
        if (isbn.length() != EAN13Barcode.ISBN_SIZE) {
            throw new BarcodeException("ISBN is an invalid length");
        }

        // trim off the check digit
        // check digit will be created automaticaly as the size is only 11
        return EAN13Barcode.ISBN_NUMBER_SYSTEM + isbn.substring(0, isbn.length() - 1);        // check digit will be created automaticaly as the size is only 11
    }
}
