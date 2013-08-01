/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.smooth;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import wbitoolkit.common.LibPathSet;

/**
 *
 * @author wb
 */
public class Loess {
    public static double span=0.25;
    public static int degree=2;
    private static native double[] smooth(double[] x, double[] y, double span, int degree);
    static{
        try {
            LibPathSet.setLibDir("/home/wb/NetBeansProjects/WBIToolkit/lib");
//            LibPathSet.setLibDir(LibPathSet.getPackageDir());
        } catch (IOException ex) {
            System.out.println("fail to set library path!");
        }

        if(System.getProperty("os.name").equals("Linux")==false){
            System.loadLibrary("libgslcblas");
            System.loadLibrary("libgsl");
        }
        System.loadLibrary("LoessSmooth");
    }
    
    public Loess(){
        
    }
    public Loess(double span){
        this.span=span;
    }
    public Loess(int degree){
        this.degree=degree;
    }
    public Loess(double span, int degree){
        this.span=span;
        this.degree=degree;
    }
    

    public static double[] smooth(double[] y){
        double[] x=new double[y.length];
        for(int i=0;i<y.length;i++)
            x[i]=i;
        return smooth(x,y,span,degree);
    }
    public static double[] smooth(double[] x, double[] y){
        return smooth(x,y,span,degree);
    }


}
