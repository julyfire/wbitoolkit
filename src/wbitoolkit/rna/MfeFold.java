/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.rna;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import wbitoolkit.common.LibPathSet;

/**
 *
 * @author wb
 */
public class MfeFold {
    private native float fold(String seq,float t);
    static{
        try {
            LibPathSet.setLibDir("/home/wb/NetBeansProjects/WBIToolkit/lib");
        } catch (IOException ex) {
            System.out.println("Fail to set library path!");
        }
        System.loadLibrary("Fold");
	
    }

    private static float temperature=37;


    /**
     * fold the sequence
     */
    public float cal(String sequence){
        return fold(sequence,temperature);
    }

}
