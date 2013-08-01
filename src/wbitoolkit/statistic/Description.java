/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.statistic;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author wb
 */
public class Description {
    
    public static double mean(double[] a){
        
        double mean=0;
        for(int i=0;i<a.length;i++)
            mean+=a[i];
        mean/=a.length;
        return mean;
    }
    
    public static double mean(Double[] a){
        double mean=0;
        for(int i=0;i<a.length;i++)
            mean+=a[i];
        mean/=a.length;
        return mean;
    }
    
    public static double mean(List<Double> a){
        double mean=0;
        for(int i=0;i<a.size();i++)
            mean+=a.get(i);
        mean/=a.size();
        return mean;
    }
    
    public static double sd(double[] a){
        double sd=0;
        double mean=mean(a);
        for(int i=0;i<a.length;i++)
            sd+=(a[i]-mean)*(a[i]-mean);
        //sd=Math.sqrt(sd/(a.length-1));
        sd=Math.sqrt(sd/a.length);
        
        return sd;
    }
    
    public static double sd(List<Double> a){
        double sd=0;
        double mean=mean(a);
        for(int i=0;i<a.size();i++)
            sd+=(a.get(i)-mean)*(a.get(i)-mean);
        sd=Math.sqrt(sd/a.size());
        return sd;
    }
    
    public static double min(Double[][] dm){
        double m=dm[0][1];
        for(int i=0;i<dm.length-1;i++)
            for(int j=i+1;j<dm.length;j++)
                if(m>dm[i][j])
                    m=dm[i][j];
        return m;
    }
    public static double min(Double[] d){
        double m=d[0];
        for(int i=1;i<d.length;i++)
            if(m>d[i])
                m=d[i];
        return m;
    }
    public static double min(double[] d){
        double m=d[0];
        for(int i=1;i<d.length;i++)
            if(m>d[i])
                m=d[i];
        return m;
    }
    public static double max(Double[][] dm){
        double m=dm[0][1];
        for(int i=0;i<dm.length-1;i++)
            for(int j=i+1;j<dm.length;j++)
                if(m<dm[i][j])
                    m=dm[i][j];
        return m;
    }
    public static double max(Double[] d){
        double m=d[0];
        for(int i=1;i<d.length;i++)
            if(m<d[i])
                m=d[i];
        return m;
    }
    public static double max(double[] d){
        double m=d[0];
        for(int i=1;i<d.length;i++)
            if(m<d[i])
                m=d[i];
        return m;
    }
    
    public static double trimDecimalTo(double d, int n){
        double b=Math.pow(10, n);
        return ((int)(d*b))/b;      
    }
    
    public static double quantile(Double[] data, double cutoff){
        Arrays.sort(data);
        return data[(int)(data.length*cutoff)];
    }
}
