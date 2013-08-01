/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.phylo;

/**
 *
 * @author wb
 */
public abstract class DistanceMatrix {
    
    private Object[] data;
    private double[][] matrix;
    
    public abstract double distanceOf(Object a, Object b);
    
    private void calculateMatrix(){
        matrix=new double[data.length][data.length];
        for(int i=0;i<data.length-1;i++){
            for(int j=i+1;j<data.length;j++){
                matrix[i][j]=distanceOf(data[i],data[j]);
                matrix[j][i]=matrix[i][j];
            }
        }
    }
    
    public double[][] getMatrix(){
        if(matrix==null){
            calculateMatrix();
        }
        return matrix;
    }

    /**
     * @return the data
     */
    public Object[] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Object[] data) {
        this.data = data;
    }
}
