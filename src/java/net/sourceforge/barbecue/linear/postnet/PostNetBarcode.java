/*
 * Created on Aug 31, 2004
*/
package net.sourceforge.barbecue.linear.postnet;

import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.Module;
import net.sourceforge.barbecue.linear.LinearBarcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Brendon Anderson
 */
public class PostNetBarcode extends LinearBarcode {

    protected final static int HEIGHT = 20;

    public PostNetBarcode(String zipcode) throws BarcodeException {
        super(zipcode);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.barbecue.Barcode#calculateChecksum()
     */
    protected Module calculateChecksum() {
        return null;
    }

    /* (non-Javadoc)
     * @see net.sourceforge.barbecue.Barcode#encodeData()
     */
    protected Module[] encodeData() {
        long sum = 0;
        List modules = new ArrayList();
        for (int i = 0; i < data.length(); i++) {
            String c = String.valueOf(data.charAt(i));
            Module module = ModuleFactory.getModule(c);
            sum += Long.parseLong(c);
            modules.add(module);
        }

        //add the check digit
        long check = (10 - (sum % 10));
        if (check == 10) {
            check = 0;
        }

        modules.add(ModuleFactory.getModule(String.valueOf(check)));
        return (Module[]) modules.toArray(new PostNetModule[0]);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.barbecue.Barcode#getBarcodeWidth(int)
     */
    protected double getBarcodeWidth(int resolution) {
        return 0;
    }

    /* (non-Javadoc)
     * @see net.sourceforge.barbecue.Barcode#getPostAmble()
     */
    protected Module getPostAmble() {
        return ModuleFactory.START_STOP;
    }

    /* (non-Javadoc)
     * @see net.sourceforge.barbecue.Barcode#getPreAmble()
     */
    protected Module getPreAmble() {
        return ModuleFactory.START_STOP;
    }

    /* (non-Javadoc)
     * @see java.awt.Component#getHeight()
     */
    public int getHeight() {
        return HEIGHT;
    }

}
