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

package net.sourceforge.barbecue.linear.ean;

import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.CompositeModule;
import net.sourceforge.barbecue.Module;
import net.sourceforge.barbecue.linear.code128.Accumulator;
import net.sourceforge.barbecue.linear.code128.CharBuffer;
import net.sourceforge.barbecue.linear.code128.Code128Barcode;
import net.sourceforge.barbecue.linear.code128.ModuleFactory;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.StringTokenizer;

/**
 * An implementation of the UCC 128 and EAN 128 code formats. These are almost identical
 * to the vanilla Code 128 format, but they are encoded in character set C and include the
 * FNC1 character at the start. In addition, an Application Identifier must be provided
 * that identifies the application domain of the barcode. Please see the convenienve methods
 * on BarcodeFactory that provide application domain specific instances of this barcode type.
 *
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 */
public class UCCEAN128Barcode extends Code128Barcode {
    /**
     * SSCC-18 application identifier.
     */
    public static final String SSCC_18_AI = "00";
    /**
     * SCC-14 shipping code application identifier.
     */
    public static final String SCC_14_AI = "01";
    /**
     * Global Trade Item Number application identifier.
     */
    public static final String GTIN_AI = SCC_14_AI;
    /**
     * EAN 128 application identifier for all EAN 128 formats.
     */
    public static final String EAN128_AI = "01";
    /**
     * Shipment Identification Number application identifier.
     */
    public static final String SHIPMENT_ID_AI = "402";
    /**
     * US Postal service application identifier for all USPS formats.
     */
    public static final String USPS_AI = "420";
    
    private final String applicationIdentifier;
    private final boolean includeCheckDigit;
	private String labelData;
	private boolean labelDataEncoded = false;

    /**
     * Creates a new UCC/EAN 128 barcode with the given application identifier and
     * data to encode. The AI will be prepended to the data (which also has a mod 10
     * check digit appended) before encoding, and will appear in parentheses in the
     * printed label underneath the barcode. A mod 10 check digit will be generated.
     * @param applicationIdentifier The application identifier for this barcode
     * @param data The data to encode
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public UCCEAN128Barcode(String applicationIdentifier, String data) throws BarcodeException {
        this(applicationIdentifier, data, true);
    }
    

    /**
     * Creates a new UCC/EAN 128 barcode with the given application identifier and
     * data to encode. The AI will be prepended to the data (which also has a mod 10
     * check digit appended) before encoding, and will appear in parentheses in the
     * printed label underneath the barcode.
     * @param applicationIdentifier The application identifier for this barcode
     * @param data The data to encode
     * @param includeCheckDigit specifies whether a mod 10 check digit should be generated or not
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public UCCEAN128Barcode(String applicationIdentifier, String data, boolean includeCheckDigit) throws BarcodeException {
        super(FNC_1 + applicationIdentifier + data + getMod10CheckDigit(data, includeCheckDigit), C);
        if (applicationIdentifier == null || applicationIdentifier.length() == 0) {
            throw new IllegalArgumentException("Application Identifier must be provided");
        }
        this.applicationIdentifier = applicationIdentifier;
        this.includeCheckDigit = includeCheckDigit;
		this.labelData = data;
    }
    
    /**
     * Creates a new UCC/EAN 128 barcode based on the provided data, with an 
     * optional Modulo 10 check digit.
     * @param data The data to encode
     * @param includeCheckDigit if true then a modulo 10 check digit based on 
     *  data is appended
     * @throws BarcodeException If the data to be encoded is invalid
     */
    public UCCEAN128Barcode(String data, boolean includeCheckDigit) throws BarcodeException {
        super(FNC_1 + data + getMod10CheckDigit(data, includeCheckDigit), C);
        this.applicationIdentifier = "";
        this.includeCheckDigit = includeCheckDigit;
		this.labelData = data;
    }
    
	/**
	 * Creates a new UCC/EAN 128 barcode with the given application identifier and
	 * data to encode. The AI will be prepended to the data (which also has a mod 10
	 * check digit appended) before encoding, and will appear in parentheses in the
	 * printed label underneath the barcode. A mod 10 check digit will be generated.
	 * @param encodedData The data to encode. The application identifiers should
	 * be enclosed in parentheses, i.e., (01) 123435 (14) 1234235 e.t.c.
	 *
	 * Concatenating Element Strings of variable length, which includes all Application
	 * Identifiers that do not start with two characters contained in Figure 5.3.8.2.1 ? 1,
	 * involves the use of a Separator Character. The Separator Character used is the
	 * Function 1 Character (FNC1). It is placed immediately after the last symbol
	 * character of a variable length data string and is followed by the Application
	 * Identifier of the next Element String. If the Element String is the last to be encoded,
	 * it is followed by the Symbol Check and Stop Characters and not the FNC1
	 * Separator Character.
	 * @throws BarcodeException If the data to be encoded is invalid
	 */
	public UCCEAN128Barcode(String encodedData) throws BarcodeException
	{
		super(FNC_1, C);
		this.applicationIdentifier = EAN128_AI;
		this.includeCheckDigit = false;

		StringTokenizer st = new StringTokenizer(encodedData,"()",true);

		StringBuffer sb = new StringBuffer();
		StringBuffer labelBuffer = new StringBuffer();

		boolean lastAIwasVariableLength = false;

		while (st.hasMoreTokens())
		{
			String tok = st.nextToken();

			String ai = null;

			if (tok.equals("("))
			{
				ai = st.nextToken();
				st.nextToken(); // get rid of the last ")"
			}

			String barcode_data = st.nextToken();

			if (lastAIwasVariableLength)
				sb.append(FNC_1);

			lastAIwasVariableLength = (getAILength(ai) == 0);

			sb.append(ai);
			sb.append(barcode_data);

			labelBuffer.append("("+ai+")");
			labelBuffer.append(barcode_data);

			if (ai.equals(EAN128_AI))
			{
				String checkDigit = getGTINCheckDigit(barcode_data);
				sb.append(checkDigit);
				// The check digit should be in the human readable label
				labelBuffer.append(checkDigit);
			}
     	}
		this.data += sb.toString();
		this.labelData = labelBuffer.toString();
		this.labelDataEncoded = true;
}

