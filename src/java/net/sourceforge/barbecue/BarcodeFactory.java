/***********************************************************************************************************************
 * Copyright (c) 2003, International Barcode Consortium
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 * Neither the name of the International Barcode Consortium nor the names of any contributors may be used to endorse
 * or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 ***********************************************************************************************************************/

///CLOVER:OFF

package net.sourceforge.barbecue;

import net.sourceforge.barbecue.linear.codabar.CodabarBarcode;
import net.sourceforge.barbecue.linear.code128.Code128Barcode;
import net.sourceforge.barbecue.linear.code39.Code39Barcode;
import net.sourceforge.barbecue.linear.ean.BooklandBarcode;
import net.sourceforge.barbecue.linear.ean.EAN13Barcode;
import net.sourceforge.barbecue.linear.ean.UCCEAN128Barcode;
import net.sourceforge.barbecue.linear.postnet.PostNetBarcode;
import net.sourceforge.barbecue.linear.twoOfFive.Int2of5Barcode;
import net.sourceforge.barbecue.linear.twoOfFive.Std2of5Barcode;
import net.sourceforge.barbecue.linear.upc.UPCABarcode;
import net.sourceforge.barbecue.twod.pdf417.PDF417Barcode;

/**
 * This factory provides a standard way of creating barcodes.
 *
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 */
public final class BarcodeFactory {

    /**
     * You can't construct one of these.
     */
    private BarcodeFactory() {
    }

    /**
     * Creates a Code 128 barcode that dynamically switches between character sets
     * to give the smallest possible encoding. This will encode
     * all numeric characters, upper and lower case alpha characters and control characters
     * from the standard ASCII character set. The size of the barcode created will be the
     * smallest possible for the given data, and use of this "optimal" encoding will
     * generally give smaller barcodes than any of the other 3 "vanilla" encodings.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createCode128(String data) throws BarcodeException {
        return new Code128Barcode(data, Code128Barcode.O);
    }

    /**
     * Creates a Code 128 barcode using the A character set. This will encode
     * all numeric characters, upper case alpha characters and control characters
     * from the standard ASCII character set. The Code 128 barcode supports on-the-fly
     * character set changes using the appropriate code change symbol. The type A barcode
     * also supports a one character 'shift' to set B.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createCode128A(String data) throws BarcodeException {
        return new Code128Barcode(data, Code128Barcode.A);
    }

    /**
     * Creates a Code 128 barcode using the B character set. This will encode
     * all numeric characters and upper and lower case alpha characters
     * from the standard ASCII character set. The Code 128 barcode supports on-the-fly
     * character set changes using the appropriate code change symbol. The type B barcode
     * also supports a one character 'shift' to set A.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createCode128B(String data) throws BarcodeException {
        return new Code128Barcode(data, Code128Barcode.B);
    }

    /**
     * Creates a Code 128 barcode using the C character set. This will encode
     * only numeric characters in a double density format (e.g. 1 digit in the barcode
     * encodes two digits in the data). The Code 128 barcode supports on-the-fly
     * character set changes using the appropriate code change symbol. No shifts are
     * possible with the type C barcode.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createCode128C(String data) throws BarcodeException {
        return new Code128Barcode(data, Code128Barcode.C);
    }

    /**
     * Creates a UCC 128 barcode. This will encode numeric characters and must
     * include the correct application identifier for the application domain in which
     * you wish to use the barcode.
     *
     * @param applicationIdentifier The application identifier for the domain
     * @param data                  The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createUCC128(String applicationIdentifier, String data) throws BarcodeException {
        return new UCCEAN128Barcode(applicationIdentifier, data);
    }

    /**
     * Creates a EAN 128 barcode.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createEAN128(String data) throws BarcodeException {
        return new UCCEAN128Barcode(UCCEAN128Barcode.EAN128_AI, data);
    }

    /**
     * Creates a US Postal Service barcode based on the UCC/EAN 128 symbology.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createUSPS(String data) throws BarcodeException {
        return new UCCEAN128Barcode(UCCEAN128Barcode.USPS_AI, data);
    }

    /**
     * Creates a shipment identification number based on the UCC/EAN 128 symbology.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createShipmentIdentificationNumber(String data) throws BarcodeException {
        return new UCCEAN128Barcode(UCCEAN128Barcode.SHIPMENT_ID_AI, data);
    }

    /**
     * Create an EAN128 barcode with multiple application identifiers (AI's). The data is
     * specified in a string of the form (ai) data (ai) data. For example
     * (01)0941919600001(10)012004(21)000001 will create a barcode consisting
     * of a GTIN AI (01) and the data '0941919600001' (note: the
     * GTIN check digit is calculated automatically.), then a lot AI (10)
     * followed by the lot number '012004', and an item AI (21) with item
     * number 000001.
     */
    public static Barcode parseEAN128(String encoded_data) throws BarcodeException {
        return new UCCEAN128Barcode(encoded_data);
    }

