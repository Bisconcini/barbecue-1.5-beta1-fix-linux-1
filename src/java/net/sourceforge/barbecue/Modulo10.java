/***********************************************************************************************************************
 * Copyright (c) 2004, International Barcode Consortium
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

package net.sourceforge.barbecue;

/**
 * Modulo 10 Check Sum class.
 *
 * @author <a href="mailto:james@metalskin.com">James Jenner</a>
 */
public class Modulo10 {
    
    /* no public access */
    private Modulo10() {
    }
    
    /**
     * Calculates a modulo 10 check digit based on the passed numeric data.
     * The calculated check sum is determined using the weighted values, if
     * there is a need for no weight to be applied against the odd and/or 
     * even characters then use the value 1.
     * If a non-numeric value is within the data then NumberFormatException 
     * will be thrown.
     * @param data The data to encode into a Modulo 10 check digit
     * @param weightEven Every even digit will be multiplied by this value
     * @param weightOdd Every odd digit will be multiplied by this value
     * @param beginsEven true if the first numeric data is to be treated as even,
     * false if the first digit is to be treated as odd
     * @return the numeric value of the check digit calculated
     * @see #getMod10CheckDigit(String, int, int)
     * @see #getMod10CheckDigit(String, int)
     */
    public static int getMod10CheckDigit(String data, int weightEven, int weightOdd, boolean beginsEven) 
      throws java.lang.NumberFormatException {
        int sum = 0;
        int len = data.length();
        int value;
        int compareValue = beginsEven ? 0 : 1;
        int result = 0;
        
        for(int i = 0; i < len; i++) {
            value = Integer.parseInt(String.valueOf(data.charAt(i)));
            sum += (i % 2) == compareValue ? weightEven * value : weightOdd * value;
        }

        result = 10 - (sum % 10);

        if(result == 10) {
            result = 0;
        }

        return result;
    }

    /**
     * Calculates a modulo 10 check digit based on the passed numeric data.
     * The calculated check sum is determined using the weighted values, if
     * there is a need for no weight to be applied against the odd and/or 
     * even characters then use the value 1.
     * If a non-numeric value is within the data then NumberFormatException 
     * will be thrown.
     * The first value of data is treated as even.
     * @param data The data to encode into a Modulo 10 check digit
     * @param weightEven Every even digit will be multiplied by this value
     * @param weightOdd Every odd digit will be multiplied by this value
     * false if the first digit is to be treated as odd
     * @return the numeric value of the check digit calculated
     * @see #getMod10CheckDigit(String, int)
     */
    public static int getMod10CheckDigit(String data, int weightEven, int weightOdd) 
      throws java.lang.NumberFormatException {
        return getMod10CheckDigit(data, weightEven, weightOdd, true);
    }
    
    /**
     * Calculates a modulo 10 check digit based on the passed numeric data.
     * The only weight available is odd, this is a common usage.
     * If a non-numeric value is within the data then NumberFormatException 
     * will be thrown.
     * The first value of data is treated as even.
     * @param data The data to encode into a Modulo 10 check digit
     * @param weightOdd Every odd digit will be multiplied by this value
     * @return the numeric value of the check digit calculated
     * @see #getMod10CheckDigit(String, int, int)
     */
    public static int getMod10CheckDigit(String data, int weightOdd) throws java.lang.NumberFormatException {
        return getMod10CheckDigit(data, 1, weightOdd, true);
    }
}
