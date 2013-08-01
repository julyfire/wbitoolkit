/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.common;

/**
 *
 * @author wb
 */
public class Matrix {
    
    public static double[][] transpose(double[][] d){
        int m=d.length;
        int n=d[0].length;
        double[][] td=new double[n][m];
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                td[i][j]=d[j][i];
            }
        }
        return td;
    }
}