	/** Get the length of a pre-defined length EAN application identifier.
	*	@return the number of characters (including the application identifier) or zero if the AI
	*  does not have a fixed length.
	*  Note: See the UCC/EAN-128 Symbology Specifications for details.
	*  No separator character is required when these appliction identifiers
	*  are used.
	*/
	private int getAILength(String ai) // return 0 if variable
	{
		if (ai.equals("00"))
			return 20;
		if (ai.equals("01"))
			return 16;
		if (ai.equals("02"))
			return 16;
		if (ai.equals("03"))
			return 16;
		if (ai.equals("04"))
			return 18;
		if (ai.equals("11"))
			return 8;
		if (ai.equals("12"))
			return 8;
		if (ai.equals("13"))
			return 8;
		if (ai.equals("14"))
			return 8;
		if (ai.equals("15"))
			return 8;
		if (ai.equals("16"))
			return 8;
		if (ai.equals("17"))
			return 8;
		if (ai.equals("18"))
			return 8;
		if (ai.equals("19"))
			return 8;
		if (ai.equals("20"))
			return 4;
		if (ai.equals("31"))
			return 10;
		if (ai.equals("32"))
			return 10;
		if (ai.equals("33"))
			return 10;
		if (ai.equals("34"))
			return 10;
		if (ai.equals("35"))
			return 10;
		if (ai.equals("36"))
			return 10;
		if (ai.equals("41"))
			return 16;

		return 0;
	}

	/**
     * Returns the pre-amble for this barcode type. This is basically
     * a specific instance of the Code 128 barcode that always uses
     * a C start char and FNC1 char in succession.
     * @return The pre-amble module
     */
    protected Module getPreAmble() {
        CompositeModule module = new CompositeModule();
        if(drawingQuietSection) {
            module.add(QUIET_SECTION);
        }
        module.add(START_C);
        return module;
    }

    /**
     * Returns the text to be displayed underneath the barcode.
     * @return The text that the barcode represents
     */
    public String getLabel()
	{
        if (null != labelData && labelDataEncoded)
		{
			return labelData;
		}

		if (null != label)
		{
            return label;
        }

        return '(' + applicationIdentifier + ") " + labelData + getMod10CheckDigit(labelData, includeCheckDigit);
    }
    
    /**
     * Generates a mod 10 check digit for the barcode data (ignoring the app id).
     * @param data The data to generate the check digit for
     * @param calculate Whether the check digit should actually be calculated or not
     * @return The check digit (or "" if not calculated)
     */
    static String getMod10CheckDigit(String data, boolean calculate) {
        if (!calculate) {
            return "";
        }
        
        Accumulator sum = new Accumulator(START_INDICES[C]);
        Accumulator index = new Accumulator(1);
        CharBuffer buf = new CharBuffer(BUF_SIZES[C]);
        StringCharacterIterator iter = new StringCharacterIterator(data);
        for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
            buf.addChar(c);
            if (buf.isFull()) {
                int code = ModuleFactory.getIndex(buf.toString(), C);
                sum.add(code * index.getValue());
                index.increment();
                buf.clear();
            }
        }
        return String.valueOf(sum.getValue() % 10);
    }

	/**
	 * Generates a UCC/EAN standard check digit for the EAN
	 * barcode element (ignoring the app id).
	 * @param element The data to generate the check digit for
	 * @return The check digit (or "" if not calculated)
	 */
	static String getGTINCheckDigit(String element)
	{
		/*	Note: The other Check digit calculation appears wrong.
			This one is taken from the EAN SPEC Section 3.A.1 Appendix 1: Check Digit Calculations
			IT ONLY APPLIES TO GTIN and possibly others!
			NOTE: Price and Weight elements MUST to use a different method.
		*/
		int len = element.length();
		int multiplier = 1;
		int sum = 0;
		for (int i=(len-1);i>=0;i--)
		{
			if (multiplier == 1)
				multiplier = 3;
			else
				multiplier = 1;

			sum += multiplier * Integer.parseInt(element.substring(i,i+1));
		}
		// Step 3: Subtract sum from nearest equal or higher multiple of ten (110)  =  Check Digit (9)
		int ret = ((sum / 10) + 1) * 10 - sum;
		return String.valueOf(ret);
	}}