    /**
     * Creates an SSCC-18 number based on the UCC/EAN 128 symbology.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createSSCC18(String data) throws BarcodeException {
        return new UCCEAN128Barcode(UCCEAN128Barcode.SSCC_18_AI, data);
    }

    /**
     * Creates an SCC-14 shipping code number based on the UCC/EAN 128 symbology.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createSCC14ShippingCode(String data) throws BarcodeException {
        return new UCCEAN128Barcode(UCCEAN128Barcode.SCC_14_AI, data);
    }

    /**
     * Creates a Global Trade Item Number (GTIN) based on the UCC/EAN 128 symbology.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createGlobalTradeItemNumber(String data) throws BarcodeException {
        return new UCCEAN128Barcode(UCCEAN128Barcode.GTIN_AI, data);
    }

    /**
     * Creates a barcode based on the EAN 13 Symbology.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createEAN13(String data) throws BarcodeException {
        return new EAN13Barcode(data);
    }

    /**
     * Creates a Bookland barcode, which is based on the EAN 13 Symbology.
     * For example, if you createBookland("968-26-1240-3") you will receive
     * an EAN 13 barcode of 9789682612404.
     * Note that only the '-' character will be automaticaly removed from the
     * ISBN data.
     *
     * @param isbn The ISBN of the book to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createBookland(String isbn) throws BarcodeException {
        return new BooklandBarcode(isbn);
    }

    /**
     * Creates a barcode based on the UPC-A Symbology.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createUPCA(String data) throws BarcodeException {
        return new UPCABarcode(data);
    }

    /**
     * Creates a barcode based on the UPC-A Symbology signifying a random weight.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createRandomWeightUPCA(String data) throws BarcodeException {
        return new UPCABarcode(data, true);
    }

    /**
     * Creates a barcode based on the Standard 2 of 5 Symbology.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     * @see #createStd2of5(String, boolean)
     */
    public static Barcode createStd2of5(String data) throws BarcodeException {
        return new Std2of5Barcode(data);
    }

    /**
     * Creates a barcode based on the Standard 2 of 5 Symbology.
     *
     * @param data       The data to encode
     * @param checkDigit if true then a check digit is appended automatically
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     * @see #createStd2of5(String)
     */
    public static Barcode createStd2of5(String data, boolean checkDigit) throws BarcodeException {
        return new Std2of5Barcode(data, checkDigit);
    }

    /**
     * Creates a barcode based on the Interleave 2 of 5 Symbology.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     * @see #createInt2of5(String, boolean)
     */
    public static Barcode createInt2of5(String data) throws BarcodeException {
        return new Int2of5Barcode(data);
    }

    /**
     * Creates a barcode based on the Interleave 2 of 5 Symbology.
     *
     * @param data       The data to encode
     * @param checkDigit if true then a check digit is appended automatically
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     * @see #createInt2of5(String)
     */
    public static Barcode createInt2of5(String data, boolean checkDigit) throws BarcodeException {
        return new Int2of5Barcode(data, checkDigit);
    }

    /**
     * Creates a PDF417 two dimensional barcode.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createPDF417(String data) throws BarcodeException {
        return new PDF417Barcode(data);
    }

    /**
     * Creates a Code 39 linear barcode.
     *
     * @param data             The data to encode
     * @param requiresChecksum True if a check digit is required, false if not
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createCode39(String data, boolean requiresChecksum) throws BarcodeException {
        return new Code39Barcode(data, requiresChecksum);
    }

    /**
     * Creates a Code 3 of 9 (Code 39) linear barcode.
     *
     * @param data             The data to encode
     * @param requiresChecksum True if a check digit is required, false if not
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode create3of9(String data, boolean requiresChecksum) throws BarcodeException {
        return new Code39Barcode(data, requiresChecksum);
    }

    /**
     * Creates a USD3 (Code 39) linear barcode.
     *
     * @param data             The data to encode
     * @param requiresChecksum True if a check digit is required, false if not
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createUSD3(String data, boolean requiresChecksum) throws BarcodeException {
        return new Code39Barcode(data, requiresChecksum);
    }

    /**
     * Creates a Codabar linear barcode.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createCodabar(String data) throws BarcodeException {
        return new CodabarBarcode(data);
    }

    /**
     * Creates a USD-4 (Codabar) linear barcode.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createUSD4(String data) throws BarcodeException {
        return new CodabarBarcode(data);
    }

    /**
     * Creates a NW-7 (Codabar) linear barcode.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createNW7(String data) throws BarcodeException {
        return new CodabarBarcode(data);
    }

    /**
     * Creates a Monarch (Codabar) linear barcode.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode createMonarch(String data) throws BarcodeException {
        return new CodabarBarcode(data);
    }

    /**
     * Creates a 2 of 7 (Codabar) linear barcode.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public static Barcode create2of7(String data) throws BarcodeException {
        return new CodabarBarcode(data);
    }

    /**
     * Creates a <a href="http://en.wikipedia.org/wiki/POSTNET">PostNet</a> linear barcode.
     *
     * @param data The data to encode
     * @return The barcode
     * @throws BarcodeException If the data to be encoded is invalid
     * 
     * 
     * 
     */
    public static Barcode createPostNet(String data) throws BarcodeException {
        return new PostNetBarcode(data);
    }
}

///CLOVER:ON

