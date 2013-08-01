/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.smooth;

import java.util.ArrayList;

/**
 *
 * @author wb
 */
public class KernelDensity {
    
    private double[] data;
    private double bandwidth;
    private double min;
    private double max;
    private double n;
    private double meanX;
    private double meanXX;
    private double extension=3;
    private double[] x;
    private double[] probX;
    private int size=100;
    
    public KernelDensity(){
        
    }
    
    public KernelDensity(double[] data){
        loadData(data);
        this.bandwidth=defaultBandwidth();
        probDensity();
    }
    
    public void loadData(double[] data){
        this.data=data;
        this.x=new double[size];
        this.probX=new double[size];
        this.min=data[0];
        this.max=data[0];
        this.n=data.length;
        this.meanX=data[0];
        this.meanXX=data[0]*data[0];
        for(int i=1;i<n;i++){
            this.min=data[i]<min?data[i]:min;
            this.max=data[i]>max?data[i]:max;
            this.meanX+=data[i];
            this.meanXX+=data[i]*data[i];
        }
        this.meanX/=n;
        this.meanXX/=n;
    }
    
    public double defaultBandwidth(){
        double sigma=Math.sqrt(meanXX-meanX*meanX);
        
        return sigma*Math.pow(4.0/(3*n),1.0/5.0);
//        return 0.9*sigma*Math.pow(n, -1.0/5);
    }
    
    public void probDensity(){
        double[] r=extended_Range();
        double extMin=r[0];
        double extMax=r[1];
        double step=(extMax-extMin)/size;
        for(int i=0;i<size;i++){
            x[i]=extMin+step*i;
            probX[i]=prob(getX()[i]);
        }
    }
    
    public double optimalBandwidth(){
        int maxIterrationStep=25;
        double eps=1e-3;
        return optimalBandwidth(maxIterrationStep, eps);
    }
    
    public double optimalBandwidth(int stepNumber, double eps){
        double x0=defaultBandwidth();
        double y0=optimalBandwidthEquation(x0);
        
        double x=0.8*x0;
        double y=optimalBandwidthEquation(x);
        
        double dx=0;
        
        int i=0;
        while(i++<stepNumber){
            x-=y*(x0-x)/(y0-y);
            y=optimalBandwidthEquation(x);
            if(Math.abs(y)<eps*y0) break;
        }
        return x;
    }
        
    
    public double optimalBandwidthSafe(){
        double x0=defaultBandwidth()/n;
        double x1=defaultBandwidth()*2;
        double eps=1e-3;
        return optimalBandwidthSafe(x0, x1, eps);
    }
    
    public double optimalBandwidthSafe(double x0, double x1, double eps){
        double y0=optimalBandwidthEquation(x0);
        double y1=optimalBandwidthEquation(x1);
        
        if(y0*y1>=0){
            System.out.println("Warning: failed to find optimal bandwidth, default bandwidth will be used!");
            return defaultBandwidth();
        }
        double x=0,y=0;
        int i=0;
        while(Math.abs(x0-x1)>eps*x1){
            i+=1;
            x=(x0+x1)/2;
            y=optimalBandwidthEquation(x);
            if(Math.abs(y)<eps*y0) break;
            if(y*y0<0){
                x1=x;
                y1=y;
            }
            else{
                x0=x;
                y0=y;
            }               
        }
        return x;
    }
    
    private double optimalBandwidthEquation(double w) {
        double alpha=1.0/(2.0*Math.sqrt(3.14159265358979323846));
        double sigma=1.0;
        double q=stiffnessIntegral(w);
        return w-Math.pow(n*q*Math.pow(sigma,4)/alpha,-1.0/5.0);
    }
    
    private double stiffnessIntegral(double w){
        double eps=1e-4;
        double[] r=extended_Range();
        double mn=r[0];
        double mx=r[1];
        int n=1;
        double dx=(mx-mn)/n;
        double yy=0.5*(Math.pow(curvature(mn,w),2)+Math.pow(curvature(mx,w),2))*dx;
        double maxn=(mx-mn)/Math.sqrt(eps);
        maxn=maxn>2048?2048:maxn;
        for(n=2;n<=maxn;n*=2){
            dx/=2.0;
            double y=0;
            for(int i=1;i<=n-1;i+=2)
                y+=Math.pow(curvature(mn+i*dx,w),2);
            yy=0.5*yy+y*dx;
            if(n>8 && Math.abs(y*dx-0.5*yy)<eps*yy) break;
        }
        return yy;
    }
    
    private double curvature(double x, double w){
        double y=0;
        for(int i=0;i<this.n;i++){
            y+=gaussCurvature(x,getData()[i],w);
        }
        return y/this.n;
    }
    
    private double gaussCurvature(double x, double m, double s){
        double z=(x-m)/s;
        return (z*z-1.0)*gauss(x, m, s)/(s*s);
    }
    
    private double prob(double x){
        double y=0;
        for(int i=0;i<this.n;i++){
            y+=gauss(x,getData()[i],this.getBandwidth());           
        }
        return y/this.n;
    }
    
    private double gauss(double x, double u, double s){
        double z=(x-u)/s;
        return Math.exp(-0.5*z*z)/(s*Math.sqrt(2.0*3.14159265358979323846));
    }
    
    private double[] extended_Range(){
        double extBandW=this.extension*this.getBandwidth();
        double extMin=this.min-extBandW;
        double extMax=this.max+extBandW;
        return new double[]{extMin,extMax};
    }

    /**
     * @return the data
     */
    public double[] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(double[] data) {
        this.data = data;
    }

    /**
     * @return the bandwidth
     */
    public double getBandwidth() {
        return bandwidth;
    }

    /**
     * @param bandwidth the bandwidth to set
     */
    public void setBandwidth(double bandwidth) {
        this.bandwidth = bandwidth;
    }

    /**
     * @return the x
     */
    public double[] getX() {
        return x;
    }

    /**
     * @return the probX
     */
    public double[] getProbX() {
        return probX;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    
    
}
